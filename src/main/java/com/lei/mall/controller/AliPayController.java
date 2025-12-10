package com.lei.mall.controller;

import com.lei.mall.service.AliPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 支付宝支付控制器
 * @author lei
 */
@RestController
@Slf4j
public class AliPayController {

    @Resource
    private AliPayService aliPayService;

    /**
     * 生成支付二维码
     * @param itemId 商品ID
     * @param num 购买数量
     * @param request HTTP请求对象
     * @return 二维码URL
     */
    @GetMapping("/pay")
    public String generatePayQrCode(Long itemId, Integer num,HttpServletRequest request) {
        return aliPayService.generatePayQrCode(itemId, num,request);
    }

    /**
     * 支付宝回调接口
     * @param request 请求参数
     * @return 响应结果
     */
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        log.info("收到支付宝支付回调");

        // 获取回调参数
        String tradeStatus = request.getParameter("trade_status");
        String outTradeNo = request.getParameter("out_trade_no");
        String tradeNo = request.getParameter("trade_no");
        String totalAmount = request.getParameter("total_amount");

        log.info("回调参数: tradeStatus={}, outTradeNo={}, tradeNo={}, totalAmount={}",
                tradeStatus, outTradeNo, tradeNo, totalAmount);

        // 处理回调
        boolean result = aliPayService.handlePayNotify(outTradeNo, tradeStatus, tradeNo, totalAmount);

        return result ? "success" : "fail";
    }

    /**
     * 查询支付状态
     * @param outTradeNo 商户订单号
     * @return 支付状态
     */
    @GetMapping("/query")
    public String queryPayStatus(String outTradeNo) {
        return aliPayService.queryPayStatus(outTradeNo).getHttpBody();
    }
}