package com.lei.mall.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lei
 * 类别信息VO类
 */
@Data
public class CategoryVO implements Serializable {
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


    /**
     * 总订单数
     */
    private Integer totalOrderCount;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}