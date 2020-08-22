package ycz.cn.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 创建ServerSocketChannel
 */
public class NIOServer {

    public static void main(String[] args) throws Exception{

        //创建 ServerSocketChannel 得到 SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到Selector 对象
        Selector selector = Selector.open();

        //绑定端口6666 在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 把ServerSocketChannel注册到 Selector  关心 事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {
            //这里等待了1s 没有事件发生就返回
            if (selector.select(1000) == 0){ // 没有事件发生
                System.out.println("服务器等待了1秒, 无连接");
                continue;
            }
            //如果返回的 > 0; 就获取到相关的SelectionKey集合
            //1.如果返回的 > 0;表示已经获取到关注事件
            //2.selector.selectedKeys()返回关注事件的集合
            //     通过SelectionKeys 反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys 数量 = " + selectionKeys.size());
            //遍历 selectionKeys
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                //获取 selectionKey
                SelectionKey key = keyIterator.next();
                //根据key 发生的事件 做不同的处理
                if (key.isAcceptable()) {   //如果是 OP_ACCEPT
                    // 给该客户端生成SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功 生成一个SocketChannel " + socketChannel.hashCode());
                    //将SocketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //SocketChannel 注册到Selector 关注事件为OP_READ, 同时给SocketChannel 关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("客户端连接后 ，注册的selectionKey 数量=" + selector.keys().size()); //2,3,4..
                }
                if (key.isReadable()) { // 发生OP_READ
                    // 通过key 反向获取到对应的Channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到该Channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("form 客户端 " + new String(buffer.array()));
                }
                    //手动从集合移除当前的 SelectionKey,防止重复操作
                    keyIterator.remove();
            }
        }

    }
}
