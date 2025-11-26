package com.lei.mall.model.request;

import com.lei.mall.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 商品类别查询请求体
 * @author lei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryQueryRequest extends PageRequest implements Serializable {

    /**
     * 类别名称
     */
    private String name;


    private static final long serialVersionUID = 1L;
}