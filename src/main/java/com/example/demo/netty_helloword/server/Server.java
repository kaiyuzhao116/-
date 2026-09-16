package com.example.demo.netty_helloword.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public final class Server {
    public static void main(String[] args) throws Exception{

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerInitializer());


            // 绑定端口，并同步等待成功，即启动服务端
            ChannelFuture f = b.bind(8888);
            // 监听服务端关闭，并阻塞等待服务端关闭
            f.channel().closeFuture().sync();
        }finally{
            // 优雅关闭两个 EventLoopGroup 对象
                        bossGroup.shutdownGracefully();
                        workerGroup.shutdownGracefully();
        }
    }
}
