FROM openjdk:11-jre-slim

# 设置工作目录
WORKDIR /app

# 复制应用jar文件
COPY target/School Ponit Mall-1.0-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 8084

# 运行应用
ENTRYPOINT ["java","-jar","app.jar"]