package com.lei.mall.model.request;

import com.lei.mall.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 热门商品查询请求体
 * @author lei
 * @TableName item
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HotItemQueryRequest extends PageRequest implements Serializable {

    /**
     * 按照销量查询前num名商品信息
     */
    private Long num;

    private static final long serialVersionUID = 1L;
}