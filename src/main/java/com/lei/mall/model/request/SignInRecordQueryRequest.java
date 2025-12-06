package com.lei.mall.model.request;

import com.lei.mall.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 签到记录表
 * @author lei
 * @TableName sign_in_record
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SignInRecordQueryRequest extends PageRequest implements Serializable {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 签到日期
     */

    private Date signInDate;

    /**
     * 连续签到天数
     */
    private Integer consecutiveDays;

    /**
     * 获得积分数量
     */
    private Integer points;


    private static final long serialVersionUID = 1L;
}