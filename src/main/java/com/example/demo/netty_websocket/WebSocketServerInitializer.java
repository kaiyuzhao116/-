package com.example.demo.netty_websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * WebSocket 服务端的 pipeline 配置。
 *
 * 关键：WebSocket 连接的建立过程分两步
 *  1) 客户端先发一个普通 HTTP 请求（Upgrade: websocket）完成"握手"
 *  2) 握手成功后，同一条 TCP 连接升级为 WebSocket 全双工长连接
 *
 * 所以 pipeline 里必须同时具备 HTTP 编解码 + WebSocket 协议处理：
 *   HttpServerCodec      —— 把字节流编解码成 HTTP 请求/响应（处理握手阶段）
 *   HttpObjectAggregator —— 把分段的 HTTP 消息聚合成完整 FullHttpRequest（对应 重要.txt 学的知识点）
 *   WebSocketServerProtocolHandler —— 自动处理握手、Close/Ping/Pong 控制帧
 *   业务 Handler          —— 处理真正的文本/二进制消息帧
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        // 1. HTTP 编解码器（WebSocket 握手基于 HTTP）
        pipeline.addLast(new HttpServerCodec());

        // 2. 以块方式写入，支持大数据流（学习项目可选，这里保留帮助理解）
        pipeline.addLast(new ChunkedWriteHandler());

        // 3. 聚合 HTTP 消息，最大 64KB；拿到完整 FullHttpRequest
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));

        // 4. WebSocket 协议处理器：path 必须是 /ws，会自动完成握手并处理控制帧
        //    它会把 TextWebSocketFrame / BinaryWebSocketFrame 等继续向后传递
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        // 5. 自定义业务处理器：处理前端发来的文本消息
        //    每连接新建实例（SimpleChannelInboundHandler 非 @Sharable）
        pipeline.addLast(new WebSocketServerHandler());
    }
}
