package ycz.cn.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

    public static void main(String[] args) throws IOException {

        // 线程池机制
        // 1、先创建线程池
        // 2、如果有客户的链接 创建线程与之通信

        // 创建线程池
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("服务器启动了");

        while (true) {
            System.out.println("线程信息: id = " + Thread.currentThread().getId() + "名字 = " + Thread.currentThread().getName());
            // 监听 等待客户的连接   阻塞
            final Socket socket = serverSocket.accept();

            System.out.println("连接到一个客户端了");

            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    handle(socket);
                }
            });
        }
    }

//    编写一个和客户的通信的方法
    public static void handle(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
//      从Socket获取输入流
            InputStream inputStream = socket.getInputStream();
            while (true) {
                System.out.println("线程信息: id = " + Thread.currentThread().getId() + "名字 = " + Thread.currentThread().getName());
                int read = inputStream.read(bytes);
                if (read != -1){
                    System.out.println(new String(bytes,0,read)); //输出客户端发送的数据
                }else {
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
