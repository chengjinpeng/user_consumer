package com.chengxi.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  这里是配置feign的日记级别类，因为feign注解修改的客户端被代理的时候，
 *  都会创建一个新的Feign.Logger实例，所以在配置文件中简单配置没有用，
 *  需要额外指定，并把配置文件放到feign接口的客户端中
 *
 *   详细级别依次递增
 * - NONE：不记录任何日志信息，这是默认值。
 * - BASIC：仅记录请求的方法，URL以及响应状态码和执行时间
 * - HEADERS：在BASIC的基础上，额外记录了请求和响应的头信息
 * - FULL：记录所有请求和响应的明细，包括头信息、请求体、元数据。
 *
 *
 * @author chengxi
 * @date 2018/12/4 23:31
 */
@Configuration
public class FeignConfig {

    /**
     * feign日志记录的设置
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

}
