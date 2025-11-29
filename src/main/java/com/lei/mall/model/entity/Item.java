package com.lei.mall.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 商品信息表
 * @author lei
 * @TableName item
 */
@TableName(value ="item")
@Data
public class Item implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 所属类别ID
     */
    @TableField(value = "categoryId")
    private Long categoryid;

    /**
     * 商品名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 商品描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 商品图片URL
     */
    @TableField(value = "imageUrl")
    private String imageurl;

    /**
     * 积分价格
     */
    @TableField(value = "price")
    private Integer price;

    /**
     * 库存数量
     */
    @TableField(value = "stock")
    private Integer stock;

    /**
     * 计量单位
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 状态：0-上架，1-下架
     */
    @TableField(value = "status")
    private Integer status;


    /**
     * 逻辑删除标志：0-未删除，1-已删除
     */
    @TableLogic
    @TableField(value = "isDelete")
    private Integer isDelete;

    /**
     * 
     */
    @TableField(value = "createTime")
    private Date createtime;

    /**
     * 
     */
    @TableField(value = "updateTime")
    private Date updatetime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}