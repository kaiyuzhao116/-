package com.example.demo.netty_websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

/**
 * Netty WebSocket 服务端启动类（方案A：已并入 Spring 生命周期）。
 *
 * 与旧版差异：
 *   - 旧：独立 main() 启动，和 Spring(8080) 是两个进程，拿不到 UserService Bean。
 *   - 新：作为 @Component，随 DemoApplication 一起启动，可注入 Spring 管理的
 *         WebSocketServerInitializer（进而拿到 UserService），实现握手 token 校验。
 *
 * 现在只需运行 DemoApplication，8090 的 WebSocket 服务会随之起来。
 */
@Component
public class WebSocketServer {

    // 单独用一个端口，避免和 8080(Spring)、8888(原TCP) 冲突
    private static final int PORT = 8090;

    private final WebSocketServerInitializer initializer;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;

    public WebSocketServer(WebSocketServerInitializer initializer) {
        this.initializer = initializer;
    }

    /**
     * Spring 容器就绪后启动 Netty。bind(...).sync() 只阻塞到端口绑定完成即返回，
     * 不会卡住 Spring 启动（旧代码里的 closeFuture().sync() 才是永久阻塞，已去掉，
     * 进程存活交给 Spring 维持）。
     */
    @PostConstruct
    public void start() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(initializer);

            serverChannel = b.bind(PORT).sync().channel();
            System.out.println("Netty WebSocket 服务端启动成功，监听端口: ws://localhost:" + PORT + "/ws");
        } catch (InterruptedException e) {
            // bind 失败（如端口被占）时，必须关掉已建线程组，
            // 否则非守护线程会吊住 JVM、端口与句柄都无法释放
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
            throw e;
        }
    }

    /** Spring 关闭时优雅释放线程组与端口 */
    @PreDestroy
    public void stop() {
        if (serverChannel != null) {
            serverChannel.close().syncUninterruptibly();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        System.out.println("Netty WebSocket 服务端已关闭");
    }
}
