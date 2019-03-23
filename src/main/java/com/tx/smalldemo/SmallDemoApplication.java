package com.tx.smalldemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(value = "com.tx.smalldemo.miaosha.dao")
public class SmallDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmallDemoApplication.class, args);
    }

}
