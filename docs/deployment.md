# 部署文档

## 1. 环境要求

### 1.1 系统环境
- Windows/Linux/macOS
- Java 11 或更高版本
- MySQL 5.7 或更高版本
- Redis 3.0 或更高版本

### 1.2 软件依赖
- Maven 3.6 或更高版本
- Git（可选，用于代码管理）

## 2. 部署前准备

### 2.1 安装Java
确保系统已安装Java 11或更高版本，并配置好环境变量。

检查Java版本：
```bash
java -version
```

### 2.2 安装MySQL
安装并启动MySQL服务，创建数据库：

```sql
CREATE DATABASE usercenter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2.3 安装Redis
安装并启动Redis服务。

### 2.4 克隆项目代码
```bash
git clone <项目地址>
cd UserCenter
```

## 3. 配置文件修改

### 3.1 数据库配置
修改 `src/main/resources/application.yml` 文件中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/usercenter?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowMultiQueries=true
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 3.2 Redis配置
修改 `src/main/resources/application.yml` 文件中的Redis配置：

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    database: 1
    timeout: 2000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
        max-wait: -1ms
```

### 3.3 服务端口配置
如需修改服务端口，可以在 `application.yml` 中修改：

```yaml
server:
  port: 8083
```

## 4. 数据库初始化

执行数据库脚本 `sql/user.sql`：

```bash
mysql -u your_username -p usercenter < sql/user.sql
```

## 5. 编译打包

在项目根目录下执行Maven命令进行编译打包：

```bash
mvn clean package
```

打包完成后，会在 `target` 目录下生成jar包。

## 6. 启动服务

### 6.1 开发环境启动
在IDE中直接运行 `UserCenterApplication` 类的main方法。

或者使用Maven命令：
```bash
mvn spring-boot:run
```

### 6.2 生产环境启动
```bash
java -jar target/usercenter-*.jar
```

### 6.3 后台运行（Linux/macOS）
```bash
nohup java -jar target/usercenter-*.jar > logs/usercenter.log 2>&1 &
```

## 7. 验证部署

### 7.1 检查服务启动
查看控制台输出或日志文件，确认服务启动成功。

### 7.2 访问API文档
访问以下地址查看API文档：
```
http://localhost:8083/api/doc.html
```

### 7.3 测试接口
使用Postman或curl测试基本接口：
```bash
# 测试接口
curl http://localhost:8083/api/test
```

## 8. 管理员账户设置

首次部署后，需要手动设置管理员账户。可以通过以下SQL语句将普通用户设置为管理员：

```sql
UPDATE user SET userRole = 1 WHERE userAccount = 'your_admin_account';
```

## 9. 日志配置

默认日志配置在 `application.yml` 中，可以根据需要进行调整：

```yaml
logging:
  level:
    com.lei.usercenter: info
  file:
    name: logs/usercenter.log
```

## 10. 常见问题及解决方案

### 10.1 数据库连接失败
1. 检查数据库服务是否启动
2. 检查数据库连接配置是否正确
3. 检查数据库用户权限

### 10.2 Redis连接失败
1. 检查Redis服务是否启动
2. 检查Redis连接配置是否正确

### 10.3 端口被占用
修改 `application.yml` 中的端口配置，或停止占用端口的进程。

### 10.4 启动失败
1. 查看控制台或日志文件中的错误信息
2. 检查配置文件是否正确
3. 确保所有依赖服务正常运行

## 11. 性能优化建议

### 11.1 数据库优化
1. 为常用查询字段添加索引
2. 定期清理无用数据
3. 考虑分表分库策略

### 11.2 Redis优化
1. 合理设置过期时间
2. 监控内存使用情况
3. 考虑集群部署

### 11.3 JVM优化
根据服务器配置调整JVM参数：
```bash
java -Xms512m -Xmx1024m -jar target/usercenter-*.jar
```

## 12. 监控与维护

### 12.1 健康检查
通过以下接口检查服务健康状态：
```
GET /actuator/health
```

### 12.2 性能监控
通过以下接口查看性能指标：
```
GET /actuator/metrics
```

### 12.3 定期维护
1. 定期备份数据库
2. 清理过期日志文件
3. 监控系统资源使用情况