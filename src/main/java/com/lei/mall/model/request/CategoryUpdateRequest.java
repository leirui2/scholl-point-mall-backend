package com.lei.mall.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品类别修改请求体
 * @author lei
 */
@Data
public class CategoryUpdateRequest implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 类别名称
     */
    private String name;

    /**
     * 类别描述
     */
    private String description;

    private static final long serialVersionUID = 1L;
}