package com.lei.mall.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lei
 * 商品信息VO类
 */
@Data
public class ItemCategoryVO {
    // 商品信息
    private Long id;
    private Long categoryid;
    private String name;
    private String description;
    private String imageurl;
    private Integer price;
    private Long order_count;
    private Integer stock;
    private String unit;
    private Integer status;

    // 类别信息
    private String categoryName;
    
    // 时间戳
    private Date createTime;
    private Date updateTime;
}