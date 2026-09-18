package com.example.demo.netty_helloword.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    // StringDecoder / StringEncoder 是 @Sharable 的，可以安全复用
    private static final StringDecoder DECODER = new StringDecoder();
    private static final StringEncoder ENCODER = new StringEncoder();
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 添加帧限定符来防止粘包现象
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));

        //解码编码和客户端应该一样
        pipeline.addLast(DECODER);
        pipeline.addLast(ENCODER);
        // 添加自定义的处理器

        /**服务相关的设置的代码写完之后，我们再来编写主要的业务代码。 使用Netty编写业务层的代码，
        / 我们需要继承ChannelInboundHandlerAdapter 或SimpleChannelInboundHandler类
        /SimpleChannelInboundHandler类之后，
        / 会在接收到数据后会自动release掉数据占用的Bytebuffer资源。
        / 并且继承该类需要指定数据格式。 而继承ChannelInboundHandlerAdapter则不会自动释放，
        /需要手动调用ReferenceCountUtil.release()等方法进行释放。继承该类不需要指定数据格式。 所以在这里，个人推荐服务端继承ChannelInboundHandlerAdapter，
        *手动进行释放，防止数据未处理完就自动释放了'
        /**
         * 而且服务端可能有多个客户端进行连接，并且每一个客户端请求的数据格式都不一致，这时便可以进行相应的处理。 客户端根据情况可以继承SimpleChannelInboundHandler类。
         * 好处是直接指定好传输的数据格式，就不需要再进行格式的转换了
         */
        // 每个客户端连接都要创建新的 Handler 实例：
        // SimpleChannelInboundHandler 不是 @Sharable，复用同一个实例会导致第二个连接被拒绝
        pipeline.addLast(new ServerHandler());
    }
}
