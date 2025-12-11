# School Point Mall 学校积分商城系统

这是一个基于Spring Boot构建的学校周边积分商城系统，学生可以通过签到等方式获取积分，并使用积分兑换商品。系统提供了完整的用户管理、商品管理、积分管理等功能。

## 功能特性

### 1. 用户管理
- 用户注册
- 用户登录/登出
- 用户信息修改
- 用户密码修改
- 用户状态管理
- 用户列表分页查询
- 用户角色管理（普通用户、管理员）
- 用户逻辑删除

### 2. 积分系统
- 每日签到获取积分
- 连续签到额外奖励
- 积分兑换商品
- 积分流水记录查询

### 3. 商品管理
- 商品分类管理
- 商品信息管理
- 商品上下架管理
- 商品库存管理

### 4. 订单系统
- 积分兑换订单管理
- 订单状态跟踪
- 订单历史查询

### 5. 支付系统
- 纯积分支付
- 纯支付宝支付
- 混合支付（积分+支付宝）
  - 支持自定义积分与现金比例（当前默认对半分，1积分=0.1元）
  - 支付宝当面付二维码生成
  - 支付异步通知处理
  - 支付状态查询

### 6. 权限控制
- 普通用户权限
- 管理员权限

### 7. 数据安全
- 密码加密存储
- Session管理
- Redis缓存支持

### 8. 文件存储
- 基于MinIO的文件存储
- 商品图片上传管理

### 9. 接口文档
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
- MinIO (对象存储)
- 支付宝EasySDK (支付)
- ZXing (二维码生成)

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
│   │       │   ├── request    # 请求模型
│   │       │   └── vo         # 视图模型
│   │       ├── service        # 服务层
│   │       │   └── impl       # 服务实现
│   │       └── utils          # 工具类
│   │       └── mallApplication.java  # 启动类
│   └── resources
│       ├── com.lei.mall.mapper  # MyBatis XML映射文件
│       └── application.yml      # 配置文件
└── test                         # 测试代码
    └── java.com.lei.mall
