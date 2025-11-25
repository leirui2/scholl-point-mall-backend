package com.lei.mall.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品类别表
 * @author lei
 * @TableName category
 */
@TableName(value ="category")
@Data
public class Category implements Serializable {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 类别名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 类别描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 状态：0启用   -    1禁用，
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 逻辑删除标志：0-未删除，1-已删除
     */
    @TableLogic
    @TableField(value = "isDelete")
    private Integer isDelete;

    /**
     * 
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}