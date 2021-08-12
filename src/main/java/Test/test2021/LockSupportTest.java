package Test.test2021;

import java.text.MessageFormat;
import java.util.concurrent.locks.LockSupport;

/*
 Thread.State
WAITING状态是：“一个线程在等待另一个线程执行一个动作时在这个状态”
IMED_WAITING状态为：“一个线程在一个特定的等待时间内等待另一个线程完成一个动作会在这个状态”
 */
public class LockSupportTest {


    private Thread thread1 = null;
    private Thread thread2 = null;

    public void test() {
//        threadBlock();
        threadInterrupted();
//        Thread.State
    }

    private void threadBlock() {
        try {

            thread1 = new Thread(() ->
            {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(300);
                        if (i == 3) {
                            //线程阻塞:Blocked
                            LockSupport.park();
                        }
                        System.out.println(MessageFormat.format("Thread - {0}", Thread.currentThread().getId()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread2 = new Thread(() ->
            {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (thread1 != null) {
                    //激活线程，继续执行
                    LockSupport.unpark(thread1);
                }
            });
            thread1.start();
            thread2.start();
        } catch (Exception ex) {
            int n = 0;
        }

    }

    private volatile boolean isInterrupted = false;

    /*
    终止掉线程
    设置线程中断不影响线程的继续执行
     */
    private void threadInterrupted() {
        try {

            thread1 = new Thread(() ->
            {
                for (int i = 0; i < 10; i++) {
                    try {

                        if (!Thread.currentThread().isInterrupted()) {
                            //i==4时
                            // sleep interrupted：此时thread2 调用  thread1.interrupt(); thread1在执行sleep（）,就会sleep interrupted
                            //如果线程设置interrupt，调用sleep会抛异常 sleep interrupted
                            Thread.sleep(300);//sleep interrupted
                            System.out.println(MessageFormat.format("Thread - {0} running {1}", Thread.currentThread().getId(), i));
                        }
                        if (isInterrupted) {
                            thread1.interrupt();//设置线程中断不影响线程的继续执行
                            boolean re = Thread.currentThread().isInterrupted();//true
                            int m = 0;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("thread1 done.");
            });

            thread2 = new Thread(() ->
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (thread1 != null) {
                    System.out.println("thread1  interrupt ");
                    //终止掉线程
                    //设计一个变量通知线程thread1自己设置自己中断
                    isInterrupted = true;
                }
            });
            thread1.start();
            thread2.start();
        } catch (Exception ex) {
            int n = 0;
        }
    }


}
