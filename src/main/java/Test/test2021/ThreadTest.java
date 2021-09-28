package Test.test2021;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;

/**
 * AtomicIntegerTest cas demo2019
 */
public class ThreadTest {

    /*
     线程终止：Thread.currentThread().isInterrupted();//true   //终止掉线程
                    //设计一个变量通知线程thread1自己设置自己中断
                    isInterrupted = true;
     线程阻塞：LockSupport.unpark()、 LockSupport.park() block
     线程运行完成：join

    线程同步、线程间通信：synchronized 、ReentrantLock await signal、object的wait notify 、semaphore.acquire()release()、cas
     */
    public void test() {
        joinTest();
//        threadExceptionTest();
    }

    private void threadExceptionTest() {

        try {
            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() ->
            {
                //必须在线程内部进行异常处理，无法抛出到外边的另外一个线程。和C#一样
                Integer m = Integer.parseInt("m");
//                try {
//                    Integer m = Integer.parseInt("m");
//                } catch (Exception e) {
//                   System.out.println("Thead 内部:"+e.getMessage());
//                }
                return 1;
            });

            // get 内部还是通过 LockSupport.unpark()、 LockSupport.park() block 当前线程。知道线程完成
            completableFuture.get();
        } catch (Exception e) {
            //外层捕获，没有进入catch
            System.out.println("Thead 外部:" + e.getMessage());
            int m = 0;
        }


        System.out.println("Completed");
    }

    //region join 阻塞直到指定时间或线程执行完成
    Thread threadJoin = null;

    private void joinTest() {
        try {
            joinFun();
            Thread.sleep(1000);
            //join 阻塞：内部调用object 的wait方法
            threadJoin.join(0);//Waits at most millis milliseconds for this thread to die. A timeout of 0 means to wait forever.
            //此时线程已终止
            boolean isAlive = threadJoin.isAlive();//false
            Thread.State state = threadJoin.getState();//TERMINATED
            //threadJoin.wait();不建议用线程类继承object 的wait等方法
            System.out.println(MessageFormat.format("Thread - {0} joinTest done!", Thread.currentThread().getId()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void joinFun() {
        threadJoin = new Thread(() ->
        {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(MessageFormat.format("Thread - {0} threadJoin done!", Thread.currentThread().getId()));
        });
        threadJoin.start();
    }

    //endregion
}
