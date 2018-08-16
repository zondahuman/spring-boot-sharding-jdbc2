package com.abin.lee.sharding.jdbc.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@SpringBootApplication
@ComponentScan("com.abin.lee.*.**")
@MapperScan("com.abin.lee.sharding.jdbc.dao")
@EnableAutoConfiguration(exclude = {MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableTransactionManagement(proxyTargetClass = true)   //开启事物管理功能
public class ShardingJdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcApplication.class, args);
    }

}
