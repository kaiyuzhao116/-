package com.example.demo.netty_user.service.impl;

import com.example.demo.netty_user.entity.User;
import com.example.demo.netty_user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用户服务实现（纯模拟：内存存储、无密码、无过期）。
 * 由 Spring 容器管理，注入到 UserController。
 */
@Service
public class UserServiceImpl implements UserService {

    private final AtomicLong idGenerator = new AtomicLong(1);
    /** username -> User */
    private final Map<String, User> userStore = new ConcurrentHashMap<>();
    /** token -> username */
    private final Map<String, String> tokenStore = new ConcurrentHashMap<>();

    @Override
    public String login(String username) {
        User user = userStore.computeIfAbsent(username,
                name -> new User(Long.valueOf(idGenerator.getAndIncrement()), name));
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenStore.put(token, user.getUsername());
        return token;
    }

    @Override
    public User getByToken(String token) {
        String username = tokenStore.get(token);
        if (username == null) {
            return null;
        }
        return userStore.get(username);
    }

    @Override
    public boolean exists(String username) {
        return userStore.containsKey(username);
    }

    @Override
    public int count() {
        return userStore.size();
    }
}
