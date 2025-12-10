package com.lei.mall.service;

import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付宝支付服务
 * @author lei
 */
public interface AliPayService {

    /**
     * 生成支付二维码
     * @param itemId 商品ID
     * @param num 购买数量
     * @param request HTTP请求对象
     * @return 二维码URL
     */
    String generatePayQrCode(Long itemId, Integer num, HttpServletRequest request);

    /**
     * 处理支付回调
     * @param outTradeNo 商户订单号
     * @param tradeStatus 交易状态
     * @param tradeNo 支付宝交易号
     * @param totalAmount 支付金额
     * @return 处理结果
     */
    boolean handlePayNotify(String outTradeNo, String tradeStatus, String tradeNo, String totalAmount);

    /**
     * 查询支付状态
     * @param outTradeNo 商户订单号
     * @return 支付状态响应
     */
    AlipayTradeQueryResponse queryPayStatus(String outTradeNo);
}
