# API接口文档

## 1. 公共返回结果说明

所有接口均返回统一格式的JSON数据：

```json
{
  "code": 0,
  "message": "ok",
  "data": {}
}
```

字段说明：
- code: 状态码，0表示成功，其他值表示失败
- message: 返回信息
- data: 返回数据

### 1.1 状态码说明

| 状态码 | 说明 |
|-------|------|
| 0 | 成功 |
| 40000 | 请求参数错误 |
| 40100 | 未登录 |
| 40101 | 无权限 |
| 40400 | 请求数据不存在 |
| 50000 | 系统内部异常 |
| 50001 | 操作失败 |

## 2. 用户接口

### 2.1 用户注册

**接口地址**: `POST /api/user/register`

**接口描述**: 用户注册接口

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userAccount | string | 是 | 用户账号，长度不少于6位 |
| userPassword | string | 是 | 用户密码，长度不少于8位 |
| checkPassword | string | 是 | 确认密码，需与userPassword一致 |

**请求示例**:
```json
{
  "userAccount": "testuser",
  "userPassword": "12345678",
  "checkPassword": "12345678"
}
```

**返回结果**:

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 状态码 |
| message | string | 返回信息 |
| data | long | 用户ID |

**返回示例**:
```json
{
  "code": 0,
  "message": "ok",
  "data": 123456789
}
```

### 2.2 用户登录

**接口地址**: `POST /api/user/login`

**接口描述**: 用户登录接口

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userAccount | string | 是 | 用户账号 |
| userPassword | string | 是 | 用户密码 |

**请求示例**:
```json
{
  "userAccount": "testuser",
  "userPassword": "12345678"
}
```

**返回结果**:

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 状态码 |
| message | string | 返回信息 |
| data | object | 用户登录信息对象 |

**data对象字段**:

| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | long | 用户ID |
| userAccount | string | 用户账号 |
| userName | string | 用户昵称 |
| email | string | 邮箱 |
| gender | int | 性别 |
| phone | string | 电话 |
| userAvatar | string | 用户头像 |
| userProfile | string | 用户简介 |
| userStatus | int | 用户状态 |
| createTime | string | 创建时间 |
| updateTime | string | 更新时间 |

**返回示例**:
```json
{
  "code": 0,
  "message": "ok",
  "data": {
    "id": 123456789,
    "userAccount": "testuser",
    "userName": "testuser",
    "email": null,
    "gender": null,
    "phone": null,
    "userAvatar": null,
    "userProfile": null,
    "userStatus": 0,
    "createTime": "2023-01-01T00:00:00.000+00:00",
    "updateTime": "2023-01-01T00:00:00.000+00:00"
  }
}
```

### 2.3 获取当前登录用户

**接口地址**: `GET /api/user/getLoginUser`

**接口描述**: 获取当前登录用户信息

**请求参数**: 无

**返回结果**:

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 状态码 |
| message | string | 返回信息 |
| data | object | 用户登录信息对象 |

**data对象字段**同用户登录接口返回的data字段

**返回示例**:
```json
{
  "code": 0,
  "message": "ok",
  "data": {
    "id": 123456789,
    "userAccount": "testuser",
    "userName": "testuser",
    "email": null,
    "gender": null,
    "phone": null,
    "userAvatar": null,
    "userProfile": null,
    "userStatus": 0,
    "createTime": "2023-01-01T00:00:00.000+00:00",
    "updateTime": "2023-01-01T00:00:00.000+00:00"
  }
}
```

### 2.4 退出登录

**接口地址**: `POST /api/user/logout`

**接口描述**: 用户退出登录

**请求参数**: 无

**返回结果**:

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 状态码 |
| message | string | 返回信息 |
| data | boolean | 退出结果 |

**返回示例**:
```json
{
  "code": 0,
  "message": "ok",
  "data": true
}
```

### 2.5 修改用户信息

**接口地址**: `POST /api/user/update`

**接口描述**: 修改用户信息

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 用户ID |
| userAccount | string | 是 | 用户账号 |
| userPassword | string | 否 | 用户密码 |
| userName | string | 否 | 用户昵称 |
| email | string | 否 | 邮箱 |
| gender | int | 否 | 性别 |
| phone | string | 否 | 电话 |
| userAvatar | string | 否 | 用户头像 |
| userProfile | string | 否 | 用户简介 |
| userStatus | int | 否 | 用户状态 |

**请求示例**:
```json
{
  "id": 123456789,
  "userAccount": "testuser",
  "userName": "测试用户",
  "email": "test@example.com"
}
```

**返回结果**:

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 状态码 |
| message | string | 返回信息 |
| data | object | 用户更新信息对象 |

**data对象字段**:

| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | long | 用户ID |
| userAccount | string | 用户账号 |
| userName | string | 用户昵称 |
| email | string | 邮箱 |
| gender | int | 性别 |
| phone | string | 电话 |
| userAvatar | string | 用户头像 |
| userProfile | string | 用户简介 |
| userStatus | int | 用户状态 |

**返回示例**:
```json
{
  "code": 0,
  "message": "ok",
  "data": {
    "id": 123456789,
    "userAccount": "testuser",
    "userName": "测试用户",
    "email": "test@example.com",
    "gender": null,
    "phone": null,
    "userAvatar": null,
    "userProfile": null,
    "userStatus": 0
  }
}
```

### 2.6 管理员查询用户完整信息

**接口地址**: `GET /api/user/admin/getUserById`

