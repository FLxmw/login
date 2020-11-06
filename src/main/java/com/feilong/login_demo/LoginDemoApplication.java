package com.feilong.login_demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SuppressWarnings("ALL")
@SpringBootApplication(scanBasePackages = "com.feilong")
@MapperScan(basePackages = "com.feilong.dao")
public class LoginDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginDemoApplication.class, args);
    }

}
