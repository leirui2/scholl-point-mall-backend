package com.lei.mall.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品信息表脱敏
 * @author lei
 * @TableName item
 */
@Data
public class ItemVO implements Serializable {


    private Long categoryid;

    private String name;

    private String description;

    private String imageurl;

    private Integer price;

    private String unit;

    private static final long serialVersionUID = 1L;
}