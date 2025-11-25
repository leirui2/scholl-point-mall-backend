package com.lei.mall.controller;

import com.lei.mall.common.ApiResponse;
import com.lei.mall.common.PageResult;
import com.lei.mall.common.ResultUtils;
import com.lei.mall.exception.BusinessException;
import com.lei.mall.model.entity.User;
import com.lei.mall.model.request.*;
import com.lei.mall.model.vo.UserLoginVO;
import com.lei.mall.model.vo.UserUpdateVO;
import com.lei.mall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户接口
 *
 * @author lei
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 注册用户
     * @param user 用户注册请求参数
     * @return 用户ID
     */
    @PostMapping("/register")
    public ApiResponse<Long> register(@RequestBody UserRegisterRequest user) {
        log.info("用户注册：{}", user);
        if (StringUtils.isAnyBlank(user.getUserAccount(), user.getUserPassword(), user.getCheckPassword())) {
            throw new BusinessException("用户信息不能为空");
        }
        long id = userService.userRegister(user);
        return ResultUtils.success(id);
    }


    /**
     * 用户登录
     * @param userLoginRequest 用户登录请求参数
     * @param request    HTTP请求
     * @return 用户登录信息
     */
    @PostMapping("/login")
    public ApiResponse<UserLoginVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        log.info("用户登录：{}", userLoginRequest);
        if (request == null) {
            throw new BusinessException("请求错误");
        }
        if (StringUtils.isAnyBlank(userLoginRequest.getUserAccount(), userLoginRequest.getUserPassword())) {
            throw new BusinessException("用户信息缺少");
        }

        UserLoginVO userLoginVO = userService.userlogin(userLoginRequest, request);
        return ResultUtils.success(userLoginVO);
    }


    /**
     * 获取当前登录用户
     * @param request    HTTP请求
     * @return 用户登录信息（脱敏）
     */
    @GetMapping("/getLoginUser")
    public ApiResponse<UserLoginVO> getLoginUser(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException("请求错误");
        }
        UserLoginVO userLoginVO = userService.getLoginUser(request);
        return ResultUtils.success(userLoginVO);
    }

    /**
     * 退出登录
     * @param request  HTTP请求
     *  @return 退出登录结果
     */
    @PostMapping("/logout")
    public ApiResponse<Boolean> logout(HttpServletRequest request){
        boolean flag = userService.logout(request);
        return ResultUtils.success(flag);
    }


    /**
     * 用户信息修改
     */
    @PostMapping("/update")
    public ApiResponse<UserUpdateVO> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,HttpServletRequest request){
        log.info("修改用户信息：{}", userUpdateRequest);
        if (request == null) {
            throw new BusinessException("请求错误");
        }
        if (StringUtils.isAnyBlank(userUpdateRequest.getUserAccount(), userUpdateRequest.getId().toString())) {
            throw new BusinessException("用户信息缺少");
        }
        UserUpdateVO userUpdateVO = userService.updateUser(userUpdateRequest,request);
        return ResultUtils.success(userUpdateVO);
    }


    /**
     * 修改密码
     */
    @PostMapping("/updateUserPassword")
    public ApiResponse<Boolean> updateUserPassword(@RequestBody UpdateUserPsswordRequest updateUserPsswordRequest, HttpServletRequest request){
        log.info("修改密码：{}", updateUserPsswordRequest);
        if (request == null) {
            throw new BusinessException("请求错误");
        }
        if (StringUtils.isAnyBlank(updateUserPsswordRequest.getUserPassword(),updateUserPsswordRequest.getConfirmPassword(),updateUserPsswordRequest.getOldPassword())) {
            throw new BusinessException("用户信息缺少");
        }
       Boolean res = userService.updateUserPassword(updateUserPsswordRequest,request);
        return ResultUtils.success(res);
    }


    /**
     * 管理员根据ID查询用户完整信息
     * @param id 用户ID
     * @param request HTTP请求
     * @return 用户完整信息
     */
    @GetMapping("/admin/getUserById")
    public ApiResponse<User> getUserById(long id, HttpServletRequest request) {
        User user = userService.getUserById(id, request);
        return ResultUtils.success(user);
    }

    /**
     * 根据ID查询用户公开信息（脱敏）
     * @param id 用户ID
     * @param request HTTP请求
     * @return 用户公开信息
     */
    @GetMapping("/getPublicUserInfoById")
    public ApiResponse<UserLoginVO> getPublicUserInfoById(long id, HttpServletRequest request) {
        UserLoginVO userLoginVO = userService.getPublicUserInfoById(id, request);
        return ResultUtils.success(userLoginVO);
    }

    /**
     * 管理员分页查询用户列表
     * @param userQueryRequest 查询条件
     * @param request HTTP请求
     * @return 用户列表分页结果
     */
    @PostMapping("/admin/listUsersByPage")
    public ApiResponse<PageResult<User>> listUsersByPage(@RequestBody UserQueryRequest userQueryRequest, HttpServletRequest request) {
        PageResult<User> pageResult = userService.listUsersByPage(userQueryRequest, request);
        return ResultUtils.success(pageResult);
    }

    /**
     * 管理员更新用户状态（启用/禁用）
     * @param id 用户ID
     * @param status 用户状态 0-正常 1-禁用
     * @param request HTTP请求
     * @return 是否更新成功
     */
    @PostMapping("/admin/updateUserStatus")
    public ApiResponse<Boolean> updateUserStatus(@RequestParam long id, @RequestParam int status, HttpServletRequest request) {
        boolean result = userService.updateUserStatus(id, status, request);
        return ResultUtils.success(result);
    }


    /**
     * 管理员重置用户密码
     * @param id 用户ID
     * @param request HTTP请求
     * @return 是否更新成功
     */
    @PostMapping("/admin/resetPassword")
    public ApiResponse<Boolean> resetPassword(@RequestParam long id, HttpServletRequest request) {
        boolean result = userService.resetPassword(id, request);
        return ResultUtils.success(result);
    }

    /**
     * 管理员更新用户角色（普通用户/管理员）
     * @param id 用户ID
     * @param role 用户角色 0-普通用户 1-管理员
     * @param request HTTP请求
     * @return 是否更新成功
     */
    @PostMapping("/admin/updateUserRole")
    public ApiResponse<Boolean> updateUserRole(@RequestParam long id, @RequestParam int role, HttpServletRequest request) {
        boolean result = userService.updateUserRole(id, role, request);
        return ResultUtils.success(result);
    }

    /**
     * 管理员逻辑删除用户
     * @param id 用户ID
     * @param request HTTP请求
     * @return 是否删除成功
     */
    @PostMapping("/admin/deleteUser")
    public ApiResponse<Boolean> deleteUser(@RequestParam long id, HttpServletRequest request) {
        boolean result = userService.deleteUser(id, request);
        return ResultUtils.success(result);
    }

    /**
     * 管理员修改用户信息
     */
    @PostMapping("/admin/update")
    public ApiResponse<UserUpdateVO> updateUserByAdmin(@RequestBody UserUpdateRequest userUpdateRequest,HttpServletRequest request){
        log.info("管理员修改用户信息：{}", userUpdateRequest);
        if (request == null) {
            throw new BusinessException("请求错误");
        }
        if (StringUtils.isAnyBlank(userUpdateRequest.getUserAccount(), userUpdateRequest.getId().toString())) {
            throw new BusinessException("用户信息缺少");
        }
        UserUpdateVO userUpdateVO = userService.updateUserByAdmin(userUpdateRequest,request);
        return ResultUtils.success(userUpdateVO);
    }

}