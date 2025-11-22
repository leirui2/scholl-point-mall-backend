package com.lei.mall.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志实体
 * @author lei
 * @TableName login_log
 */
@TableName(value = "login_log")
@Data
public class LoginLog implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 登录IP
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 登录时间
     */
    @TableField(value = "loginTime")
    private Date loginTime;

    /**
     * 用户代理
     */
    @TableField(value = "userAgent")
    private String userAgent;

    /**
     * 登录状态 0-成功 1-失败
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 错误信息
     */
    @TableField(value = "errorMsg")
    private String errorMsg;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}