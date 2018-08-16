package com.abin.lee.sharding.jdbc.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by abin on 2018/8/16.
 * http://localhost:9061/swagger-ui.html
 * http://localhost:9061/v2/api-docs
 */
@Configuration
public class SwaggerConfig {
    @Autowired
    private Environment env;

    @Bean
    public Docket buildDocket() {
        String localPort = env.getProperty("server.port");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInf())
                .host("localhost"+localPort)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.abin.lee.sharding.jdbc.controller"))//controller路径
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo buildApiInf() {
//        return new ApiInfoBuilder()
//                .title("RestAPI Docs")
//                .build();
        return new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot 测试使用 Swagger2 构建RESTful API")
                .termsOfServiceUrl("https://github.com/zondahuman/spring-boot-sharding-jdbc2")
                //创建人
                .contact(new Contact("MarryFeng", "https://github.com/zondahuman", ""))
                //版本号
                .version("1.0")
                //描述
                .description("API 描述")
                .build();
    }



}
