package com.lei.mall.controller;

import com.baomidou.mybatisplus.annotation.TableField;
import com.lei.mall.common.ApiResponse;
import com.lei.mall.common.PageResult;
import com.lei.mall.common.ResultUtils;
import com.lei.mall.model.entity.Category;
import com.lei.mall.model.entity.SignInRecord;
import com.lei.mall.model.request.CategoryQueryRequest;
import com.lei.mall.model.request.SignInRecordQueryRequest;
import com.lei.mall.service.SignInRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 签到记录表前端操作接口
 * @author lei
 * @TableName sign_in_record
 */
@RestController
@RequestMapping("/signInRecord")
public class SignInRecordController {

    @Resource
    private SignInRecordService signInRecordService;

    /**
     * 签到
     * @return 签到获得的积分点数
     */
    @PostMapping("/signIn")
    public ApiResponse<Integer> signIn(HttpServletRequest  request){
        return signInRecordService.signIn(request);
    }


    /**
     * 分页查询签到记录
     * @param signInRecordQueryRequest 查询条件
     * @param request HTTP请求
     * @return 签到记录列表分页结果
     */
    @PostMapping("/listSignInRecordByPage")
    public ApiResponse<PageResult<SignInRecord>> listSignInRecordByPage(@RequestBody SignInRecordQueryRequest signInRecordQueryRequest, HttpServletRequest request) {
        PageResult<SignInRecord> pageResult = signInRecordService.listSignInRecordByPage(signInRecordQueryRequest, request);
        return ResultUtils.success(pageResult);
    }

    /**
     * 分页查询自己的签到记录
     * @param signInRecordQueryRequest 查询条件
     * @param request HTTP请求
     * @return 签到记录列表分页结果
     */
    @PostMapping("/listSignInMyRecordByPage")
    public ApiResponse<PageResult<SignInRecord>> listSignInMyRecordByPage(@RequestBody SignInRecordQueryRequest signInRecordQueryRequest, HttpServletRequest request) {
        PageResult<SignInRecord> pageResult = signInRecordService.listSignInMyRecordByPage(signInRecordQueryRequest, request);
        return ResultUtils.success(pageResult);
    }

    /**
     * 判断当前ID用户是否签到
     * @param request HTTP请求
     * @return 是否签到
     */
    @PostMapping("/isSignIn")
    public ApiResponse<Boolean> isSignIn(HttpServletRequest request){
        return ResultUtils.success(signInRecordService.isSignIn(request));
    }
}
