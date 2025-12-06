package com.lei.mall.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 签到规则表新增请求
 * @author lei
 */
@Data
public class SignInRuleAddRequest implements Serializable {


    /**
     * 连续签到天数
     */
    private Integer consecutiveDays;

    /**
     * 奖励积分数量
     */
    private Integer points;

    /**
     * 规则描述
     */
    private String description;



    private static final long serialVersionUID = 1L;
}