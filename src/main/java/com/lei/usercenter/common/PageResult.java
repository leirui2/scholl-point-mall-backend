package com.lei.usercenter.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装
 *
 * @author lei
 */
@Data
public class PageResult<T> implements Serializable {

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总数
     */
    private long total;

    /**
     * 当前页号
     */
    private long current;

    /**
     * 页面大小
     */
    private long size;

    private static final long serialVersionUID = 1L;
}