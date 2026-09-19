package com.example.demo.netty_user.service.impl;

import com.example.demo.netty_user.entity.User;
import com.example.demo.netty_user.mapper.UserMapper;
import com.example.demo.netty_user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 用户服务实现（纯模拟：无密码、无过期）。
 * 数据落到 MySQL 表 t_user，通过原生 MyBatis 的 UserMapper 读写。
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public String login(String username) {
        String token = UUID.randomUUID().toString().replace("-", "");
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            // 首次登录：建用户并绑定 token
            userMapper.insert(new User(null, username, token));
        } else {
            // 已存在：刷新 token（一人一 token）
            userMapper.updateToken(username, token);
        }
        return token;
    }

    @Override
    public User getByToken(String token) {
        if (token == null) {
            return null;
        }
        return userMapper.selectByToken(token);
    }

    @Override
    public boolean exists(String username) {
        return userMapper.selectByUsername(username) != null;
    }

    @Override
    public int count() {
        return userMapper.countAll();
    }
}