**接口描述**: 管理员根据ID查询用户完整信息

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 用户ID |

**返回结果**:

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 状态码 |
| message | string | 返回信息 |
| data | object | 用户完整信息对象 |

**data对象字段**:

| 参数名 | 类型 | 说明 |
|--------|------|------|
| id | long | 用户ID |
| userAccount | string | 用户账号 |
| userPassword | string | 用户密码（加密后） |
| userName | string | 用户昵称 |
| email | string | 邮箱 |
| gender | int | 性别 |
| phone | string | 电话 |
| userAvatar | string | 用户头像 |
| userProfile | string | 用户简介 |
| userStatus | int | 用户状态 |
| userRole | int | 用户角色 |
| createTime | string | 创建时间 |
| updateTime | string | 更新时间 |
| isDelete | int | 是否删除 |

**返回示例**:
```json
{
  "code": 0,
  "message": "ok",
  "data": {
    "id": 123456789,
    "userAccount": "testuser",
    "userPassword": "加密后的密码",
    "userName": "测试用户",
    "email": "test@example.com",
    "gender": 0,
    "phone": "13800138000",
    "userAvatar": "http://example.com/avatar.jpg",
    "userProfile": "用户简介",
    "userStatus": 0,
    "userRole": 0,
    "createTime": "2023-01-01T00:00:00.000+00:00",
    "updateTime": "2023-01-01T00:00:00.000+00:00",
    "isDelete": 0
  }
}
```

### 2.7 普通用户查询用户公开信息

**接口地址**: `GET /api/user/getPublicUserInfoById`

**接口描述**: 普通用户根据ID查询用户公开信息

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 用户ID |

**返回结果**:

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 状态码 |
| message | string | 返回信息 |
| data | object | 用户公开信息对象 |

**data对象字段**同获取当前登录用户接口返回的data字段

**返回示例**:
```json
{
  "code": 0,
  "message": "ok",
  "data": {
    "id": 123456789,
    "userAccount": "testuser",
    "userName": "测试用户",
    "email": "test@example.com",
    "gender": 0,
    "phone": "13800138000",
    "userAvatar": "http://example.com/avatar.jpg",
    "userProfile": "用户简介",
    "userStatus": 0,
    "createTime": "2023-01-01T00:00:00.000+00:00",
    "updateTime": "2023-01-01T00:00:00.000+00:00"
  }
}
```

### 2.8 管理员分页查询用户列表

**接口地址**: `POST /api/user/admin/listUsersByPage`

**接口描述**: 管理员分页查询用户列表

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| current | int | 否 | 当前页号，默认1 |
| pageSize | int | 否 | 页面大小，默认10 |
| userAccount | string | 否 | 用户账号（模糊查询） |
| userName | string | 否 | 用户昵称（模糊查询） |
| gender | int | 否 | 性别 |
| userStatus | int | 否 | 用户状态 |
| userRole | int | 否 | 用户角色 |

**请求示例**:
```json
{
  "current": 1,
  "pageSize": 10,
  "userAccount": "test",
  "userName": "测试"
}
```

**返回结果**:

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 状态码 |
| message | string | 返回信息 |
| data | object | 分页结果对象 |

**data对象字段**:

| 参数名 | 类型 | 说明 |
|--------|------|------|
| records | array | 用户列表 |
| total | long | 总数 |
| current | long | 当前页号 |
| size | long | 页面大小 |

**records数组元素字段**同管理员查询用户完整信息接口返回的data字段

**返回示例**:
```json
{
  "code": 0,
  "message": "ok",
  "data": {
    "records": [
      {
        "id": 123456789,
        "userAccount": "testuser",
        "userPassword": "加密后的密码",
        "userName": "测试用户",
        "email": "test@example.com",
        "gender": 0,
        "phone": "13800138000",
        "userAvatar": "http://example.com/avatar.jpg",
        "userProfile": "用户简介",
        "userStatus": 0,
        "userRole": 0,
        "createTime": "2023-01-01T00:00:00.000+00:00",
        "updateTime": "2023-01-01T00:00:00.000+00:00",
        "isDelete": 0
      }
    ],
    "total": 1,
    "current": 1,
    "size": 10
  }
}
```

### 2.9 管理员更新用户状态

**接口地址**: `POST /api/user/admin/updateUserStatus`

**接口描述**: 管理员更新用户状态（启用/禁用）

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 用户ID |
| status | int | 是 | 用户状态，0-正常，1-禁用 |

**返回结果**:

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 状态码 |
| message | string | 返回信息 |
| data | boolean | 更新结果 |

**返回示例**:
```json
{
  "code": 0,
  "message": "ok",
  "data": true
}
```

### 2.10 管理员更新用户角色

**接口地址**: `POST /api/user/admin/updateUserRole`

**接口描述**: 管理员更新用户角色

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 用户ID |
| role | int | 是 | 用户角色，0-普通用户，1-管理员 |

**返回结果**:

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 状态码 |
| message | string | 返回信息 |
| data | boolean | 更新结果 |

**返回示例**:
```json
{
  "code": 0,
  "message": "ok",
  "data": true
}
```

### 2.11 管理员逻辑删除用户

**接口地址**: `POST /api/user/admin/deleteUser`

**接口描述**: 管理员逻辑删除用户

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 用户ID |

**返回结果**:

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 状态码 |
| message | string | 返回信息 |
| data | boolean | 删除结果 |

**返回示例**:
```json
{
  "code": 0,
  "message": "ok",
  "data": true
}
```