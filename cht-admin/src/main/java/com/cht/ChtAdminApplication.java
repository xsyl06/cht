package com.cht;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = {"com.cht.**.mapper"})
@ServletComponentScan
@EnableTransactionManagement
public class ChtAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChtAdminApplication.class);
    }
}
