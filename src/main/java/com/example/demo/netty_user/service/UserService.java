package com.example.demo.netty_user.service;

import com.example.demo.netty_user.entity.User;

/**
 * 用户服务接口（纯模拟，无密码）。
 */
public interface UserService {

    /**
     * 模拟登录：不存在则自动创建用户，返回 token。
     */
    String login(String username);

    /**
     * 根据 token 取回用户，取不到返回 null。
     */
    User getByToken(String token);

    /**
     * 用户名是否存在。
     */
    boolean exists(String username);

    /**
     * 当前用户总数。
     */
    int count();
}
