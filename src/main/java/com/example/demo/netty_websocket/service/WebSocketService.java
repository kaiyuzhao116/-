package com.example.demo.netty_websocket.service;

import com.example.demo.netty_websocket.dto.WsBaseDTO;
import io.netty.channel.Channel;

import java.util.List;

/**
 * WebSocket 业务服务接口。
 *
 * 说明：这是参照成熟项目改编的简化版，用 demo 现有类型对齐——
 *   - 消息体用 WsBaseDTO（原版是 WsBaseVO）
 *   - 在线身份用 String uid/token 表示（demo 无完整用户体系，去掉了 authorize/logout）
 *
 * 职责：维护"在线连接花名册"，并提供广播 / 定向 / 单发等推送能力。
 */
public interface WebSocketService {

    /**
     * 处理新连接建立事件：把 channel 登记进在线花名册
     *
     * @param channel 连接
     * @param token   握手阶段解析出的身份标识
     */
    void connect(Channel channel, String token);

    /**
     * 处理连接断开事件：把 channel 从在线花名册移除
     *
     * @param channel 连接
     */
    void removed(Channel channel);

    /**
     * 推送消息给所有在线的人
     *
     * @param wsBaseDTO 发送的消息体
     * @param skipUid   需要跳过的人（一般是发送者自己）
     */
    void sendToAllOnline(WsBaseDTO wsBaseDTO, String skipUid);

    /**
     * 推送消息给所有在线的人
     *
     * @param wsBaseDTO 发送的消息体
     */
    void sendToAllOnline(WsBaseDTO wsBaseDTO);

    /**
     * 推送消息给除发送者以外的所有在线连接（按 channel 排除自己）
     * 用于聊天转发：demo 各连接 token 相同，无法按 uid 区分，故按 channel 排除
     *
     * @param wsBaseDTO 发送的消息体
     * @param self      发送者自己的连接（将被跳过）
     */
    void sendToOtherOnline(WsBaseDTO wsBaseDTO, Channel self);

    /**
     * 推送消息给指定的一批人
     *
     * @param wsBaseDTO 发送的消息体
     * @param uidList   接收人的 id 列表
     */
    void sendToUidList(WsBaseDTO wsBaseDTO, List<String> uidList);

    /**
     * 推送消息给指定的某一个人
     *
     * @param wsBaseDTO 发送的消息体
     * @param uid       接收人 id
     */
    void sendToUid(WsBaseDTO wsBaseDTO, String uid);

    /**
     * 某人是否在线
     *
     * @param uid 用户 id
     * @return boolean
     */
    boolean isOnline(String uid);

    /**
     * 获取当前在线人数
     *
     * @return int
     */
    int getOnlineCount();
}
