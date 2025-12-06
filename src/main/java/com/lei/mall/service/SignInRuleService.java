package com.lei.mall.service;

import com.lei.mall.model.entity.SignInRule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lei.mall.model.request.SignInRuleAddRequest;
import com.lei.mall.model.request.SignInRuleUpdateRequest;

import javax.servlet.http.HttpServletRequest;

/**
* @author lei
* @description 针对表【sign_in_rule(签到规则表)】的数据库操作Service
* @createDate 2025-12-05 23:00:24
*/
public interface SignInRuleService extends IService<SignInRule> {

    /*
     * 获取签到规则详情
     * @param id 签到规则ID
     * @return 签到规则详情
     */
    SignInRule getSignInRuleDetail(Long id, HttpServletRequest request);


    /**
     * 管理员删除签到规则（逻辑删除）
     * @param id 规则ID
     * @param request HTTP请求
     * @return 是否删除成功
     */
    boolean deleteSignInRuleByAdmin(long id, HttpServletRequest request);

    /**
     * 管理员更新签到规则信息
     *
     * @param signInRuleUpdateRequest 签到规则更新请求
     * @param request                 HTTP请求
     * @return
     */
    SignInRule updateSignInRuleByAdmin(SignInRuleUpdateRequest signInRuleUpdateRequest, HttpServletRequest request);

    /**
     * 管理员新增签到规则
     * @param signInRuleAddRequest 签到规则新增请求
     * @param request HTTP请求
     * @return 新增的签到规则
     */
    SignInRule addSignInRuleByAdmin(SignInRuleAddRequest signInRuleAddRequest, HttpServletRequest request);
}
