package ycz.cn.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {

    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("F:\\01.jpg");
        FileChannel sourceCh = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("F:\\02.jpg");
        FileChannel destCh = fileOutputStream.getChannel();

        //使用transferFrom完成拷贝
        destCh.transferFrom(sourceCh, 0, sourceCh.size());

        sourceCh.close();
        destCh.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
