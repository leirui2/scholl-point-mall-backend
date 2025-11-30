package com.lei.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mall.common.ErrorCode;
import com.lei.mall.exception.BusinessException;
import com.lei.mall.model.entity.Item;
import com.lei.mall.model.entity.PurchaseRecord;
import com.lei.mall.model.entity.User;
import com.lei.mall.model.request.PurchaseItemRequest;
import com.lei.mall.model.vo.UserLoginVO;
import com.lei.mall.service.ItemService;
import com.lei.mall.service.PurchaseRecordService;
import com.lei.mall.mapper.PurchaseRecordMapper;
import com.lei.mall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author lei
* @description 针对表【purchase_record(购买记录表)】的数据库操作Service实现
* @createDate 2025-11-29 19:07:48
*/
@Service
@Slf4j
public class PurchaseRecordServiceImpl extends ServiceImpl<PurchaseRecordMapper, PurchaseRecord>
    implements PurchaseRecordService{

    @Resource
    private ItemService itemService;


    @Resource
    private UserService userService;

    /**
     * 下单商品
     * @param purchaseItemRequest 下单请求
     */
    @Override
    @Transactional(rollbackFor = Exception.class)   //事务
    public Boolean purchaseItem(PurchaseItemRequest purchaseItemRequest, HttpServletRequest request) throws BusinessException {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数错误");
        }
        Long itemId = purchaseItemRequest.getItemId();
        Long userId = userService.getLoginUser(request).getId();
        Integer num = purchaseItemRequest.getNum();

        if(itemId == null || userId == null || num == null || num <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "下单商品参数缺少或无效");
        }
        // 获取当前用户
        UserLoginVO loginUser = userService.getLoginUser(request);
        //获取当前商品
        Item item = itemService.getById(itemId);

        // 检查商品状态 // 假设0表示上架状态
        if (item.getStatus() != 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "商品已下架");
        }
        // 检查用户状态 // 假设0表示正常状态
        if (loginUser.getUserStatus() != 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "用户账户异常");
        }

        //剩余积分
        int remainingPoints = loginUser.getPoints() - (item.getPrice() * num);
        if ( remainingPoints < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(),"剩余积分不够");
        }
        if(item.getStock() < num){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(),"商品库存不够");
        }

        try {
            // 购买逻辑
            // 任何抛出的 Exception 都会导致事务回滚

            //修改用户剩余积分
            User user = userService.getById(loginUser.getId());
            user.setPoints(remainingPoints);
            userService.updateById(user);

            //修改商品剩余数量
            item.setStock(item.getStock()-num);
            //修改商品订单数+1
            item.setOrder_count(item.getOrder_count()+1);
            itemService.updateById(item);

            //添加购买记录
            PurchaseRecord purchaseRecord = new PurchaseRecord();
            purchaseRecord.setItemId(itemId);
            purchaseRecord.setUserId(userId);
            purchaseRecord.setNum(num);

            this.save(purchaseRecord);

        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            // 系统异常记录日志并抛出
            log.error("购买商品时发生系统错误，itemId={}, userId={}, num={}", itemId, userId, num, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR.getCode(), "购买失败，系统内部错误");
        }

        return true;
    }
}