package com.lei.mall.model.request;

import com.lei.mall.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 下单记录查询请求体
 * @author lei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseRecordQueryRequest extends PageRequest implements Serializable {

    //商品名
    private String itemName;
    //订单号
    private String orderNumber;
    private static final long serialVersionUID = 1L;
}