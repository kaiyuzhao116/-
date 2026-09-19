package com.example.demo.netty_websocket.util;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * Netty 连接属性工具类：集中定义"可挂到 Channel 上的槽位"及其读写。
 *
 * 抽取自原先散落在 HttpHeadersHandler / WebSocketServerHandler 里的 attr 定义与读写，
 * 参照成熟项目做法统一收口——纯整理，不改变行为。
 */
public class NettyUtil {

    /** 客户端 IP（握手阶段解析） */
    public static final AttributeKey<String> IP = AttributeKey.valueOf("clientIp");
    /** 握手 URL query 里带过来的 token */
    public static final AttributeKey<String> TOKEN = AttributeKey.valueOf("token");
    /** 握手校验通过后，用 token 查库解析出的用户名，供广播显示发送者 */
    public static final AttributeKey<String> USERNAME = AttributeKey.valueOf("username");

    private NettyUtil() {
    }

    /** 往这条连接的槽位写值 */
    public static <T> void setAttr(Channel channel, AttributeKey<T> key, T data) {
        Attribute<T> attr = channel.attr(key);
        attr.set(data);
    }

    /** 从这条连接的槽位取值 */
    public static <T> T getAttr(Channel channel, AttributeKey<T> key) {
        return channel.attr(key).get();
    }
}
