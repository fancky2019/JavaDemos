package Test.test2021;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;

/**
 * AtomicIntegerTest cas demo2019
 * 进程：是系统进行资源分配和调度的基本单位。进程是线程的容器。进程是程序的实体
 * 线程：操作系统能够进行运算调度的最小单位。
 * 线程id 在一台操作系统内是唯一的。线程标识符永远不会是0。
 * <p>
 * 一旦所涉及的线程和进程终止，操作系统可以重用进程ID和线程ID。
 */
public class ThreadTest {

    private static final Logger LOGGER = LogManager.getLogger();

    /*
     线程终止：Thread.currentThread().isInterrupted();//true   //终止掉线程
                    //设计一个变量通知线程thread1自己设置自己中断
                    isInterrupted = true;
     线程阻塞：LockSupport.unpark()、 LockSupport.park() block
     线程运行完成：join

    线程同步、线程间通信：synchronized 、ReentrantLock await signal、object的wait notify 、semaphore.acquire()release()、cas
     */
    public void test() {
//        joinTest();

//        threadPoolExceptionTest();
        threadExceptionTestFun();
    }

    /*
    如果主线程代码执行完，此时子线程抛出异常，主线程是无法捕获的
     */
    private void threadPoolExceptionTest() {

        try {
            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() ->
            {
                //必须在线程内部进行异常处理，无法抛出到外边的另外一个线程。和C#一样
//                Integer m = Integer.parseInt("m");
                try {
                    Integer m = Integer.parseInt("m");
                } catch (Exception e) {
                    System.out.println("Thead 内部:" + e.getMessage());
                    LOGGER.error("", e);//用此重载，打印异常
                    throw e;
                }
                return 1;
            });

            // get 内部还是通过 LockSupport.unpark()、 LockSupport.park() block 当前线程。知道线程完成
            //外部主线程已经执行完，如果内部线程抛异常将不会进入外部线程
//            completableFuture.get();
        } catch (Exception e) {
            //外层捕获，没有进入catch
            System.out.println("Thead 外部:" + e.getMessage());
            int m = 0;
        }


        System.out.println("threadPoolExceptionTest Completed");
    }


    private void threadExceptionTestFun() {

        try {
            Thread thread = new Thread(() ->
            {
                int i = 0;
                while (true) {
                    System.out.println(++i);
                    //必须在线程内部进行异常处理，可能无法抛出到外边的另外一个线程。和C#一样
//                    Integer m = Integer.parseInt("m");
                    try {
                        Integer m = Integer.parseInt("m");
                    } catch (Exception e) {
//                       e.printStackTrace();
                        LOGGER.error("", e);//用此重载，打印异常
                        System.out.println("Thead 内部:" + e.getMessage());
                        //主线程设置捕获子线程的异常
//                        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->{  });
//                       throw e;//如果主线程的代码已经执行完是捕捉不到子线程抛出的异常
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {

                    }
                }
            });

            //外部主线程已经执行完，如果内部线程抛异常将不会进入外部线程
            thread.start();
        } catch (Exception e) {
            //外层捕获，没有进入catch
            System.out.println("Thead 外部:" + e.getMessage());
            int m = 0;
        }


        System.out.println("threadExceptionTestFun Completed");
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
//        Thread.currentThread().getId();
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
