package com.example.demo.netty_user.controller;

import com.example.demo.netty_user.entity.User;
import com.example.demo.netty_user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

/**
 * 用户 REST 接口（纯模拟）。运行在 Spring Boot（8080）上，与 Netty WebSocket（8090）无关。
 *
 * 测试：
 *   POST http://localhost:8080/netty_user/login?username=凯宇   -> {"token":"...."}
 *   GET  http://localhost:8080/netty_user/info?token=....        -> {"id":1,"username":"凯宇"}
 *   GET  http://localhost:8080/netty_user/count                  -> 用户数
 */
@RestController
@RequestMapping("/netty_user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String username) {
        String token = userService.login(username);
        return Collections.singletonMap("token", token);
    }

    @GetMapping("/info")
    public User info(@RequestParam String token) {
        return userService.getByToken(token);
    }

    @GetMapping("/count")
    public int count() {
        return userService.count();
    }
}
