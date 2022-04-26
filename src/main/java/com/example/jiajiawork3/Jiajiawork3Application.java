package com.example.jiajiawork3;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.jiajiawork3.dao")
public class Jiajiawork3Application {

    public static void main(String[] args) {
        SpringApplication.run(Jiajiawork3Application.class, args);
    }

}
