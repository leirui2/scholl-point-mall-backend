package com.lei.mall.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户登录VO（脱敏）
 * @author lei
 */

@Data
public class UserLoginVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;


    /**
     * 用户状态： int  0 - 正常    1-ban
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}