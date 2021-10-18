package com.lf.srb.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lf
 * @creat 2021-09-09 20:08
 */
@SpringBootApplication
@ComponentScan({"com.lf.srb" , "com.lf.common"})
public class ServiceCoreApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(ServiceCoreApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
