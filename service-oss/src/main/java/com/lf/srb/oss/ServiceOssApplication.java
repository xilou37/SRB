package com.lf.srb.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lf
 * @creat 2021-09-16 19:15
 */
@SpringBootApplication
@ComponentScan({"com.lf.srb", "com.lf.common"})
public class ServiceOssApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(ServiceOssApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
