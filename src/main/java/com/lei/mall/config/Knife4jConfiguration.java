package com.lei.mall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author lei
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "defaultApi")
    public Docket defaultApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("学校积分兑换商城 APIs")
                        .description("学校积分兑换商城项目 APIs")
                        .termsOfServiceUrl("http://www.xx.com/")
                        .contact(new Contact("leirui","",""))
                        .version("1.0")
                        .build())
                .groupName("default")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lei.mall.controller"))
                .paths(PathSelectors.any())
                .build();
    }

}