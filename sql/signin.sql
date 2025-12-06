-- 创建签到记录表
CREATE TABLE `sign_in_record` (
  `id` bigint NOT NULL COMMENT '主键',
  `userId` bigint NOT NULL COMMENT '用户ID',
  `signInDate` datetime NOT NULL COMMENT '签到日期',
  `consecutiveDays` int NOT NULL DEFAULT '1' COMMENT '连续签到天数',
  `points` int NOT NULL DEFAULT '0' COMMENT '获得积分数量',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_userid_signdate` (`userId`,`signInDate`),
  KEY `idx_userid_createtime` (`userId`,`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='签到记录表';

-- 创建积分流水表
CREATE TABLE `point_transaction` (
  `id` bigint NOT NULL COMMENT '主键',
  `userId` bigint NOT NULL COMMENT '用户ID',
  `points` int NOT NULL COMMENT '积分变动数量',
  `type` tinyint NOT NULL COMMENT '积分变动类型 (1: 签到奖励, 2: 兑换商品, 3: 补签扣除等)',
  `businessId` bigint DEFAULT NULL COMMENT '关联业务ID (如签到记录ID、商品购买记录ID等)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述信息',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_userid_createtime` (`userId`,`createTime`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分流水表';

-- 创建签到规则表
CREATE TABLE `sign_in_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `consecutiveDays` int NOT NULL COMMENT '连续签到天数',
  `points` int NOT NULL COMMENT '奖励积分数量',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '规则描述',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '是否启用 (0: 禁用, 1: 启用)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_consecutivedays` (`consecutiveDays`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='签到规则表';

-- 初始化签到规则数据
INSERT INTO `sign_in_rule` (`consecutiveDays`, `points`, `description`) VALUES
(1, 10, '第一天签到奖励'),
(2, 15, '连续第二天签到奖励'),
(3, 20, '连续第三天签到奖励'),
(4, 25, '连续第四天签到奖励'),
(5, 30, '连续第五天签到奖励'),
(6, 35, '连续第六天签到奖励'),
(7, 40, '连续第七天签到奖励');

-- 更新用户表，添加连续签到天数字段
ALTER TABLE `user` 
ADD COLUMN `consecutiveSignInDays` int NOT NULL DEFAULT 0 COMMENT '连续签到天数' AFTER `points`;