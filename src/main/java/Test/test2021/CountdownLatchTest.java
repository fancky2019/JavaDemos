package Test.test2021;

import Test.test2020.StopwatchTest;
import com.google.common.base.Stopwatch;

import java.text.MessageFormat;
import java.util.concurrent.*;


/*
并发执行所有任务，等待任务执行完。
无法顺序执行任务  可用 notify  await. Semaphore --参见SemaphoreTest
它只保证任务执行完。任务同时执行，直到执行完。
 */
public class CountdownLatchTest {
    public void test() throws InterruptedException {
        fun1();
    }

    private void fun1() throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();
        final CountDownLatch latch = new CountDownLatch(3);
        Stopwatch stopwatch = Stopwatch.createStarted();
        long time1 = System.currentTimeMillis();

        for (int i = 0; i < 3; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("子线程 - " + Thread.currentThread().getName() + " 开始执行");
                        //(long) (Math.random() * 5000)
                        Thread.sleep(5000);
                        System.out.println("子线程 - " + Thread.currentThread().getName() + " 执行完成");
                        latch.countDown();//当前线程调用此方法，则计数减一
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            //调用线程池执行任务。线程池内部执行任务的顺序不定
            service.execute(runnable);
        }

        try {
            Thread.sleep(100);//确保子线程启动
            System.out.println("主线程 - " + Thread.currentThread().getName() + " 等待子线程执行完成...");
            latch.await();//阻塞当前线程，直到计数器的值为0
            System.out.println("主线程 - " + Thread.currentThread().getName() + " 开始执行完成...");
            stopwatch.stop();
            //99 milliSeconds
            System.out.println(MessageFormat.format("{0} milliSeconds", stopwatch.elapsed(TimeUnit.MILLISECONDS)));
//            stopwatch.reset();//不重置时间会累加。
//            stopwatch.start();


            long time2 = System.currentTimeMillis();

            long time12 = time2 - time1;
            System.out.println(time12);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
