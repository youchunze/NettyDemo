package ycz.cn.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {

    public static void main(String[] args) throws Exception {


        //创建文件输入流
        FileInputStream fileInputStream = new FileInputStream("F:\\file01.txt");
        FileChannel fileChannel1 = fileInputStream.getChannel();


        FileOutputStream fileOutputStream = new FileOutputStream("F:\\file02.txt");
        FileChannel fileChannel2 = fileOutputStream.getChannel();


        // 创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true) {
            // 这里有个重要操作
            byteBuffer.clear(); //清空byteBuffer
            int read = fileChannel1.read(byteBuffer);
            if (read == -1) {
                break;
            }
            // 将byteBuffer 中的数据写到channel2 ---- file02.txt
            byteBuffer.flip();
            fileChannel2.write(byteBuffer);
        }

        // 将字节数据转出String输出
        System.out.println(new String(byteBuffer.array()));

        fileInputStream.close();
        fileOutputStream.close();

    }
}
