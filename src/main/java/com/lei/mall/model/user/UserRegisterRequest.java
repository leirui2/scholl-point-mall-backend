package com.lei.mall.model.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 注册请求体
 * @author lei
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
