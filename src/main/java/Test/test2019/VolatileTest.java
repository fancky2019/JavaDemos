package Test.test2019;

import java.beans.Statement;
import java.io.Console;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
volatile 只能保证对单次读/写的原子性。i++ 这种操作不能保证原子性。
如果你有多个线程对变量写入，volatile 无法解决并发的问题，并发必须使用 synchronized 来防止竞争条件。
原子性可以应用于除 long 和 double 之外的所有基本类型之上的 “简单操作”。

并发特征：原子性 可见性 有序性。volatile 不保证原子性。内存屏障防止cpu优化的指令重排保证有序性。强制刷新到主存可见性。



volatile  内存屏障 ， happen before 原则 读之前要等所有写操作要完成。强制从cpu缓存中把数据刷新到主内存
主内存线程间数据共享，
 */
public class VolatileTest {

    public void test() {

//        volatileTest();
//        unAtomic();
        readWrite();
    }


    private volatile int volParam = 0;

    private void volatileTest() {
        ExecutorService executorService = Executors.newCachedThreadPool();


        List<Callable<String>> callables = new LinkedList<>();
        //  List<Callable<Void>> tasks1=new LinkedList<>();
        List<Runnable> runnables = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            callables.add(() ->
            {
                try {
                    //确保for 循环内的线程执行完
                    Thread.sleep(2000);
                } catch (Exception ex) {
                    int mmm = 0;
                }
                return String.valueOf(++volParam);
            });

        }
        try {
//            Thread.sleep(1000);
            //所有的任务完成前将阻塞该调用线程。
            List<Future<String>> futures = executorService.invokeAll(callables);
            for (Future future : futures) {
                System.out.println(future.get());
            }
        } catch (Exception ex) {
            int mmm = 0;
        }
        System.out.println(volParam);
    }

    /*
    submit(Callable<T> task) 内部实现：
    *public class FutureTask<V> implements RunnableFuture<V>
    public interface RunnableFuture<V> extends Runnable, Future<V> {
    void run();
     }
     public interface ExecutorService extends Executor
     public abstract class AbstractExecutorService implements ExecutorService
     ThreadPoolExecutor 调用 父类 AbstractExecutorService 类实现接口ExecutorService的submit方法，
     submit内部调用 Executor接口的execute方法，execute 由 ThreadPoolExecutor类实现

      ThreadPoolExecutor的方法execute(Runnable command) 调用  addWorker(Runnable firstTask, boolean core)将Runnable
       添加到worker 队列，addWorker内启动线程将Runnable 传给内部线程，启动执行。

      FutureTask<V> implements RunnableFuture<V>  实现  RunnableFuture<V>接口的run 方法
      run 方法内部调用 Callable<T> 的call实现,并调用set(V v) 方法把执行结果赋值给全局变量。

     */
    private void fun3() {
        //extends RunnableFuture<V> extends Runnable, Future<V>
        FutureTask<Void> future = new FutureTask<Void>(() ->
        {
            System.out.println("Future<Void>");
            return null;
        });
        Executors.newCachedThreadPool().submit(future);

        //  ExecutorService 提供批量调用Callables ，只能单个Runnable
//        List<FutureTask<String>> futureTasks=new LinkedList<>();
//        futureTasks.add(new FutureTask<>( ()->
//        {
//            return "futureTasks";
//        }));
//        Executors.newCachedThreadPool().invokeAll(futureTasks);

        Executors.newCachedThreadPool().submit(new FutureTask<>(() ->
        {
            return "futureTasks";
        }));
        Runnable runnable = () ->
        {
            System.out.println("runnables");
        };
        Executors.newCachedThreadPool().submit(runnable);

        Executors.newCachedThreadPool().submit(() ->
        {
            System.out.println("runnables");
        });

        CompletableFuture.supplyAsync(() ->
        {
            return 1;
        }).thenApply(r ->
        {
            return r + 3;
        });
    }

    private void unFinish() {
        for (int i = 0; i < 10; i++) {
            new Thread(() ->
            {
                volParam++;
            }).start();
        }
        //for 循环内的线程可能未执行完volParam可能不等于10；
        try {
            //确保or 循环内的线程执行完
            Thread.sleep(1000);
        } catch (Exception ex) {

        }
        System.out.println(volParam);
    }


    /*
             //volatile不支持原子操作
                    //_volParam 取值、++、赋值三个操作，不是原子操作。
                    //因此此结果可能不是600000。因为多个线程取值可能都是没++之后的值。
     */
    private void unAtomic() {
        AtomicInteger atomicInteger = new AtomicInteger();


        Thread thread1 = new Thread(() ->
        {
            for (int j = 0; j < 100000; j++) {
                volParam++;
                atomicInteger.incrementAndGet();
            }

        });
        Thread thread2 = new Thread(() ->
        {
            for (int j = 0; j < 100000; j++) {
                volParam++;
                atomicInteger.incrementAndGet();
            }

        });
        Thread thread3 = new Thread(() ->
        {
            for (int j = 0; j < 100000; j++) {
                volParam++;
                atomicInteger.incrementAndGet();
            }

        });
        Thread thread4 = new Thread(() ->
        {
            for (int j = 0; j < 100000; j++) {
                volParam++;
                atomicInteger.incrementAndGet();
            }

        });
        Thread thread5 = new Thread(() ->
        {
            for (int j = 0; j < 100000; j++) {
                volParam++;
                atomicInteger.incrementAndGet();
            }

        });
        Thread thread6 = new Thread(() ->
        {
            for (int j = 0; j < 100000; j++) {
                volParam++;
                atomicInteger.incrementAndGet();
            }

        });
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();

        while (thread1.getState() != Thread.State.TERMINATED ||
                thread2.getState() != Thread.State.TERMINATED ||
                thread3.getState() != Thread.State.TERMINATED ||
                thread4.getState() != Thread.State.TERMINATED ||
                thread5.getState() != Thread.State.TERMINATED ||
                thread6.getState() != Thread.State.TERMINATED) {

        }
        //存在并发问题，由于不支持原子操作所以可能_volParam!=600000
        System.out.println(volParam);
        //for 循环内的线程可能未执行完volParam可能不等于10；
        try {
            //确保or 循环内的线程执行完
            Thread.sleep(2000);

//            Integer integer=0;
//            AtomicInteger atomicInteger=new AtomicInteger(0);
//            atomicInteger.incrementAndGet();


            thread1.isAlive();
        } catch (Exception ex) {

        }
        System.out.println(volParam);
    }


    volatile List<Integer> list = new ArrayList<>();


    private void readWrite() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        CompletableFuture.runAsync(() ->
        {
            synchronized (list) {
                try {
                    list.set(1, 20);
                    Thread.sleep(5000);
                    list.set(2, 30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        CompletableFuture.runAsync(() ->
        {
            try {
                Thread.sleep(2000);
                System.out.println("before set list[1]=" + list.get(1));
                Thread.sleep(5000);
                System.out.println("after set list[2]=" + list.get(2));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


}
