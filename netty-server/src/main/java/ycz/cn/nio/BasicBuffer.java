package ycz.cn.nio;

import java.nio.IntBuffer;

public class BasicBuffer {

    public static void main(String[] args) {

        // 创建一个Buffer,大小为5，即可存放5个Int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        // 存放数据
        intBuffer.put(10);
        intBuffer.put(11);
        intBuffer.put(12);
        intBuffer.put(13);
        intBuffer.put(14);

        // 读写切换 ！！
        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
