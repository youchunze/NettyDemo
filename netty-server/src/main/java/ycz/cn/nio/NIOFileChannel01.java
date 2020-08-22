package ycz.cn.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {

    public static void main(String[] args) throws Exception {
        String s = "hello world 程序";

        FileOutputStream fileOutputStream = new FileOutputStream("F:\\file01.txt");

        // 通过输出流获取对应的文件FileChannel
        //FileChannel 的真是类型是 FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        byteBuffer.put(s.getBytes());

        byteBuffer.flip();

        // 讲ByteBuffer数据写入到 FileChannel
        fileChannel.write(byteBuffer);

        fileOutputStream.close();

    }
}
