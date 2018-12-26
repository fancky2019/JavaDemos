package Test;

import com.sun.xml.internal.ws.util.CompletedFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest {


    public void declareStartThread() {

        Thread thread = new Thread(() ->
        {

            doWork();
        });
        thread.start();
    }

    private void doWork() {
        Lock lock = new ReentrantLock();
        try {
            lock.lock();
            System.out.println("start()");
            Thread.sleep(200);
        } catch (Exception ex) {

        } finally {
            lock.unlock();
        }
    }

    public synchronized void test() {
        try {

            // futureTask();
            CompletableFuture<Integer> completableFuture = completableFutureDemo();
            Thread.sleep(11 * 1000);

            Integer m = completableFuture.get();
            Integer n = 1;
            //whenComplete();
        } catch (Exception ex) {
            String str = ex.getMessage();
            Integer m = 0;
        }

        // threadPool();

    }

    private synchronized void synchronizedTest() {
        synchronized (ThreadTest.class) {
            float f = 3.4f;
            double d = 3.4;
        }
    }

    public void threadPool() throws Exception {

        // 4个线程池差异看源码ThreadPoolExecutor实例的参数
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() ->
        {
            doWork();
        });


        //定长线程个数
        ExecutorService SynchronousQueue1 = Executors.newFixedThreadPool(10);
        //队列
        Executors.newSingleThreadExecutor();
        //创建固定10个核心线程
        Executors.newScheduledThreadPool(10);
    }

    private void futureTask() throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        //C#里的IAsyncResult 轮询执行原理一样
        Future<Integer> future = executorService.submit(() ->
        {
            Thread.sleep(5 * 1000);
            return 1;
        });
        while (true) {
            if (future.isDone()) {
                Integer re = future.get();
                break;
            }
        }
    }

    private CompletableFuture<Integer> completableFutureDemo() {
        try {
            //没有返回值
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() ->
            {
            });
            //有返回值
            //     CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() ->
            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() ->
            {
                try {
                    Thread.sleep(10 * 1000);

                    return 1;
                } catch (Exception ex) {
                    return 0;
                }

            }).thenApply(s ->
            {

                return 11;
            });
            //如果没有执行完成，会返回complete的参数作为返回值
            // completableFuture.complete(2);
            //回去返回值，回等待线程执行完成
            //   Integer result = completableFuture.get();

            Integer m = 0;


            Thread.currentThread().getName();
            System.currentTimeMillis();
            return completableFuture;
            //  CompletableFuture<Integer> c=new CompletableFuture<Integer>();
        }
//        catch (InterruptedException ex) {
//
//        }
        catch (Exception ex) {
            String str = ex.getMessage();
            return null;
        }
    }


    public void whenComplete() {
        String threadName = Thread.currentThread().getName();
        Thread.currentThread().getId();
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                String threadName1 = Thread.currentThread().getName();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (1 == 1) {
                throw new RuntimeException("测试一下异常情况");
            }
            return "s1";
        }).whenComplete((s, t) -> {
            System.out.println(s);
            System.out.println(t.getMessage());
        }).exceptionally(e -> {
            System.out.println(e.getMessage());
            return "hello world";
        }).join();
        System.out.println(result);
    }

}

