package Test.test2019;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {
    public void test() {
        AtomicInteger atomicInteger1 = new AtomicInteger(0);

        boolean re = atomicInteger1.compareAndSet(0, 1);
        atomicInteger1.compareAndSet(1, 0);
        atomicInteger1.incrementAndGet();
        atomicInteger1.decrementAndGet();
        fun();
    }

    private void fun() {

        for (int i = 0; i < 10; i++) {
            Executors.newCachedThreadPool().submit(() ->
            {
                doWork();
            });
//            new Thread(() ->
//            {
//                doWork();
//            }).start();
        }
    }

    private int m = 0;
    AtomicInteger atomicInteger = new AtomicInteger(0);

    private void doWork() {
        //获取锁之前一直自旋,实现自旋锁。
        while (!acquire()) {
            try {
//                Thread.sleep(1);
            } catch (Exception ex) {

            }
        }
        System.out.println(m++);
        try {
            Thread.sleep(200);
        } catch (Exception ex) {

        }
        release();


    }

    /**
     * 加锁逻辑
     *
     * @return 是否获取锁
     */
    private boolean acquire() {
        return atomicInteger.compareAndSet(0, 1);
    }

    /**
     * 释放锁
     */
    private void release() {
//        atomicInteger.compareAndSet(1, 0);
// 其实不用比较，此方法只有在获取锁的块内调用
        atomicInteger.set(0);
    }
}
