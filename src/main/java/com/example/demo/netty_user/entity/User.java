package com.example.demo.netty_user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简单用户实体（纯模拟，无密码）。
 * demo 用内存存储，仅保留最基本的标识字段。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String username;
}
