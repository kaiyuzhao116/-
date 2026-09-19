package com.example.demo.netty_websocket;

import com.example.demo.netty_websocket.util.NettyUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 握手拦截层：放在 HttpObjectAggregator 之后、WebSocketServerProtocolHandler 之前。
 *
 * 职责（只在握手阶段干一次活）：
 *   1) 从握手请求里提取 客户端IP 和 token（query 参数）
 *   2) 把它们存到 Channel 的属性上（AttributeKey）—— 相当于"挂到这条连接管道上"，
 *      后续任何 Handler 都能通过 channel.attr(KEY).get() 取用
 *   3) 存完立刻把自己从 pipeline 移除（ctx.pipeline().remove(this)），
 *      因为握手之后再来的都是 WebSocket 帧，不需要这一层了，移除可省去每帧穿透的开销
 *
 * ⚠️ 关键：
 *   - 继承 ChannelInboundHandlerAdapter，用 fireChannelRead 放行握手请求，
 *     否则 ProtocolHandler 拿不到请求，升级失败。
 *   - 用 @ChannelHandler.Sharable 因为这里不存任何"每连接独有"的实例字段，
 *     信息都存在 Channel 属性里，可安全共享同一个实例。
 */
@ChannelHandler.Sharable
public class HttpHeadersHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 只有握手阶段才是 FullHttpRequest
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;

            // 1) 取客户端 IP
            String ip = "";
            if (ctx.channel().remoteAddress() instanceof InetSocketAddress) {
                ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
            }

            // 2) 从 URL query 里取 token（示例：ws://localhost:8090/ws?token=abc123）
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            List<String> tokens = decoder.parameters().get("token");
            String token = (tokens != null && !tokens.isEmpty()) ? tokens.get(0) : null;

            // 3) 把 IP / token 存到 Channel 属性上 —— 后续 Handler 就能取用
            NettyUtil.setAttr(ctx.channel(), NettyUtil.IP, ip);
            NettyUtil.setAttr(ctx.channel(), NettyUtil.TOKEN, token);

            System.out.println("===== 握手解析并挂载到管道 =====");
            System.out.println("Origin: " + request.headers().get(HttpHeaderNames.ORIGIN));
            System.out.println("客户端IP: " + ip + "  token: " + token);
            System.out.println("==============================");
        }

        // ★ 先放行握手请求给下一层完成 WebSocket 升级
        ctx.fireChannelRead(msg);

        // ★ 再把自己从 pipeline 移除：握手已完成，后续数据帧不再经过本层
        ctx.pipeline().remove(this);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
