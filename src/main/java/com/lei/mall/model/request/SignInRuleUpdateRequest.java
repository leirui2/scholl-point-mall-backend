package com.lei.mall.model.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 签到规则表更新请求
 * @author lei
 */
@Data
public class SignInRuleUpdateRequest implements Serializable {
    /**
     * 主键
     */
    private Long id;

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