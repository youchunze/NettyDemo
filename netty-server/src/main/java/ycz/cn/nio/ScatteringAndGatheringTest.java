package ycz.cn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Scattering:将数据写入到Buffer时，可以采用Buffer数组，依次写入[分散]
 * Gathering: 从buffer读取数据，可以采用buffer数组,依次读
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws Exception {
        //使用ServerSockChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到Socket并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待客户的连接（Telnet）
        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength = 8;
        // 循环读数据
        while (true) {

            int byteRead = 0;

            while (byteRead < messageLength) {
                long l = socketChannel.read(byteBuffers);
                byteRead += l;
                System.out.println("byteRead=" + byteRead);
                //使用流打印
                Arrays.asList(byteBuffers).stream().map(buffer -> "position=" + buffer.position() + ", limit=" + buffer.limit())
                        .forEach(System.out::println);

                }


            //将所有buffer进行flip()
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }

            //将所有的buffer进行clear()
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());

            System.out.println("byteRead:=" + byteRead + "byteWrite:=" + byteWrite + "messageLength:=" + messageLength);
        }
    }
}
