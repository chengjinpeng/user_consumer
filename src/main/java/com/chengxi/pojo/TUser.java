package com.chengxi.pojo;

import lombok.Data;

/**
 * 生成类: t_user
 * 创建时间: 2018年12月03日
 * 作者: 姓名
 */

@Data
public class TUser {

    private Integer id;

    private Integer age;

    private String username;

    private String password;

    private String email;

    private String sex;

    public TUser() {
    }

    @Override
    public String toString() {
        return "TUser{" +
                "id=" + id +
                ", age=" + age +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}