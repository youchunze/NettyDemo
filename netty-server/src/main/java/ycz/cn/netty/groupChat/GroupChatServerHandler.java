package ycz.cn.netty.groupChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.text.SimpleDateFormat;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个Channel组 管理所有的Channel
    // GlobalEventExecutor全局事件执行器 帮助我们执行ChannelGroup
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH;mm:ss");



    //handleAdded 表示连接建立，一旦连接，第一个被执行
    //将当前Channel加入到ChannelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将客户加入聊天的信息推送到其他客户端
        /**
         *  该方法会将所有的channelGroup进行遍历 并发送消息
         */
        channelGroup.writeAndFlush("【客户端】" + channel.remoteAddress() + " 加入聊天\n");
        channelGroup.add(channel);

    }

    //断开连接  将某某客户离开推送给其他在线客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        ctx.writeAndFlush("【客户端】" + channel.remoteAddress() + "离开聊天\n");
        System.out.println("channelGroup size " +  channelGroup.size());
    }

    //  表示channel处于活动状态 提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了~");
    }

    // 表示 channel处于非活动状态 提示xx下线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了~");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取到当channel
        Channel channel = ctx.channel();
        // 这时我们遍历channelGroup 根据不同的情况 回送不同的消息
        channelGroup.forEach(ch -> {
            if (channel != ch){ //表示当前channel 直接转发
                ch.writeAndFlush("【客户】" + channel.remoteAddress() + "发送消息 " + msg + "\n");
            }else {
                ch.writeAndFlush("【自己】发送了消息 " + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
