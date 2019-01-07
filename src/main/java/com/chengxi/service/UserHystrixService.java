package com.chengxi.service;

import com.chengxi.pojo.TUser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * @author chengxi
 * @date 2018/12/4 20:44
 */
@Service
@Slf4j  // 日志类注解
//@Transactional
public class UserHystrixService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 导入日志类，被idea的slf4j注解取代
     */
//    private static final Logger logger = LoggerFactory.getLogger(UserHystrixService.class);

    /**
     * 演示熔断机制，注意要在服务器处设置线程睡眠用来显示请求超时，熔断机制接收到请求超时的时候回调用失败回滚函数
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "selectUserByIdFallback")
    public TUser selectUsersById(Integer id){

            // 开始发起请求的时间
            long begin = System.currentTimeMillis();

            // 拼接发送的url地址
            String url = "http://user-service/userService/"+id;
            // 根据整合的restTemplate模板发送请求，并添加到用户集合中
            ResponseEntity<TUser> user = restTemplate.getForEntity(url, TUser.class);
            // 响应结束的时间
            long end = System.currentTimeMillis();

            // 使用idea提供的slf4j输出日志，当前类直接输出不用再标记。大括号是用来存放后面的数值的
            log.info("访问的时间是：{}",end - begin);

            // 使用日志记录访问的时间
//            logger.info("访问的时间是：{}",end - begin);

        return user.getBody();
    }

    /**
     * Hystrix失败回滚调用的函数
     * @param id
     * @return
     */
    public TUser selectUserByIdFallback(Integer id){

            TUser user = new TUser();
            user.setId(id);
            user.setUsername("用户查询的信息出现异常");

        return user;
    }

}
