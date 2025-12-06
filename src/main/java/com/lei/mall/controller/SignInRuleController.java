package com.lei.mall.controller;

import com.lei.mall.common.ApiResponse;
import com.lei.mall.common.ResultUtils;
import com.lei.mall.exception.BusinessException;
import com.lei.mall.model.entity.SignInRule;
import com.lei.mall.model.request.SignInRuleAddRequest;
import com.lei.mall.model.request.SignInRuleUpdateRequest;
import com.lei.mall.model.request.UserUpdateRequest;
import com.lei.mall.model.vo.UserUpdateVO;
import com.lei.mall.service.SignInRuleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 签到规则接口
 * @author lei
 */
@RestController
@RequestMapping("/signInRule")
@Slf4j
public class SignInRuleController {

    @Resource
    private SignInRuleService signInRuleService;


    /**
     * 获取签到规则列表
     * @return 签到规则列表
     */
    @PostMapping("/list")
    public ApiResponse<List<SignInRule>> listSignInRules() {
        log.info("listSignInRules");
        return ResultUtils.success(signInRuleService.list());
    }

    /*
     * 获取签到规则详情
     * @param id 签到规则ID
     * @param request HTTP请求
     * @return 签到规则详情
     */
    @PostMapping("/getRuleDetail")
    public ApiResponse<SignInRule> getSignInRuleDetail(Long id, HttpServletRequest request) {
        log.info("getSignInRuleDetail, id: {}", id);
        SignInRule signInRule = signInRuleService.getSignInRuleDetail(id,request);
        return ResultUtils.success(signInRule);
    }

    /**
     * 管理员删除签到规则（逻辑删除）
     * @param id 规则ID
     * @param request HTTP请求
     * @return 是否删除成功
     */
    @PostMapping("/admin/delete")
    public ApiResponse<Boolean> deleteSignInRuleByAdmin(@RequestParam long id, HttpServletRequest request) {
        boolean result = signInRuleService.deleteSignInRuleByAdmin(id, request);
        return ResultUtils.success(result);
    }


    /**
     * 管理员修改签到规则信息
     * @param signInRuleUpdateRequest 签到规则更新请求
     * @param request HTTP请求
     * @return 更新后的签到规则
     */
    @PostMapping("/admin/update")
    public ApiResponse<SignInRule> updateSignInRuleByAdmin(@RequestBody SignInRuleUpdateRequest signInRuleUpdateRequest, HttpServletRequest request){
        log.info("管理员修改签到规则信息：{}", signInRuleUpdateRequest);

        SignInRule signInRule = signInRuleService.updateSignInRuleByAdmin(signInRuleUpdateRequest,request);
        return ResultUtils.success(signInRule);
    }


     /**
     * 管理员新增签到规则
     * @param signInRuleAddRequest 签到规则新增请求
     * @param request HTTP请求
     * @return 新增的签到规则
     */
    @PostMapping("/admin/add")
    public ApiResponse<SignInRule> addSignInRuleByAdmin(@RequestBody SignInRuleAddRequest signInRuleAddRequest, HttpServletRequest request) {
        log.info("管理员新增签到规则：{}", signInRuleAddRequest);
        SignInRule signInRule = signInRuleService.addSignInRuleByAdmin(signInRuleAddRequest, request);
        return ResultUtils.success(signInRule);
    }


}
