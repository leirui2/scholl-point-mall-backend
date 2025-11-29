package com.lei.mall.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * @author lei
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账号
     */
    @TableField(value = "userAccount")
    private String userAccount;

    /**
     * 密码
     */
    @TableField(value = "userPassword")
    private String userPassword;

    /**
     * 用户昵称
     */
    @TableField(value = "userName")
    private String userName;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 性别
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 用户头像
     */
    @TableField(value = "userAvatar")
    private String userAvatar;

    /**
     * 用户简介
     */
    @TableField(value = "userProfile")
    private String userProfile;

    /**
     * 用户状态： int  0 - 正常 1-ban
     */
    @TableField(value = "userStatus")
    private Integer userStatus;

    /**
     * 剩余积分
     * 初始值为0
     */
    @TableField(value = "points")
    private Integer points;

    /**
     * 用户角色: 0 - 普通用户 1 - 管理员
     */
    @TableField(value = "userRole")
    private Integer userRole;

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

    /**
     * 是否删除
     */
    @TableLogic
    @TableField(value = "isDelete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}