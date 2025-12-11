package com.lei.mall.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.lei.mall.common.ApiResponse;
import com.lei.mall.common.ErrorCode;
import com.lei.mall.common.ResultUtils;
import com.lei.mall.config.AlipayConfig;
import com.lei.mall.exception.BusinessException;
import com.lei.mall.model.entity.Item;
import com.lei.mall.model.entity.PayResultDTO;
import com.lei.mall.model.entity.PurchaseRecord;
import com.lei.mall.model.entity.TransactionRecord;
import com.lei.mall.service.*;
import com.lei.mall.utils.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付宝支付服务实现
 * @author lei
 */
@Service
@Slf4j
public class AliPayServiceImpl implements AliPayService {

    @Resource
    private AlipayConfig alipayConfig;

    @Resource
    private ItemService itemService;

    @Resource
    private UserService userService;

    @Resource
    private PurchaseRecordService purchaseRecordService;

    @Resource
    private PointTransactionService pointTransactionService;

    @Override
    public ApiResponse<PayResultDTO> generatePayQrCode(Long itemId, Integer num, HttpServletRequest request) {
        try {
            // 从请求中获取用户ID
            Long userId = userService.getLoginUser(request).getId();

            // 1. 验证参数
            if (itemId == null || num == null || userId == null || num <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(),"参数无效");
            }

            // 2. 获取商品信息
            Item item = itemService.getById(itemId);
            if (item == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(),"商品不存在");
            }

            // 3. 计算总价（假设商品价格单位为分）
            BigDecimal totalAmount = new BigDecimal(item.getPointPrice() * num).divide(new BigDecimal(100));

            // 4. 生成订单号
            String outTradeNo = OrderNoUtils.generateOrderNo();

            // 5. 创建预订单记录
            PurchaseRecord record = new PurchaseRecord();
            record.setItemId(itemId);
            record.setUserId(userId);
            record.setItemName(item.getName());
            record.setNum(num);
            record.setOrderNumber(outTradeNo);
            record.setPaymentStatus(0); // 待支付
            record.setPaymentAmount(item.getPointPrice() * num);
            purchaseRecordService.save(record);

            // 6. 调用支付宝API生成二维码
            Factory.setOptions(alipayConfig.config());
            // 创建预创建模型并设置超时时间
            AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace()
                    .preCreate(item.getName(), outTradeNo, totalAmount.toString());

            // 7. 解析响应获取二维码URL
            JSONObject json = JSONObject.parseObject(response.getHttpBody());
            JSONObject tradeResp = json.getJSONObject("alipay_trade_precreate_response");

            String qrcode = tradeResp.getString("qr_code");

            // 生成支付二维码和订单号的逻辑
            PayResultDTO payResultDTO = new PayResultDTO();
            payResultDTO.setQrCodeUrl(qrcode);
            payResultDTO.setOrderNumber(outTradeNo);
            payResultDTO.setItemName(item.getName());
            payResultDTO.setTotalAmount(totalAmount);

            return ResultUtils.success(payResultDTO);

        } catch (Exception e) {
            log.error("生成支付二维码失败: {}", e.getMessage(), e);
            throw new RuntimeException("生成支付二维码失败", e);
        }
    }

    @Override
    public boolean handlePayNotify(String outTradeNo, String tradeStatus, String tradeNo, String totalAmount) {
        try {
            // 1. 验证参数
            if (outTradeNo == null || tradeStatus == null) {
                return false;
            }

            // 2. 查询订单
            PurchaseRecord record = purchaseRecordService.lambdaQuery()
                    .eq(PurchaseRecord::getOrderNumber, outTradeNo)
                    .one();

            if (record == null) {
                return false;
            }

            // 3. 验证支付状态 ，如果是已支付，直接返回
            if (record.getPaymentStatus() == 1) {
                return true; // 幂等处理
            }

            // 4. 验证金额
            BigDecimal actualAmount = new BigDecimal(totalAmount).multiply(new BigDecimal(100));
            if (actualAmount.intValue() != record.getPaymentAmount()) {
                return false;
            }

            // 5. 更新订单状态
            if ("TRADE_SUCCESS".equals(tradeStatus)) {
                // 更改状态为 已支付
                record.setPaymentStatus(1);
                record.setPaymentTime(new Date());
                purchaseRecordService.updateById(record);

                //交易流水记录表
                TransactionRecord transactionRecord = new TransactionRecord();
                transactionRecord.setPoints(0);
                transactionRecord.setPayType(2);
                transactionRecord.setMoney(String.valueOf(actualAmount));
                //积分变动类型 (1: 签到奖励, 2: 兑换商品, 3: 补签扣除等)
                transactionRecord.setType(2);
                transactionRecord.setUserId(record.getUserId());
                //获取签到记录表对象的ID
                transactionRecord.setBusinessId(record.getId());
                String msg =  "购买"+record.getId()+"商品成功，消费了 " + actualAmount + "元。" ;
                transactionRecord.setDescription(msg);
                boolean res = pointTransactionService.save(transactionRecord);
                if (!res) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR.getCode(), "交易流水记录表保存失败");
                }

                // 6. 更新商品库存
                Item item = itemService.getById(record.getItemId());
                if (item != null) {
                    item.setStock(item.getStock() - record.getNum());
                    item.setOrderCount(item.getOrderCount() + 1);
                    itemService.updateById(item);
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("处理支付回调失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public AlipayTradeQueryResponse queryPayStatus(String outTradeNo) {
        try {
            Factory.setOptions(alipayConfig.config());
            return Factory.Payment.Common().query(outTradeNo);
        } catch (Exception e) {
            log.error("查询支付状态失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询支付状态失败", e);
        }
    }
}