package ycz.cn.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    public static void main(String[] args) throws Exception {
        //客户端只需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            //创建客户端启动对象
            // 主要客户端是Bootstrap 服务端是ServerBootStrap
            Bootstrap b = new Bootstrap();
            b.group(group) //设置线程组
                    .channel(NioSocketChannel.class)    //设置客户端通道的实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandel());
                        }
                    });
            System.out.println("客户端 is ok");

            //启动客户端 连接服务器
            //ChannelFuture 涉及到Netty的异步模型
            ChannelFuture cf = b.connect("127.0.0.1", 6668).sync();

            //给关闭通道进行监听
            cf.channel().closeFuture().sync();

        }finally {
            group.shutdownGracefully();
        }

    }
}
