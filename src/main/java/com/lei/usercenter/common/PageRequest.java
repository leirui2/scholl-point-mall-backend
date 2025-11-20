package com.lei.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求
 *
 * @author lei
 */
@Data
public class PageRequest implements Serializable {

    /**
     * 当前页号
     */
    private int current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    private static final long serialVersionUID = 1L;
}