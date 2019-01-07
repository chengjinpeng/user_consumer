package com.chengxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient     // 开启eureka客户端（服务消费者）
@EnableCircuitBreaker      // 开启熔断机制
//@EnableFeignClients        // 在客户端开启feign，封装http的rest请求
public class UserConsumerApplication {

    /**
     *  在SpringCloud中使用Ribbon，
     *  需要在httpclient配置文件中的restTemplate的返回方法上添加@LoadBalanced注解
     *  在使用这个restTemplate发出请求时都会被拦截处理再发出，具体看ribbon配置和源码
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(UserConsumerApplication.class, args);
    }
}
