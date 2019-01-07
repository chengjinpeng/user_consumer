package com.chengxi.feign;

import com.chengxi.pojo.TUser;
import org.springframework.stereotype.Component;

/**
 *  feign的失败回滚函数类
 * 
 * 
 * @author chengxi
 * @date 2018/12/4 23:00
 */
@Component
public class UserFeignClientFallback implements UserFeignClient {

    /**
     *  在配置文件中开启feign的熔断机制，然后继承feign的访问服务类的接口实现其方法作为访问服务类方法失败的回滚方法，当然也需要在feign接口中定义
     * @param id
     * @return
     */
    @Override
    public TUser selectUserById(Integer id) {
        TUser user = new TUser();
        user.setId(id);
        user.setUsername("用户查询异常");
        return user;
    }
}
