package com.example.demo.netty_websocket.service.impl;

import com.example.demo.netty_websocket.dto.WsBaseDTO;
import com.example.demo.netty_websocket.service.WebSocketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * WebSocket 业务服务实现（参照成熟项目改编的 demo 简化版）。
 *
 * 与原版的关键差异（demo 缺少相应基建，做了取舍）：
 *   - 在线身份用握手解析出的 token 表示（原版 UserTokenDTO + JWT + Redis）
 *   - 消息体直接 Jackson 序列化成 JSON 写入 TextWebSocketFrame（原版 Result 包装）
 *   - 发送直接 writeAndFlush，不引入业务线程池（Netty 本身即异步）
 *   - 去掉登录鉴权、上下线事件、设备校验等 demo 用不到的逻辑
 *
 * 用单例：WebSocketServerHandler 是每连接 new 出来的、非 Spring 管理，
 * 所有连接必须共享同一份在线花名册，故用静态单例。
 */
public class WebSocketServiceImpl implements WebSocketService {

    // ==================== 单例 ====================
    private static final WebSocketServiceImpl INSTANCE = new WebSocketServiceImpl();

    private WebSocketServiceImpl() {
    }

    public static WebSocketServiceImpl getInstance() {
        return INSTANCE;
    }

    // ==================== 在线花名册 ====================
    /** channel -> token：所有在线连接 */
    private static final ConcurrentHashMap<Channel, String> ONLINE_WS_MAP = new ConcurrentHashMap<>();
    /** token -> 该身份的所有 channel：同一人可能多端在线 */
    private static final ConcurrentHashMap<String, CopyOnWriteArrayList<Channel>> ONLINE_UID_MAP = new ConcurrentHashMap<>();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // ==================== 连接 / 断开 ====================
    @Override
    public void connect(Channel channel, String token) {
        if (token == null) {
            // demo 没有强制登录，token 可能为空；仍登记连接以便广播
            ONLINE_WS_MAP.put(channel, null);
            return;
        }
        ONLINE_WS_MAP.put(channel, token);
        ONLINE_UID_MAP.computeIfAbsent(token, k -> new CopyOnWriteArrayList<>()).add(channel);
    }

    @Override
    public void removed(Channel channel) {
        String token = ONLINE_WS_MAP.remove(channel);
        if (token != null) {
            CopyOnWriteArrayList<Channel> channels = ONLINE_UID_MAP.get(token);
            if (channels != null) {
                channels.remove(channel);
                if (channels.isEmpty()) {
                    ONLINE_UID_MAP.remove(token);
                }
            }
        }
    }

    // ==================== 推送 ====================
    @Override
    public void sendToAllOnline(WsBaseDTO wsBaseDTO, String skipUid) {
        ONLINE_WS_MAP.forEach((channel, token) -> {
            if (skipUid != null && Objects.equals(token, skipUid)) {
                return; // 跳过发送者自己
            }
            sendMsg(channel, wsBaseDTO);
        });
    }

    @Override
    public void sendToAllOnline(WsBaseDTO wsBaseDTO) {
        sendToAllOnline(wsBaseDTO, null);
    }

    @Override
    public void sendToOtherOnline(WsBaseDTO wsBaseDTO, Channel self) {
        ONLINE_WS_MAP.keySet().forEach(channel -> {
            if (Objects.equals(channel, self)) {
                return; // 跳过发送者自己
            }
            sendMsg(channel, wsBaseDTO);
        });
    }

    @Override
    public void sendToUidList(WsBaseDTO wsBaseDTO, List<String> uidList) {
        if (uidList == null) {
            return;
        }
        uidList.forEach(uid -> sendToUid(wsBaseDTO, uid));
    }

    @Override
    public void sendToUid(WsBaseDTO wsBaseDTO, String uid) {
        CopyOnWriteArrayList<Channel> channels = ONLINE_UID_MAP.get(uid);
        if (channels == null || channels.isEmpty()) {
            System.out.println("用户不在线: " + uid);
            return;
        }
        channels.forEach(channel -> sendMsg(channel, wsBaseDTO));
    }

    /** 真正的发送：把消息体序列化为 JSON，写成文本帧推给该连接 */
    private ChannelFuture sendMsg(Channel channel, WsBaseDTO wsBaseDTO) {
        if (channel == null || !channel.isActive()) {
            return null;
        }
        return channel.writeAndFlush(new TextWebSocketFrame(toJson(wsBaseDTO)));
    }

    private String toJson(WsBaseDTO wsBaseDTO) {
        try {
            return MAPPER.writeValueAsString(wsBaseDTO);
        } catch (Exception e) {
            throw new RuntimeException("WsBaseDTO 序列化失败", e);
        }
    }

    // ==================== 查询 ====================
    @Override
    public boolean isOnline(String uid) {
        CopyOnWriteArrayList<Channel> channels = ONLINE_UID_MAP.get(uid);
        return channels != null && !channels.isEmpty();
    }

    @Override
    public int getOnlineCount() {
        return ONLINE_WS_MAP.size();
    }
}
