package com.lei.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户活跃度实体
 * @author lei
 * @TableName user_activity
 */
@TableName(value = "user_activity")
@Data
public class UserActivity implements Serializable {
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
     * 登录次数
     */
    @TableField(value = "loginCount")
    private Integer loginCount;

    /**
     * 最后登录时间
     */
    @TableField(value = "lastLoginTime")
    private Date lastLoginTime;

    /**
     * 最后活跃时间
     */
    @TableField(value = "lastActiveTime")
    private Date lastActiveTime;

    /**
     * 活跃度分数
     */
    @TableField(value = "activityScore")
    private Integer activityScore;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}