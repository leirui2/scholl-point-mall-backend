package com.lei.mall.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 签到规则表
 * @author lei
 * @TableName sign_in_rule
 */
@TableName(value ="sign_in_rule")
@Data
public class SignInRule implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 连续签到天数
     */
    @TableField(value = "consecutiveDays")
    private Integer consecutiveDays;

    /**
     * 奖励积分数量
     */
    @TableField(value = "points")
    private Integer points;

    /**
     * 规则描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    /**
     * 逻辑删除标志：0-未删除，1-已删除
     */
    @TableLogic
    @TableField(value = "isDelete")
    private Integer isDelete;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}