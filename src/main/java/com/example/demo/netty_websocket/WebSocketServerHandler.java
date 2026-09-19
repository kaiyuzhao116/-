package com.example.demo.netty_websocket;

import com.example.demo.netty_websocket.dto.WsBaseDTO;
import com.example.demo.netty_websocket.enums.WsReqType;
import com.example.demo.netty_websocket.service.WebSocketService;
import com.example.demo.netty_websocket.service.impl.WebSocketServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 处理 WebSocket 文本帧的业务 Handler（已接入聊天转发）。
 *
 * 职责：
 *   - 握手完成：把本连接登记进在线花名册（WebSocketServiceImpl 单例）
 *   - 连接断开：从花名册注销
 *   - 收到消息：按类型分发
 *       "bye"  -> 关闭连接
 *       "ping" -> 心跳，忽略（前端每 30s 发一次的纯文本）
 *       其它   -> 作为聊天消息，广播给"除自己以外的所有在线连接"，并回一句确认给自己
 *
 * 注意：前端目前仍发纯文本，故这里对 ping/bye/普通文本做兼容处理；
 *       转发给他人时统一包成 WsBaseDTO 的 JSON（前端暂未解析，会显示成 JSON 原文）。
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final WebSocketService wsService = WebSocketServiceImpl.getInstance();

    /**
     * 握手完成事件：WebSocketServerProtocolHandler 升级成功后触发。
     * 此刻才适合登记连接、以及给客户端推欢迎语（连接已真正变为 WebSocket）。
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            String token = ctx.channel().attr(HttpHeadersHandler.ATTR_TOKEN).get();
            String ip = ctx.channel().attr(HttpHeadersHandler.ATTR_IP).get();
            // 登记进在线花名册
            wsService.connect(ctx.channel(), token);
            System.out.println("握手完成并登记在线 -> IP: " + ip + " token: " + token
                    + " channelId: " + ctx.channel().id().asShortText());
            ctx.writeAndFlush(new TextWebSocketFrame("欢迎连接 Netty WebSocket 服务端!"));
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String request = msg.text();
        System.out.println("服务端收到前端消息: " + request);

        // 退出指令：关闭连接
        if ("bye".equalsIgnoreCase(request)) {
            ctx.channel().close();
            return;
        }
        // 心跳：前端纯文本 ping，忽略即可（发送本身已维持连接活跃），不广播
        if ("ping".equalsIgnoreCase(request)) {
            return;
        }

        // 聊天转发：广播给除自己以外的所有在线连接
        String senderLabel = ctx.channel().id().asShortText();
        WsBaseDTO out = new WsBaseDTO(WsReqType.CHAT.getType(), "「" + senderLabel + "」" + request);
        wsService.sendToOtherOnline(out, ctx.channel());

        // 回一句确认给发送者自己（纯文本，兼容当前前端不解析 JSON）
        ctx.writeAndFlush(new TextWebSocketFrame("已发送: " + request));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 从在线花名册注销
        wsService.removed(ctx.channel());
        System.out.println("客户端已断开并注销，channelId: " + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
