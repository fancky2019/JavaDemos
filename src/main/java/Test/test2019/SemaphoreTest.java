package Test.test2019;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    public void test() {
        fun();
    }

    private void fun() {
        // permits：允许同时运行的任务数
        //fair:FIFO
        Semaphore semaphore = new Semaphore(3, true);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        //可用返回处理器个数。
        int ir = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < 10; i++) {
            //CompletableFuture 内部默认最多启动处理器个数 -1 个线程执行任务。
            // 指定Executor， CompletableFuture 内部创建 Integer.MAX_VALUE个线程
            CompletableFuture.runAsync(() ->
            {
                try {
                    System.out.println(MessageFormat.format("{0} Thread - {1} is waiting .", LocalDateTime.now().format(dtf), Thread.currentThread().getId()));
                    //申请不到许可，将一直阻塞
                    semaphore.acquire();
                    System.out.println(MessageFormat.format("{0} Thread - {0} enter .", LocalDateTime.now().format(dtf), Thread.currentThread().getId()));
                    Thread.sleep(5000);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    System.out.println(MessageFormat.format("{0} Thread - {0} releases permit .", LocalDateTime.now().format(dtf), Thread.currentThread().getId()));
                    semaphore.release();
                }
            }, Executors.newCachedThreadPool());

//            Executors.newCachedThreadPool().submit(() ->
//            {
//                try {
//                    System.out.println(MessageFormat.format("{0} Thread - {1} is waiting .", LocalDateTime.now().format(dtf), Thread.currentThread().getId()));
//                    //申请不到许可，将一直阻塞
//                    semaphore.acquire();
//                    System.out.println(MessageFormat.format("{0} Thread - {1} enter .", LocalDateTime.now().format(dtf), Thread.currentThread().getId()));
//                    Thread.sleep(5000);
//                } catch (Exception ex) {
//                    System.out.println(ex.getMessage());
//                } finally {
//                    System.out.println(MessageFormat.format("{0} Thread - {1} released permit .", LocalDateTime.now().format(dtf), Thread.currentThread().getId()));
//                    semaphore.release();
//                }
//            });

//            new Thread(()->
//            {
//                try {
//                    System.out.println(MessageFormat.format("{0} Thread - {1} is waiting .", LocalDateTime.now().format(dtf), Thread.currentThread().getId()));
//                    //申请不到许可，将一直阻塞
//                    semaphore.acquire();
//                    System.out.println(MessageFormat.format("{0} Thread - {1} enter .",LocalDateTime.now().format(dtf),Thread.currentThread().getId()));
//                    Thread.sleep(5000);
//                } catch (Exception ex) {
//                    System.out.println(ex.getMessage());
//                } finally {
//                    System.out.println(MessageFormat.format("{0} Thread - {1} released permit .",LocalDateTime.now().format(dtf),Thread.currentThread().getId()));
//                    semaphore.release();
//                }
//            }).start();
        }
    }


}
