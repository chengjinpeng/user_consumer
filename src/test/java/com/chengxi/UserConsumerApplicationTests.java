package com.chengxi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserConsumerApplication.class)
public class UserConsumerApplicationTests {

    // Ribbon的负载均衡客户端
    @Resource
    RibbonLoadBalancerClient client;

    @Test
    public void contextLoads() {

        for (int i = 0; i < 100; i++) {
            // 这里的测试choose方法是Ribbon默认的简单轮询方法
            ServiceInstance choose = this.client.choose("user-service");
            System.out.println(choose.getHost()+":"+choose.getPort());
        }

    }

}
