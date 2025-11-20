# 数据库设计文档

## 1. 概述

本文档详细描述了用户中心项目的数据库表结构设计，包括各字段的含义、类型、约束等信息。

## 2. 数据库表结构

### 2.1 用户表 (user)

用户表是系统的核心数据表，用于存储用户的基本信息、状态、权限等。

#### 表结构

```sql
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `userPassword` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `userName` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
  `email` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `gender` tinyint(2) DEFAULT NULL COMMENT '性别',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `userAvatar` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户头像',
  `userProfile` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户简介',
  `userStatus` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户状态： 0 - 正常 1-ban',
  `userRole` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户角色: 0 - 普通用户 1 - 管理员',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_account` (`userAccount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';
```

#### 字段说明

| 字段名 | 类型 | 允许为空 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | bigint(20) | 否 | 无 | 主键，自增ID |
| userAccount | varchar(256) | 否 | 无 | 用户账号，唯一 |
| userPassword | varchar(512) | 否 | 无 | 用户密码（加密存储） |
| userName | varchar(256) | 是 | NULL | 用户昵称 |
| email | varchar(30) | 是 | NULL | 用户邮箱 |
| gender | tinyint(2) | 是 | NULL | 用户性别，0-女，1-男，NULL-未设置 |
| phone | varchar(20) | 是 | NULL | 用户电话 |
| userAvatar | varchar(1024) | 是 | NULL | 用户头像URL |
| userProfile | varchar(512) | 是 | NULL | 用户简介 |
| userStatus | tinyint(4) | 否 | 0 | 用户状态，0-正常，1-禁用 |
| userRole | tinyint(4) | 否 | 0 | 用户角色，0-普通用户，1-管理员 |
| createTime | datetime | 否 | CURRENT_TIMESTAMP | 创建时间 |
| updateTime | datetime | 否 | CURRENT_TIMESTAMP | 更新时间 |
| isDelete | tinyint(4) | 否 | 0 | 逻辑删除标识，0-未删除，1-已删除 |

#### 索引说明

| 索引名 | 类型 | 字段 | 说明 |
|--------|------|------|------|
| PRIMARY | 主键索引 | id | 主键 |
| idx_user_account | 唯一索引 | userAccount | 用户账号唯一索引 |

### 2.2 登录日志表 (login_log)

用于记录用户登录信息，包括登录时间、IP地址等。

#### 表结构

```sql
CREATE TABLE `login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `ip` varchar(50) DEFAULT NULL COMMENT '登录IP',
  `loginTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `userAgent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '登录状态 0-成功 1-失败',
  `errorMsg` varchar(500) DEFAULT NULL COMMENT '错误信息',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`userId`),
  KEY `idx_login_time` (`loginTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';
```

#### 字段说明

| 字段名 | 类型 | 允许为空 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | bigint(20) | 否 | 无 | 主键，自增ID |
| userId | bigint(20) | 否 | 无 | 用户ID |
| ip | varchar(50) | 是 | NULL | 登录IP地址 |
| loginTime | datetime | 否 | CURRENT_TIMESTAMP | 登录时间 |
| userAgent | varchar(500) | 是 | NULL | 用户代理信息 |
| status | tinyint(4) | 否 | 0 | 登录状态，0-成功，1-失败 |
| errorMsg | varchar(500) | 是 | NULL | 错误信息 |
| createTime | datetime | 否 | CURRENT_TIMESTAMP | 创建时间 |

#### 索引说明

| 索引名 | 类型 | 字段 | 说明 |
|--------|------|------|------|
| PRIMARY | 主键索引 | id | 主键 |
| idx_user_id | 普通索引 | userId | 用户ID索引 |
| idx_login_time | 普通索引 | loginTime | 登录时间索引 |

### 2.3 操作日志表 (operation_log)

用于记录用户的关键操作信息。

#### 表结构

``sql
CREATE TABLE `operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `operation` varchar(100) NOT NULL COMMENT '操作类型',
  `method` varchar(100) NOT NULL COMMENT '请求方法',
  `uri` varchar(200) NOT NULL COMMENT '请求URI',
  `ip` varchar(50) DEFAULT NULL COMMENT '操作IP',
  `userAgent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `requestParams` text COMMENT '请求参数',
  `responseResult` text COMMENT '响应结果',
  `operationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `costTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '耗时(毫秒)',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态 0-成功 1-失败',
  `errorMsg` varchar(500) DEFAULT NULL COMMENT '错误信息',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`userId`),
  KEY `idx_operation_time` (`operationTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
```

#### 字段说明

| 字段名 | 类型 | 允许为空 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | bigint(20) | 否 | 无 | 主键，自增ID |
| userId | bigint(20) | 否 | 无 | 用户ID |
| operation | varchar(100) | 否 | 无 | 操作类型 |
| method | varchar(100) | 否 | 无 | 请求方法 |
| uri | varchar(200) | 否 | 无 | 请求URI |
| ip | varchar(50) | 是 | NULL | 操作IP地址 |
| userAgent | varchar(500) | 是 | NULL | 用户代理信息 |
| requestParams | text | 是 | NULL | 请求参数 |
| responseResult | text | 是 | NULL | 响应结果 |
| operationTime | datetime | 否 | CURRENT_TIMESTAMP | 操作时间 |
| costTime | bigint(20) | 否 | 0 | 操作耗时(毫秒) |
| status | tinyint(4) | 否 | 0 | 状态 0-成功 1-失败 |
| errorMsg | varchar(500) | 是 | NULL | 错误信息 |
| createTime | datetime | 否 | CURRENT_TIMESTAMP | 创建时间 |

#### 索引说明

| 索引名 | 类型 | 字段 | 说明 |
|--------|------|------|------|
| PRIMARY | 主键索引 | id | 主键 |
| idx_user_id | 普通索引 | userId | 用户ID索引 |
| idx_operation_time | 普通索引 | operationTime | 操作时间索引 |

### 2.4 用户活跃度表 (user_activity)

用于统计用户的活跃情况。

#### 表结构

```sql
CREATE TABLE `user_activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `loginCount` int(11) NOT NULL DEFAULT '0' COMMENT '登录次数',
  `lastLoginTime` datetime DEFAULT NULL COMMENT '最后登录时间',
  `lastActiveTime` datetime DEFAULT NULL COMMENT '最后活跃时间',
  `activityScore` int(11) NOT NULL DEFAULT '0' COMMENT '活跃度分数',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户活跃度表';
```

#### 字段说明

| 字段名 | 类型 | 允许为空 | 默认值 | 说明 |
|--------|------|----------|--------|------|
| id | bigint(20) | 否 | 无 | 主键，自增ID |
| userId | bigint(20) | 否 | 无 | 用户ID |
| loginCount | int(11) | 否 | 0 | 登录次数 |
| lastLoginTime | datetime | 是 | NULL | 最后登录时间 |
| lastActiveTime | datetime | 是 | NULL | 最后活跃时间 |
| activityScore | int(11) | 否 | 0 | 活跃度分数 |
| createTime | datetime | 否 | CURRENT_TIMESTAMP | 创建时间 |
| updateTime | datetime | 否 | CURRENT_TIMESTAMP | 更新时间 |

#### 索引说明

| 索引名 | 类型 | 字段 | 说明 |
|--------|------|------|------|
| PRIMARY | 主键索引 | id | 主键 |
| uk_user_id | 唯一索引 | userId | 用户ID唯一索引 |

## 3. 数据字典

### 3.1 性别字段 (gender)

| 值 | 含义 |
|----|------|
| 0 | 女 |
| 1 | 男 |
| NULL | 未设置 |

### 3.2 用户状态字段 (userStatus)

| 值 | 含义 |
|----|------|
| 0 | 正常 |
| 1 | 禁用 |

### 3.3 用户角色字段 (userRole)

| 值 | 含义 |
|----|------|
| 0 | 普通用户 |
| 1 | 管理员 |

### 3.4 逻辑删除字段 (isDelete)

| 值 | 含义 |
|----|------|
| 0 | 未删除 |
| 1 | 已删除 |

### 3.5 登录状态字段 (status)

| 值 | 含义 |
|----|------|
| 0 | 成功 |
| 1 | 失败 |

## 4. 示例数据

```sql
-- 普通用户示例
INSERT INTO `user` (`userAccount`, `userPassword`, `userName`, `email`, `gender`, `phone`, `userAvatar`, `userProfile`, `userStatus`, `userRole`) 
VALUES ('testuser', '加密后的密码', '测试用户', 'test@example.com', 1, '13800138000', 'http://example.com/avatar.jpg', '这是测试用户', 0, 0);

-- 管理员用户示例
INSERT INTO `user` (`userAccount`, `userPassword`, `userName`, `email`, `gender`, `phone`, `userAvatar`, `userProfile`, `userStatus`, `userRole`) 
VALUES ('admin', '加密后的密码', '管理员', 'admin@example.com', 1, '13900139000', 'http://example.com/admin.jpg', '系统管理员', 0, 1);
```

## 5. 注意事项

1. 密码字段存储的是经过MD5加盐加密后的密码，盐值在代码中定义
2. 用户账号具有唯一性约束，不能重复
3. 所有涉及用户信息的删除操作均为逻辑删除，通过更新isDelete字段实现
4. createTime和updateTime字段由数据库自动维护
5. 用户状态和角色字段的值应严格按照数据字典定义使用