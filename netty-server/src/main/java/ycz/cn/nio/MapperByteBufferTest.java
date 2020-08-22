package ycz.cn.nio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MapperByteBuffer可以让文件在内存中（堆外内存）修改,操作系统不需要拷贝一次
 */
public class MapperByteBufferTest {

    public static void main(String[] args) throws Exception {

        RandomAccessFile randomAccessFile = new RandomAccessFile("file02.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        /**
         * 参数1：读写模式
         * 参数2：可以直接修改起始位置
         * 参数3：5是映射到内存的直接大小，即将1.txt的多少个字节映射到内存，
         *      可以直接修改的范围是0~5
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte)'H');
        mappedByteBuffer.put(5, (byte)'9');

        randomAccessFile.close();
        System.out.println("修改成功！");
    }
}
