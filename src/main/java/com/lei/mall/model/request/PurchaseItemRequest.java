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
    private static final long serialVersionUID = 1L;
}
