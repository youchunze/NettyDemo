package ycz.cn.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class    NettyServer {
    public static void main(String[] args) throws Exception{
        //创建BossGroup和WorkerGroup
        // 创建两个线程组bossGroup 只处理连接请求     workerGroup真正的处理业务请求
        //两个都是无限循环
        //BossGroup和WorkerGroup 创建的子线程的个数 默认是 实际CPU核数 * 2
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务器端启动的对象，配置参数
            ServerBootstrap b = new ServerBootstrap();
            //使用链式变成来设置
            b.group(bossGroup,workerGroup)  //设置两个线程组
                    .channel(NioServerSocketChannel.class)  //设置NioServerSocketChannel作为服务器通道的实现
                    .option(ChannelOption.SO_BACKLOG, 128)  //设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)  //设置保持活动连接状态
                    .handler(null)  //该方法会在bossGroup生效  childHandler会在workerGroup生效
                    .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道初始化对象
                        //给Pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });    //给我们的workerGroup的EventLoop 对应的管道设置处理器

            System.out.println("......服务器 is ready ......");
            //启动服务器 并绑定一个端口并且同步
            ChannelFuture cf = b.bind(6668).sync();

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
