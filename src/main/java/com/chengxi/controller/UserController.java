package com.chengxi.controller;

import com.chengxi.feign.UserFeignClient;
import com.chengxi.pojo.TUser;
import com.chengxi.service.UserHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chengxi
 * @date 2018/12/3 15:09
 */

@RestController
@RequestMapping(value = "/userConsumer")
public class UserController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Resource
    private RestTemplate restTemplate;

    @GetMapping(value = "/{id}")
    public TUser selectUserById(@PathVariable Integer id){
        // 从注册服务中根据提供类的名称，获取对应的集群集合
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
        // 由于只测试了一个提供者，所以直接取第一个（从零开始），多个的时候涉及到负载均衡以及轮询等算法
        ServiceInstance serviceInstance = instances.get(0);

        // 根据获取的提供者信息，根据其中host获取对应的ip地址
        String ip = serviceInstance.getHost();
        // 根据获取的提供者信息，获取对应的port信息
        int port = serviceInstance.getPort();

        // 拼接url地址
        String url = "http://"+ip+":"+port+"/userService/"+id;

        ResponseEntity<TUser> forEntity = restTemplate.getForEntity(url, TUser.class);
        return forEntity.getBody();
    }


    /**
     * 使用Ribbon的负载均衡抒写的查询
     * @param ids
     * @return
     */
    @GetMapping(value = "/selectUsers")
    public List<TUser> selectUsersById(@RequestParam ArrayList<Integer> ids){
        // 创建一个用户集合类，用于接收用户集合
        List<TUser> tUsers = new ArrayList<>();

        // 直接拼写服务端地址
        String serviceUrl = "http://user-service/userService/";

        // 使用java8新特性lambda表达式
        ids.forEach(id -> {
            // 执行多次查询，这里使用提供者的服务器名称进行路径访问，
            // 因为在restTemplate的配制方法上加了@LoadBalanced注解，所以在每次发送url请求时都会进行拦截，由Ribbon在服务中进行服务名对应的ip，port查找、替换然后再查询
            tUsers.add(restTemplate.getForEntity(serviceUrl + id, TUser.class).getBody());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        return tUsers;
    }

    @Autowired
    private UserHystrixService userHystrixService;

    /**
     * 调用service中写的熔断机制代码
     * @param ids
     * @return
     */
    @GetMapping(value = "/selectUsersAndHystrix")
    public List<TUser> selectUser(@RequestParam ArrayList<Integer> ids){
        ArrayList<TUser> tUsers = new ArrayList<>();

        ids.forEach(id -> {
            TUser user = userHystrixService.selectUsersById(id);
            tUsers.add(user);
        });

        return tUsers;
    }

    @Autowired
    private UserFeignClient userFeignClient;

    /**
     * 采用feign对http的rest请求进行封装，自己不再用拼接，实现了松耦合。feign是对Eureka和ribbon的整合，提供负载均衡
     *  对应接口的@FeignClient注解中的服务名称加上调用方法的GetMapping的value构成请求路径，再由feign实现接口返回结果给我们
     * @param ids
     * @return
     */
    @GetMapping(value = "/selectUsersByIdAndFeign")
    public List<TUser> selectUsersByIdAndFeign(@RequestParam ArrayList<Integer> ids){
        ArrayList<TUser> tUsers = new ArrayList<>();

        ids.forEach(id -> {
            tUsers.add(userFeignClient.selectUserById(id));
        });

        return tUsers;
    }

}
