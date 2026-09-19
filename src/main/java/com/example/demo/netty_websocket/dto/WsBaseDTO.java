package com.example.demo.netty_websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WebSocket 消息基础 DTO。
 *
 * 用统一结构替代裸文本，前端与服务端之间收发 JSON 字符串：
 *   { "type": 1, "data": "..." }
 * type 作为消息类型标识（如：普通聊天 / 心跳 / 系统通知...），
 * data 承载真正的业务内容。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsBaseDTO {

    /**
     * ws 客户端 req 类型
     */
    private Integer type;

    /**
     * 业务数据内容
     */
    private String data;
}
