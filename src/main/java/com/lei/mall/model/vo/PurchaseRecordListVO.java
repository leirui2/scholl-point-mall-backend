package com.lei.mall.model.vo;

import com.lei.mall.model.entity.Item;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 购买记录列表VO
 * @author lei
 */
@Data
public class PurchaseRecordListVO  implements Serializable {

    private Long id;
    private Long itemId;
    private Item item;
    private String itemName;
    private Long userId;
    private Integer num;
    private String orderNumber;
    private Date createTime;

    private static final long serialVersionUID = 1L;
}