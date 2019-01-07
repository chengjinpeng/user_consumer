package com.chengxi.feign;

import com.chengxi.config.FeignConfig;
import com.chengxi.pojo.TUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *  dec：在feign注解中配置要访问的服务类（value的值），fallback是feign访问超时，熔断机制的回滚函数，一般是继承此接口的类（注意不同于feign帮我们实现接口中的实现类）
 *  feign的熔断机制是关闭的，需要在配置文件中配置。只要在fallback后面写上回滚函数类即可，对应实现方法会自动调用
 *  configuration：是对于当前feign代理的客户端的配置文件
 *
 *  如果参数是对象，请求就不会成功，即使指定了Get方法，feign依然会以Post方式进行发送请求。
 * 	解决方案：@RequestParam("id") Long id,@RequestParam("name") Long name
 *
 *
 * @author chengxi
 * @date 2018/12/4 22:20
 */
@FeignClient(value = "user-service",fallback = UserFeignClientFallback.class,configuration = FeignConfig.class)
public interface UserFeignClient {

    /**
     * feign根据GetMapping中的路径加上对应服务的地址找到对应的方法，如类上注解的服务名+GetMapping中的路径
     * 注意这里 @PathVariable 的注解中的参数不能省略，如现在的id，他是将输入的的参数通过注解参数传给Mapping中的路径参数。
     *      可以看成从路径中获取参数，但是注解参数不能省略且三个参数值还必须一致
     * @param id
     * @return
     */
    @GetMapping(value = "/userService/{id}")
    public TUser selectUserById(@PathVariable("id") Integer id);


}
