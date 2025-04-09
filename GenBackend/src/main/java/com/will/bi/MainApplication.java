package com.will.bi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: GenBI
 * @description: 启动类
 * @author: Mr.Zhang
 * @create: 2025-04-04 12:56
 **/

@SpringBootApplication
@MapperScan("com.will.bi.mapper")
public class MainApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(MainApplication.class, args);
    }
}
