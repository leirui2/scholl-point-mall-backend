package com.lei.mall.service;

import com.lei.mall.common.PageResult;
import com.lei.mall.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mall.model.request.*;
import com.lei.mall.model.vo.UserLoginVO;
import com.lei.mall.model.vo.UserUpdateVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author lei
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-11-08 14:53:59
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    long userRegister(UserRegisterRequest user);

    /**
     * 用户登录
     */
    UserLoginVO userlogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 获取当前登录用户
     */
    UserLoginVO getLoginUser(HttpServletRequest request);

    /**
     * 退出登录
     * @param request
     * @return
     */
    boolean logout(HttpServletRequest request);

    /**
     * 修改用户信息
     * @param userUpdateRequest
     * @param request
     * @return
     */
    UserUpdateVO updateUser(UserUpdateRequest userUpdateRequest, HttpServletRequest request);

    /**
     * 管理员根据ID获取用户完整信息
     * @param id 用户ID
     * @param request HTTP请求
     * @return 用户完整信息
     */
    User getUserById(long id, HttpServletRequest request);

    /**
     * 普通用户根据ID获取脱敏用户信息
     * @param id 用户ID
     * @param request HTTP请求
     * @return 脱敏用户信息
     */
    UserLoginVO getPublicUserInfoById(long id, HttpServletRequest request);

    /**
     * 分页查询用户列表
     * @param userQueryRequest 查询条件
     * @param request HTTP请求
     * @return 分页结果
     */
    PageResult<User> listUsersByPage(UserQueryRequest userQueryRequest, HttpServletRequest request);

    /**
     * 更新用户状态（启用/禁用）
     * @param id 用户ID
     * @param status 用户状态 0-正常 1-禁用
     * @param request HTTP请求
     * @return 是否更新成功
     */
    boolean updateUserStatus(long id, int status, HttpServletRequest request);

    /**
     * 更新用户角色（普通用户/管理员）
     * @param id 用户ID
     * @param role 用户角色 0-普通用户 1-管理员
     * @param request HTTP请求
     * @return 是否更新成功
     */
    boolean updateUserRole(long id, int role, HttpServletRequest request);

    /**
     * 逻辑删除用户
     * @param id 用户ID
     * @param request HTTP请求
     * @return 是否删除成功
     */
    boolean deleteUser(long id, HttpServletRequest request);

    /**
     * 修改密码
     * @param updateUserPsswordRequest
     * @param request
     * @return
     */
    Boolean updateUserPassword(UpdateUserPsswordRequest updateUserPsswordRequest, HttpServletRequest request);


    /**
     * 管理员重置用户密码
     * @param id 用户ID
     * @param request HTTP请求
     * @return 是否更新成功
     */
    boolean resetPassword(long id, HttpServletRequest request);

    /**
     * 管理员修改用户信息
     */
    UserUpdateVO updateUserByAdmin(UserUpdateRequest userUpdateRequest, HttpServletRequest request);

}