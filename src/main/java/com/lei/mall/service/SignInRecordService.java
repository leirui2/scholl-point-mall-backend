package com.lei.mall.service;

import com.lei.mall.common.ApiResponse;
import com.lei.mall.model.entity.SignInRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author lei
* @description 针对表【sign_in_record(签到记录表)】的数据库操作Service
* @createDate 2025-12-05 22:56:30
*/
public interface SignInRecordService extends IService<SignInRecord> {

    /**
     * 签到
     * @return 签到获得的积分点数
     */
    ApiResponse<Integer> signIn(HttpServletRequest request);
}
