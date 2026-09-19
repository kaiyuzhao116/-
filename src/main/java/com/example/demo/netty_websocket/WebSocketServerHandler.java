package com.example.demo.netty_websocket;

import com.example.demo.netty_user.entity.User;
import com.example.demo.netty_user.service.UserService;
import com.example.demo.netty_websocket.dto.WsBaseDTO;
import com.example.demo.netty_websocket.enums.WsReqType;
import com.example.demo.netty_websocket.service.WebSocketService;
import com.example.demo.netty_websocket.service.impl.WebSocketServiceImpl;
import com.example.demo.netty_websocket.util.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 处理 WebSocket 文本帧的业务 Handler（已接入聊天转发 + token 身份校验）。
 *
 * 职责：
 *   - 握手完成：用 token 查用户库校验
 *       有效 -> 绑定用户名到连接、登记在线花名册、推送欢迎语
 *       无效 -> 回一句"认证失败"并关闭连接（拒绝未登录访问）
 *   - 连接断开：从花名册注销
 *   - 收到消息：按类型分发
 *       "bye"  -> 关闭连接
 *       "ping" -> 心跳，忽略
 *       其它   -> 作为聊天消息，广播给"除自己以外的所有在线连接"（发送者显示真实用户名）
 *
 * 由 WebSocketServerInitializer 每连接 new 出来，并注入 Spring 的 UserService。
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final WebSocketService wsService = WebSocketServiceImpl.getInstance();

    /** 用户服务：握手时按 token 查库，解析出真实用户 */
    private final UserService userService;

    public WebSocketServerHandler(UserService userService) {
        this.userService = userService;
    }

    /**
     * 握手完成事件：WebSocketServerProtocolHandler 升级成功后触发。
     * 此刻做 token 校验：查得到用户才登记在线，否则拒绝并关闭。
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            String token = NettyUtil.getAttr(ctx.channel(), NettyUtil.TOKEN);
            String ip = NettyUtil.getAttr(ctx.channel(), NettyUtil.IP);

            // 用 token 查用户库校验
            User user = (token == null) ? null : userService.getByToken(token);
            if (user == null) {
                System.out.println("握手拒绝：token 无效 -> IP: " + ip + " token: " + token);
                ctx.writeAndFlush(new TextWebSocketFrame("认证失败：token 无效，请先登录后再连接"))
                        .addListener(f -> ctx.close());
                return;
            }

            String username = user.getUsername();
            // 把用户名挂到连接上，供后续广播取用
            NettyUtil.setAttr(ctx.channel(), NettyUtil.USERNAME, username);
            // 登记进在线花名册（以 token 作为在线身份，登录 token 唯一）
            wsService.connect(ctx.channel(), token);
            System.out.println("握手完成并登记在线 -> IP: " + ip + " 用户: " + username
                    + " channelId: " + ctx.channel().id().asShortText());
            ctx.writeAndFlush(new TextWebSocketFrame("欢迎 " + username + " 连接 Netty WebSocket 服务端!"));
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

        // 聊天转发：发送者标识优先用登录用户名，取不到再退回 channelId
        String username = NettyUtil.getAttr(ctx.channel(), NettyUtil.USERNAME);
        String senderLabel = (username != null) ? username : ctx.channel().id().asShortText();
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
