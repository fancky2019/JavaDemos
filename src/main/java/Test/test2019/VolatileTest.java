package Test.test2019;

import java.io.Console;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class VolatileTest {

    public void test() {
        volatileTest();
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

        Executors.newCachedThreadPool().submit(new FutureTask<>( ()->
        {
            return "futureTasks";
        }));
        Runnable runnable= ()->
        {
            System.out.println("runnables");
        };
        Executors.newCachedThreadPool().submit(runnable);

        Executors.newCachedThreadPool().submit(()->
        {
            System.out.println("runnables");
        });

        CompletableFuture.supplyAsync(()->
        {
            return  1;
        }).thenApply(r->
        {
            return  r+3;
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
}
