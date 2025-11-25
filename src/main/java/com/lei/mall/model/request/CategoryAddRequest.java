package com.lei.mall.model.request;

import lombok.Data;
import java.io.Serializable;

/**
 * 商品类别增加请求体
 * @author lei
 */
@Data
public class CategoryAddRequest implements Serializable {
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