package ycz.cn.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //得到管道
        ChannelPipeline pipeline = ch.pipeline();


        //加入一个netty 提供httpServerCodec
        //HttpServerCodec 说明
        // 1.是netty提供的http的编解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        // 2.增加自己自定义的handler
        pipeline.addLast("MyHttpServerHandler", new TestHttpServerHandler());

        System.out.println("ok~~~");
    }
}
