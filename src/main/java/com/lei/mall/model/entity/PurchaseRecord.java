package com.lei.mall.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 购买记录表
 * @author lei
 * @TableName purchase_record
 */
@TableName(value ="purchase_record")
@Data
public class PurchaseRecord implements Serializable {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品ID
     */
    @TableField(value = "itemId")
    private Long itemId;

    /**
     * 订单号
     */
    @TableField(value = "orderNumber")
    private String orderNumber;

    /**
     * 商品名称
     */
    @TableField(value = "itemName")
    private String itemName;

    /**
     * 用户ID
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 逻辑删除标志：0-未删除，1-已删除
     */
    @TableLogic
    @TableField(value = "isDelete")
    private Integer isDelete;

    /**
     * 下单数量
     */
    @TableField(value = "num")
    private Integer num;

    @TableField(value = "createTime")
    private Date createTime;

    @TableField(value = "updateTime")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}