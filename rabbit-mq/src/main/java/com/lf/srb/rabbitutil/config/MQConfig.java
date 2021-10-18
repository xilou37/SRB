package com.lf.srb.rabbitutil.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lf
 * @creat 2021-10-18 16:31
 */
@Configuration
public class MQConfig {
    @Bean
    public MessageConverter messageConverter(){
        //json字符串转换器
        return new Jackson2JsonMessageConverter();
    }
}
