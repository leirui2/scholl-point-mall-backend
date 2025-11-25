package com.lei.mall.model.request;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品更新请求体
 * @author lei
 * @TableName item
 */
@Data
public class ItemUpdateRequest implements Serializable {

    // id
    private Long id;

    /**
     * 所属类别ID
     */
    private Long categoryid;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品图片URL
     */
    private String imageurl;

    /**
     * 积分价格
     */
    private BigDecimal price;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 计量单位
     */
    private String unit;


    private static final long serialVersionUID = 1L;
}