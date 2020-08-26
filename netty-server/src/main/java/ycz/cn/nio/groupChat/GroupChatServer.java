package ycz.cn.nio.groupChat;

import io.netty.buffer.ByteBuf;
import org.omg.CORBA.INTERNAL;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {

    private Selector selector;
    private ServerSocketChannel listenChannel;
    private final static int PORT = 6667;

    /**
     * 构造方法 初始化构造器
     */
    public GroupChatServer(){
        try{
            //得到选择器
            selector = Selector.open();
            //serverSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //配置非阻塞
            listenChannel.configureBlocking(false);
            //将listenChannel注册到Selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //监听
    public void listen(){
        try {
            while (true) {
                int count = selector.select();
                //有事件处理
                if (count > 0){
                    //遍历循环得到selectionKeys集合
                    Iterator<SelectionKey> iterator =  selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        //取出SelectionKey
                        SelectionKey key = iterator.next();
                        //监听到accept事件
                        if (key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            //提示
                            System.out.println(sc.getRemoteAddress() + " 上线 ");
                        }
                        if (key.isReadable()){  //通道发生Read事件,即通道是可读的状态
                            readData(key);
                        }
                        //当前key删除 防止重复处理
                        iterator.remove();
                    }
                }else {
                    System.out.println("等待中。。。");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            //发生异常处理....
        }
    }

    //读取客户端信息
    private void readData(SelectionKey key){

        //定义一个SocketChannel
        SocketChannel socketChannel = null;
        try {
            //取到关联的Channel
            socketChannel = (SocketChannel) key.channel();
            //创建Buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int count = socketChannel.read(buffer);
            //根据count的值做处理
            if(count > 0){
                //把缓存区的数据转成字符串
                String msg = new String(buffer.array());
                System.out.println("from 客户端: " + msg);

                //向其他客户端转发消息（去掉自己）
                sendInfoToOtherClients(msg, socketChannel);
            }

        }catch (Exception e){
            try {
                System.out.println(socketChannel.getRemoteAddress() + " 离线了");
                //取消注册
                key.channel();
                //关闭通道
                socketChannel.close();
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }

    //转发个其他客户(通道)
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws Exception{

        System.out.println("服务器转发消息中....");
        //遍历所有注册到Selector上的SocketChannel 并排除自己
        for (SelectionKey key : selector.keys()){
            //通过key 取出SocketChannel
            Channel targetChannel = key.channel();
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self){
                SocketChannel dest =  (SocketChannel)targetChannel;
                //将msg存储到buff
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer数据写到通道
                dest.write(buffer);
            }
        }

    }

    public static void main(String[] args) throws Exception{
        //创建服务器对象
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }
}
