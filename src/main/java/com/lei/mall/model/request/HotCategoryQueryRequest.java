package com.lei.mall.model.request;

import com.lei.mall.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 热门类别查询请求体
 * @author lei
 * @TableName item
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HotCategoryQueryRequest extends PageRequest implements Serializable {

    /**
     * 按照销量查询前num名热门类别
     */
    private Long num;

    private static final long serialVersionUID = 1L;
}