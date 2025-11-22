# mall 用户中心模板项目

这是一个可复用的后端用户中心模板项目，基于Spring Boot构建，提供了完整的用户管理功能，包括用户注册、登录、权限控制等。你可以将此项目作为模板，快速构建其他需要用户系统的项目。

## 功能特性

### 1. 用户管理
- 用户注册
- 用户登录/登出
- 用户信息修改
- 用户密码修改
- 用户状态管理
- 用户列表分页查询
- 用户角色管理
- 用户逻辑删除

### 2. 权限控制
- 普通用户权限
- 管理员权限

### 3. 数据安全
- 密码加密存储
- Session管理
- Redis缓存支持

### 4. 接口文档
- 集成Knife4j Swagger文档
  Knife4j UI: http://localhost:8084/api/doc.html
  API 文档: http://localhost:8084/api/v2/api-docs

## 技术栈
- Spring Boot 2.x
- MyBatis-Plus
- Redis (缓存)
- MySQL (数据库)
- Lombok (简化代码)
- Knife4j (API文档)
- X-file-storage (文件存储)

## 项目结构

```
src
├── main
│   ├── java
│   │   └── com.lei.mall
│   │       ├── aspect         # 切面类
│   │       ├── common         # 通用类（返回结果、错误码等）
│   │       ├── config         # 配置类
│   │       ├── constant       # 常量定义
│   │       ├── controller     # 控制器层
│   │       ├── exception      # 异常处理
│   │       ├── mapper         # 数据访问层
│   │       ├── model          # 数据模型
│   │       │   ├── entity     # 实体类
│   │       │   ├── user       # 用户相关请求/响应模型
│   │       │   └── vo         # 视图模型
│   │       ├── service        # 服务层
│   │       │   └── impl       # 服务实现
│   │       └── utils          # 工具类
│   │       └── mallApplication.java  # 启动类
│   └── resources
│       ├── com.lei.mall.mapper  # MyBatis XML映射文件
│       └── application.yml            # 配置文件
└── test                           # 测试代码
    └── java.com.lei.mall
```

## 核心接口

### 用户注册
- **接口地址**: `POST /user/register`
- **请求参数**: 
  ```json
  {
    "userAccount": "账号",
    "userPassword": "密码",
    "checkPassword": "确认密码"
  }
  ```

### 用户登录
- **接口地址**: `POST /user/login`
- **请求参数**: 
  ```json
  {
    "userAccount": "账号",
    "userPassword": "密码"
  }
  ```

### 获取当前登录用户
- **接口地址**: `GET /user/getLoginUser`
- **请求方法**: GET

### 退出登录
- **接口地址**: `POST /user/logout`

### 修改用户信息
- **接口地址**: `POST /user/update`

### 修改用户密码
- **接口地址**: `POST /user/updateUserPassword`

### 管理员根据ID查询用户完整信息
- **接口地址**: `GET /user/admin/getUserById`
- **权限要求**: 管理员
- **说明**: 管理员可以查看任何用户的完整信息

### 根据ID查询用户公开信息
- **接口地址**: `GET /user/getPublicUserInfoById`
- **权限要求**: 登录用户
- **说明**: 用户可以查看其他用户的脱敏公开信息

### 管理员分页查询用户列表
- **接口地址**: `POST /user/admin/listUsersByPage`
- **权限要求**: 管理员
- **说明**: 支持按账号名、昵称、性别等条件筛选

### 管理员更新用户状态
- **接口地址**: `POST /user/admin/updateUserStatus`
- **权限要求**: 管理员
- **说明**: 启用或禁用用户账号

### 管理员重置用户密码
- **接口地址**: `POST /user/admin/resetPassword`
- **权限要求**: 管理员

### 管理员更新用户角色
- **接口地址**: `POST /user/admin/updateUserRole`
- **权限要求**: 管理员
- **说明**: 设置用户为普通用户或管理员

### 管理员逻辑删除用户
- **接口地址**: `POST /user/admin/deleteUser`
- **权限要求**: 管理员
- **说明**: 逻辑删除用户（标记为已删除）

### 管理员修改用户信息
- **接口地址**: `POST /user/admin/update`
- **权限要求**: 管理员

## 权限设计

项目中用户分为两种角色：
1. 普通用户 (userRole = 0)
2. 管理员 (userRole = 1)

不同角色拥有不同的接口访问权限。

## 数据脱敏

为了保护用户隐私，项目对敏感信息进行了脱敏处理。普通用户只能访问脱敏后的用户信息。

## 文档说明

项目包含以下详细文档：

1. [API接口文档](docs/api.md) - 详细说明每个接口的参数和返回值
2. [数据库设计文档](docs/database.md) - 详细说明数据表结构和字段含义
3. [部署文档](docs/deployment.md) - 详细说明项目部署步骤和注意事项

## 配置说明

项目配置文件位于 `src/main/resources/application.yml`，需要根据实际情况修改以下配置：

- 数据库连接信息
- Redis连接信息
- MinIO文件存储配置

## 部署说明

1. 创建数据库并执行 `sql/user.sql` 脚本
2. 修改 `application.yml` 中的数据库和Redis配置
3. 编译打包：`mvn clean package`
4. 运行：`java -jar target/mall-*.jar`

## 使用模板

你可以将此项目作为模板来创建新的项目：

1. 克隆或下载此项目
2. 修改包名和项目配置
3. 根据需要添加或修改功能
4. 构建和部署

## 注意事项

1. 项目使用Redis作为缓存，确保Redis服务正常运行
2. 数据库使用MySQL，确保数据库服务正常运行
3. 管理员账户需要手动设置userRole字段为1
4. 项目使用MinIO作为文件存储服务，确保MinIO服务正常运行