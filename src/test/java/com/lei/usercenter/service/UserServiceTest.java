package com.lei.usercenter.service;

import com.lei.usercenter.model.entity.User;
import com.lei.usercenter.model.user.UserRegisterRequest;
import com.lei.usercenter.model.vo.UserLoginVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testGetUserById() {
        // 创建测试用户
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setUserAccount("testadmin");
        registerRequest.setUserPassword("12345678");
        registerRequest.setCheckPassword("12345678");
        long adminId = userService.userRegister(registerRequest);

        // TODO: 需要模拟管理员登录状态来测试getUserById方法
        // 因为需要HttpServletRequest对象和Session状态，这里只是示意
    }

    @Test
    void testGetPublicUserInfoById() {
        // 创建测试用户
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setUserAccount("testuser");
        registerRequest.setUserPassword("12345678");
        registerRequest.setCheckPassword("12345678");
        long userId = userService.userRegister(registerRequest);

        // TODO: 需要模拟登录状态来测试getPublicUserInfoById方法
        // 因为需要HttpServletRequest对象和Session状态，这里只是示意
    }
}