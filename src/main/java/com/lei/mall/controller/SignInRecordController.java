package com.lei.mall.controller;

import com.baomidou.mybatisplus.annotation.TableField;
import com.lei.mall.common.ApiResponse;
import com.lei.mall.common.ResultUtils;
import com.lei.mall.model.entity.SignInRecord;
import com.lei.mall.service.SignInRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


}
