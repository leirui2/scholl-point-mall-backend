package com.lei.mall.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 交易 积分 金额 流水表
 * @author lei
 * @TableName point_transaction
 */
@TableName(value ="transaction_record")
@Data
public class TransactionRecord implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户ID
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 支付方式（1：积分支付，2：支付宝支付）
     */
    @TableField(value = "payType")
    private Integer payType;

    /**
     * money 交易金额
     */
    @TableField(value = "money")
    private String money;

    /**
     * 积分变动数量
     */
    @TableField(value = "points")
    private Integer points;

    /**
     * 积分变动类型 (1: 签到奖励, 2: 兑换商品, 3: 补签扣除等)
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 关联业务ID (如签到记录ID、商品购买记录ID等)
     */
    @TableField(value = "businessId")
    private Long businessId;

    /**
     * 描述信息
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
     * 是否删除
     */
    @TableField(value = "isDelete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}