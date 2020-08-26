package ycz.cn.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 读取数据实例(我们这里可以读取客户端发生的消息)
     * ChannelHandlerContext ctx：上下文对象，含有管道pipeline，通道Channel，地址
     * Object msg:客户端发生的消息 默认Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程 " + Thread.currentThread().getName());
        System.out.println("server ctx: " + ctx);
        System.out.println("看看 channel 和 pipeline 的关系");
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline();// pipeline本质是一个双向链表
        //将msg 转换成ByteBuf
        ByteBuf buf = (ByteBuf)msg;
        System.out.println("客户端发送消息是： " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + channel.remoteAddress());
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush 将数据写入到缓存 并刷新
        //
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端~ \\(^o^)/~",CharsetUtil.UTF_8));
    }

    //处理异常 一般是关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
