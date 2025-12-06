package com.lei.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lei.mall.common.ErrorCode;
import com.lei.mall.exception.BusinessException;
import com.lei.mall.model.entity.SignInRule;
import com.lei.mall.model.request.SignInRuleAddRequest;
import com.lei.mall.model.request.SignInRuleUpdateRequest;
import com.lei.mall.model.vo.UserLoginVO;
import com.lei.mall.service.SignInRuleService;
import com.lei.mall.mapper.SignInRuleMapper;
import com.lei.mall.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author lei
* @description 针对表【sign_in_rule(签到规则表)】的数据库操作Service实现
* @createDate 2025-12-05 23:00:24
*/
@Service
public class SignInRuleServiceImpl extends ServiceImpl<SignInRuleMapper, SignInRule>
    implements SignInRuleService{

    @Resource
    private UserService userService;

    /*
     * 获取签到规则详情
     * @param id 签到规则ID
     * @return 签到规则详情
     */
    @Override
    public SignInRule getSignInRuleDetail(Long id, HttpServletRequest request) {
        if (request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数为空");
        }
        UserLoginVO userLoginVO = userService.getLoginUser(request);
        if (userLoginVO == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR.getCode(), "用户未登录");
        }
        SignInRule signInRule = this.baseMapper.selectById(id);
        if (signInRule == null || signInRule.getIsDelete() == 1){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "签到规则不存在");
        }
        return signInRule;
    }


    /**
     * 管理员删除签到规则（逻辑删除）
     * @param id 规则ID
     * @param request HTTP请求
     * @return 是否删除成功
     */
    @Override
    public boolean deleteSignInRuleByAdmin(long id, HttpServletRequest request) {
        // 检查管理员权限
        checkAdmin(request);

        SignInRule signInRule = this.baseMapper.selectById(id);
        if (signInRule == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "签到规则不存在");
        }
        boolean b = this.removeById(id);
        if (!b){
            throw new BusinessException(ErrorCode.OPERATION_ERROR.getCode(), "删除签到规则失败");
        }
        return true;
    }

    /**
     * 管理员修改签到规则信息
     * @param signInRuleUpdateRequest 签到规则更新请求
     * @param request HTTP请求
     * @return 更新后的签到规则
     */
    @Override
    public SignInRule updateSignInRuleByAdmin(SignInRuleUpdateRequest signInRuleUpdateRequest, HttpServletRequest request) {
        // 检查管理员权限
        checkAdmin(request);

        SignInRule signInRule = this.baseMapper.selectById(signInRuleUpdateRequest.getId());
        if (signInRule == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR.getCode(), "签到规则不存在");
        }
        signInRule.setConsecutiveDays(signInRuleUpdateRequest.getConsecutiveDays());
        signInRule.setPoints(signInRuleUpdateRequest.getPoints());
        signInRule.setDescription(signInRuleUpdateRequest.getDescription());
        boolean result = this.baseMapper.updateById(signInRule) > 0;
        if (!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR.getCode(), "更新签到规则失败");
        }
        return signInRule;
    }

    /**
     * 管理员新增签到规则
     * @param signInRuleAddRequest 签到规则新增请求
     * @param request HTTP请求
     * @return 新增的签到规则
     */
    @Override
    public SignInRule addSignInRuleByAdmin(SignInRuleAddRequest signInRuleAddRequest, HttpServletRequest request) {
        // 检查管理员权限
        checkAdmin(request);

        SignInRule signInRule = new SignInRule();
        signInRule.setConsecutiveDays(signInRuleAddRequest.getConsecutiveDays());
        signInRule.setPoints(signInRuleAddRequest.getPoints());
        signInRule.setDescription(signInRuleAddRequest.getDescription());
        boolean result = this.save(signInRule) ;
        if (!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR.getCode(), "新增签到规则失败");
        }
        return signInRule;
    }

    /**
     * 检查用户是否为管理员
     * @param request HTTP请求
     */
    private void checkAdmin(HttpServletRequest request) {
        if (request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数为空");
        }
        UserLoginVO userLoginVO = userService.getLoginUser(request);
        if (userLoginVO.getUserRole() == null || userLoginVO.getUserRole() != 1){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR.getCode(), "用户无权限");
        }
    }
}




