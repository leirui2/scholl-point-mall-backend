package com.lei.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志实体
 * @author lei
 * @TableName operation_log
 */
@TableName(value = "operation_log")
@Data
public class OperationLog implements Serializable {
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
     * 操作类型
     */
    @TableField(value = "operation")
    private String operation;

    /**
     * 请求方法
     */
    @TableField(value = "method")
    private String method;

    /**
     * 请求URI
     */
    @TableField(value = "uri")
    private String uri;

    /**
     * 操作IP
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 用户代理
     */
    @TableField(value = "userAgent")
    private String userAgent;

    /**
     * 请求参数
     */
    @TableField(value = "requestParams")
    private String requestParams;

    /**
     * 响应结果
     */
    @TableField(value = "responseResult")
    private String responseResult;

    /**
     * 操作时间
     */
    @TableField(value = "operationTime")
    private Date operationTime;

    /**
     * 耗时(毫秒)
     */
    @TableField(value = "costTime")
    private Long costTime;

    /**
     * 状态 0-成功 1-失败
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