```

## 核心接口

### 用户相关
#### 用户注册
- **接口地址**: `POST /user/register`
- **请求参数**: 
  ```json
  {
    "userAccount": "账号",
    "userPassword": "密码",
    "checkPassword": "确认密码"
  }
  ```

#### 用户登录
- **接口地址**: `POST /user/login`
- **请求参数**: 
  ```json
  {
    "userAccount": "账号",
    "userPassword": "密码"
  }
  ```

#### 获取当前登录用户
- **接口地址**: `GET /user/getLoginUser`
- **请求方法**: GET

#### 退出登录
- **接口地址**: `POST /user/logout`

#### 修改用户信息
- **接口地址**: `POST /user/update`

#### 修改用户密码
- **接口地址**: `POST /user/updateUserPassword`

#### 管理员相关用户管理接口
- `GET /user/admin/getUserById` - 管理员根据ID查询用户完整信息
- `POST /user/admin/listUsersByPage` - 管理员分页查询用户列表
- `POST /user/admin/updateUserStatus` - 管理员更新用户状态
- `POST /user/admin/resetPassword` - 管理员重置用户密码
- `POST /user/admin/updateUserRole` - 管理员更新用户角色
- `POST /user/admin/deleteUser` - 管理员逻辑删除用户
- `POST /user/admin/update` - 管理员修改用户信息

### 签到相关
#### 用户签到
- **接口地址**: `POST /signInRecord/signIn`
- **说明**: 用户每日签到获取积分

#### 查询签到记录
- **接口地址**: `POST /signInRecord/listSignInRecordByPage` - 分页查询签到记录
- **接口地址**: `POST /signInRecord/listSignInMyRecordByPage` - 分页查询自己的签到记录
- **接口地址**: `POST /signInRecord/isSignIn` - 判断当前用户是否已签到

### 签到规则相关
#### 获取签到规则
- **接口地址**: `POST /signInRule/list` - 获取签到规则列表

#### 管理员签到规则管理
- `POST /signInRule/getRuleDetail` - 获取签到规则详情
- `POST /signInRule/admin/delete` - 管理员删除签到规则
- `POST /signInRule/admin/update` - 管理员修改签到规则
- `POST /signInRule/admin/add` - 管理员新增签到规则

### 商品分类相关
#### 商品分类管理
- `POST /category/add` - 添加商品类别
- `POST /category/update` - 修改商品类别信息
- `GET /category/getCategoryById` - 根据ID查询商品类别完整信息
- `POST /category/deleteCategory` - 管理员逻辑删除商品类别
- `POST /category/listCategoryByPage` - 分页查询商品类别列表
- `POST /category/hotCategoryByPage` - 热门商品类别列表

### 商品相关
#### 商品管理
- `POST /item/add` - 添加商品信息
- `POST /item/update` - 修改商品信息
- `GET /item/getItemById` - 根据ID查询商品脱敏信息
- `POST /item/updateItemStatus` - 管理员更新商品状态（启用/禁用）
- `POST /item/deleteItem` - 管理员逻辑删除商品信息
- `POST /item/listItemByPageUser` - 普通用户分页查询商品信息脱敏列表
- `POST /item/hotListItemByPage` - 获取热门商品列表推荐
- `POST /item/listItemByPage` - 管理员分页查询商品信息列表
- `POST /item/listItemCategoryByPage` - 管理员分页查询商品和类别信息列表

### 订单相关
#### 下单兑换商品
- **接口地址**: `POST /purchase_record/addPurchaseItem`
- **请求参数**:
  ```json
  {
    "itemId": "商品ID",
    "num": "兑换数量",
    "paymentType": "支付类型（1=积分支付，2=支付宝支付，3=混合支付）"
  }
  ```

#### 查询订单记录
- **接口地址**: `POST /purchase_record/listAllRecord`
- **说明**: 分页查询当前用户的所有下单记录

### 支付相关
#### 发起支付
- **接口地址**: `POST /pay`
- **请求参数**: 
  ```
  itemId: 商品ID
  num: 数量
  paymentType: 支付类型（2=支付宝支付，3=混合支付）
  ```
- **说明**: 
  - 当paymentType=2时，创建纯支付宝支付订单
  - 当paymentType=3时，创建混合支付订单并生成支付宝支付二维码

#### 支付宝回调通知
- **接口地址**: `POST /notify`
- **说明**: 支付宝异步通知接口

#### 查询支付状态
- **接口地址**: `POST /query`
- **请求参数**: outTradeNo（商户订单号）
- **说明**: 查询支付状态

## 权限设计

项目中用户分为两种角色：
1. 普通用户 (userRole = 0)
2. 管理员 (userRole = 1)

不同角色拥有不同的接口访问权限。

## 数据脱敏

为了保护用户隐私，项目对敏感信息进行了脱敏处理。普通用户只能访问脱敏后的信息。

## 支付说明

系统支持三种支付方式：
1. **纯积分支付**：完全使用积分兑换商品
2. **纯支付宝支付**：完全使用支付宝付款兑换商品
3. **混合支付**：同时使用积分和支付宝付款兑换商品
   - 当前默认对半分（可根据需求调整）
   - 比例：1积分 = 0.1元

## 配置说明

项目配置文件位于 `src/main/resources/application.yml`，需要根据实际情况修改以下配置：

- 数据库连接信息
- Redis连接信息
- MinIO文件存储配置
- 支付宝支付配置

## 部署说明

1. 创建数据库并执行初始化脚本
2. 修改 `application.yml` 中的数据库、Redis、MinIO和支付宝配置
3. 编译打包：`mvn clean package`
4. 运行：`java -jar target/school-point-mall-*.jar`

## 注意事项

1. 项目使用Redis作为缓存，确保Redis服务正常运行
2. 数据库使用MySQL，确保数据库服务正常运行
3. 管理员账户需要手动设置userRole字段为1
4. 项目使用MinIO作为文件存储服务，确保MinIO服务正常运行
5. 如需使用支付宝支付功能，需要配置支付宝相关参数