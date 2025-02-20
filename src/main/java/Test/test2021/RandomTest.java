package Test.test2021;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 线程局部变量：ThreadLocalRandom 使用 ThreadLocal 机制，每个线程都有自己独立的随机数生成器实例，因此不需要同步操作，避免了多线程竞争。
 *
 * 性能：由于每个线程都有自己的随机数生成器，ThreadLocalRandom 在多线程环境下的性能比 Random 更好。
 *
 * 使用简单：ThreadLocalRandom 的实例通过静态方法 current() 获取，不需要显式创建。
 */
public class RandomTest {
    public void test() {
        fun();
    }

    private void fun() {
//        Random random = new Random(1);
        Random random = new Random(); //如果设置参数1每次都一样的值
        //(0,10000] 左开右闭
        int r1 = random.nextInt(10000);
        //(0,10000] 左开右闭
        int r2 = random.nextInt(10000);
        int n = 0;

//        [0,9)
        ThreadLocalRandom.current().nextInt(1,9);

        //[100,2000)
        long expireTime = ThreadLocalRandom.current().nextInt(100,2000);

    }
}
