package com.lei.mall.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lei
 */
@Data
public class PurchaseItemRequest implements Serializable {
    private Long itemId;
    private Integer num;
    // 1=积分支付，2=支付宝支付
    private Integer paymentType;
    private static final long serialVersionUID = 1L;
}
