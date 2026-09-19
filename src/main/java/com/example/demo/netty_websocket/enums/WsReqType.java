package com.example.demo.netty_websocket.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ws 连接请求类型枚举。
 * 与 WsBaseDTO.type 对应，用于区分前端发来的消息类型。
 */
@Getter
@AllArgsConstructor
public enum WsReqType {
    CHECK_TOKEN(1, "登录认证"),
    HEARTBEAT(2, "心跳包"),
    CHAT(3, "聊天消息"),
    SYSTEM(4, "系统通知"),
    ;

    private final Integer type;
    private final String desc;

    // 缓存：type -> 枚举，避免每次遍历 values()
    private static final Map<Integer, WsReqType> cache;

    static {
        cache = Arrays.stream(WsReqType.values()).collect(Collectors.toMap(WsReqType::getType, Function.identity()));
    }

    public static WsReqType of(Integer type) {
        return cache.get(type);
    }
}
