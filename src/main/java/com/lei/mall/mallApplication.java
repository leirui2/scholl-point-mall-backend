package com.lei.mall;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author lei
 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan("com.lei.mall.mapper")
@EnableFileStorage
@EnableAspectJAutoProxy
//开启事务
@EnableTransactionManagement
public class mallApplication {

    public static void main(String[] args) {
        SpringApplication.run(mallApplication.class, args);
    }

}