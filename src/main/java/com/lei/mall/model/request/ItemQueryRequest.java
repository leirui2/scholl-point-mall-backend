package com.lei.mall.model.request;

import com.lei.mall.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品查询请求体
 * @author lei
 * @TableName item
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ItemQueryRequest extends PageRequest implements Serializable {

    /**
     * 所属类别ID
     */
    private Long categoryid;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 类别名称
     */
    private String categoryName;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 最低积分价格
     */
    private Integer minPointPrice;
    
    /**
     * 最高积分价格
     */
    private Integer maxPointPrice;

    /**
     * 最低库存数量
     */
    private Integer minStock;
    
    /**
     * 最高库存数量
     */
    private Integer maxStock;


    private static final long serialVersionUID = 1L;
}