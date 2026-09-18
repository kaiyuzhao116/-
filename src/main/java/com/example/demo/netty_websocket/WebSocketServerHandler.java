package com.example.demo.netty_websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalTime;

/**
 * 处理 WebSocket 文本帧的业务 Handler。
 *
 * WebSocket 通讯的数据单位是"帧(Frame)"：
 *   TextWebSocketFrame  —— 文本帧
 *   BinaryWebSocketFrame —— 二进制帧
 *   PingWebSocketFrame / PongWebSocketFrame —— 心跳控制帧（已被上层协议处理器拦截）
 *
 * 这里我们只关心前端发来的文本消息，所以泛型指定 TextWebSocketFrame。
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 收到前端文本消息时调用。
     * 这里演示一个"回声服务"：收到什么，加工后回给前端。
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String request = msg.text();
        System.out.println("服务端收到前端消息: " + request);

        if ("bye".equalsIgnoreCase(request)) {
            ctx.channel().close();
            return;
        }

        String response = "服务端已收到: '" + request + "'  时间: " + LocalTime.now();
        // 回写文本帧，前端 onmessage 会收到
        ctx.writeAndFlush(new TextWebSocketFrame(response));
    }

    /**
     * 握手完成、连接建立时触发（浏览器 WebSocket 的 onopen 对应服务端这一刻）。
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端已连接，channelId: " + ctx.channel().id().asLongText());
        ctx.writeAndFlush(new TextWebSocketFrame("欢迎连接 Netty WebSocket 服务端!"));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端已断开，channelId: " + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
