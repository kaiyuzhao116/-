package com.example.demo.netty_user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体，对应数据库表 t_user。
 * 纯模拟登录：无密码，token 在登录时生成并刷新。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String username;

    private String token;
}
