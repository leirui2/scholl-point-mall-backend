package com.lei.usercenter.model.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改密码请请求体
 * @author lei
 */
@Data
public class UpdateUserPsswordRequest implements Serializable {
    /**
     * 老密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String confirmPassword;


    private static final long serialVersionUID = 1L;
}