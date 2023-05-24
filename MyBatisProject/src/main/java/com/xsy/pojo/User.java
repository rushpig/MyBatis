package com.xsy.pojo;

import lombok.Data;

@Data
public class User {
    //主键标识
    private Integer id;
    //用户名
    private String username;

    @Override
    public String toString() {
    return "User{" +
    "id=" + id +
    ", username='" + username + '\'' + '}';
    }
}