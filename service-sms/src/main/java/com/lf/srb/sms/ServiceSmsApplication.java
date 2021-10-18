package com.lf.srb.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lf
 * @creat 2021-09-15 22:00
 */
@SpringBootApplication
@ComponentScan({"com.lf.srb" , "com.lf.common"})
@EnableFeignClients
public class ServiceSmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSmsApplication.class, args);
    }
}
