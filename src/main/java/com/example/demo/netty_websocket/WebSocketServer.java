package com.example.demo.netty_websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Netty WebSocket 服务端启动类。
 *
 * 与 netty_helloword（纯 TCP）的区别：
 * - 纯 TCP：浏览器无法直接连接，只能 Java 客户端连。
 * - WebSocket：底层是 HTTP 握手升级为长连接，浏览器原生支持 new WebSocket()。
 *
 * 学习重点：WebSocket 的 pipeline 要先经过 HTTP 相关解码器完成握手，
 * 再交给 WebSocket 帧处理器处理业务消息。
 */
public final class WebSocketServer {

    // 单独用一个端口，避免和 8080(Spring)、8888(原TCP) 冲突
    private static final int PORT = 8090;

    public static EventLoopGroup bossGroup;
    public static EventLoopGroup workerGroup;

    public static void main(String[] args) throws Exception {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new WebSocketServerInitializer());

            ChannelFuture f = b.bind(PORT).sync();
            System.out.println("Netty WebSocket 服务端启动成功，监听端口: ws://localhost:" + PORT + "/ws");
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
