package com.lei.mall.model.request;

import com.lei.mall.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户查询请求体
 * @author lei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户状态： 0 - 正常 1-ban
     */
    private Integer userStatus;

    /**
     * 用户角色: 0 - 普通用户 1 - 管理员
     */
    private Integer userRole;

    private static final long serialVersionUID = 1L;
}