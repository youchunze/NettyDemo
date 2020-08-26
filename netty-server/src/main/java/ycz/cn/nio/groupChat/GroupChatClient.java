package ycz.cn.nio.groupChat;


import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {

    //定义相关属性
    private final String Host = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient() throws IOException {
        selector = Selector.open();
        //连接服务器
        socketChannel = socketChannel.open(new InetSocketAddress(Host, PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //将Channel注册到selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
        username =  socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + " is OK...");
    }

    //向服务器发生消息
    public void sendInfo(String info){
        info = username + "说：" + info;
        try{
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //读取从服务器回复的消息
    private void readInfo(){
        try {
            int readChannels = selector.select();
            if (readChannels > 0){  //有可用通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey ket = iterator.next();
                    if (ket.isReadable()){
                        SocketChannel sc = (SocketChannel) ket.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        sc.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                    iterator.remove();
                }
            }else {
//                System.out.println("没有可用的通道...");
            }

        }catch (Exception e){

        }
    }

    public static void main(String[] args) throws Exception{
        //启动客户端
        GroupChatClient chatClient = new GroupChatClient();
        //启动一个线程
        new Thread(){
            public void run(){
                while (true) {
                    chatClient.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        //发送数据给服务器端
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }


}
