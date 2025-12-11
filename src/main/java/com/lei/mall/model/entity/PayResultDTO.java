package com.lei.mall.model.entity;

// 新增支付结果DTO

import lombok.Data;

import java.math.BigDecimal;
/**
 * @author lei
 */
@Data
public class PayResultDTO {
    private String qrCodeUrl;  // 支付二维码URL
    private String orderNumber;  // 订单号
    private String itemName;  // 商品名称（可选，用于前端展示）
    private BigDecimal totalAmount;  // 支付金额（可选，用于前端展示）

}
