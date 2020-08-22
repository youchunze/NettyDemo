package ycz.cn.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {

    public static void main(String[] args) throws Exception {

        // 创建文件
        File file = new File("F:\\file01.txt");
        //创建文件输入流
        FileInputStream fileInputStream = new FileInputStream(file);
        // 通过输出流获取对应的文件FileChannel
        //FileChannel 的真是类型是 FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();

        // 创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        // 讲ByteBuffer数据写入到 FileChannel
        fileChannel.read(byteBuffer);

        // 将字节数据转出String输出
        System.out.println(new String(byteBuffer.array()));

        fileInputStream.close();

    }
}
