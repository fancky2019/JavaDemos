package Test.test2021;

import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * ForkJoinPool 的每个工作线程都维护着一个工作队列（WorkQueue），这是一个双端队列（Deque），
 * 里面存放的对象是任务（ForkJoinTask）
 */
public class CompletableFutureTest {
    public void test() {
//        fun();
        batchTask();
    }

    private  int m=0;
    private synchronized int  add()
    {
        ++m;
        return m;
    }
    private void batchTask() {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() - 1,
                Runtime.getRuntime().availableProcessors() * 2,
                6000,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100));
//        executor.execute();
        for (int i = 0; i < 2000; i++) {


            //使用线程池 超过队列长度拒绝任务
//            CompletableFuture.supplyAsync(() ->
//            {
//                try {
//                   int m= add();
//                    System.out.println(m);
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                return 3;
//            }, executor);

            //使用默认的ForkJoinPool 线程数：核心数-1，ForkJoinTask 任务队列最大长度8192
            // throw new RejectedExecutionException("Queue capacity exceeded");
            //测试下来没有拒绝
            CompletableFuture.supplyAsync(() ->
            {
                try {
                    int m= add();
                    System.out.println(m);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return 3;
            });
        }
    }

    private void fun() {
        /**CompletableFuture 内部默认最多启动处理器个数 -1 个线程执行任务。
         private static final Executor asyncPool = useCommonPool ?
         ForkJoinPool.commonPool() : new CompletableFuture.ThreadPerTaskExecutor();
         */
//        ExecutorService service = Executors.newCachedThreadPool();

        try {

//            ThreadPoolExecutor executor = new ThreadPoolExecutor(
//                    Runtime.getRuntime().availableProcessors() - 1,
//                    Runtime.getRuntime().availableProcessors() * 2,
//                    6000,
//                    TimeUnit.MILLISECONDS,
//                    new ArrayBlockingQueue<>(1000));
//            CompletableFuture.supplyAsync(() ->
//            {
//                return 3;
//            }, executor);

            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() ->
            {
                //可以把whenComplete放在里面
                return 3;
            });

            int re = completableFuture.get();

            CompletableFuture.runAsync(() ->
            {

            }).whenComplete((a, b) ->
            {
                Object aa = a;
                Object bb = b;
            });

            try {


                BiConsumer<String, Throwable> biConsumer = (a, b) ->
                {
                    int result = Integer.parseInt(a);
                };
                // Exception ex = new Exception("sddssd");
                //跟C#不一样biConsumer（3,4），java 函数接口只是一个函数接口（可能有多个方法）要指定调用方法
                //   biConsumer.accept("sd", ex);


                CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(() ->
                {
                    return "t";
                });

                completableFuture1.whenComplete(biConsumer);

                completableFuture1.exceptionally(ex ->
                {
                    String str = ex.getMessage();
                    return "";
                });
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }


            try {

                Consumer<String> consumer = a ->
                {
                    int result = Integer.parseInt(a);
                };
                //跟C#不一样biConsumer（3,4），java 函数接口只是一个函数接口（可能有多个方法）要指定调用方法
                consumer.accept("m");

            } catch (Exception ex) {
                System.out.println(ex.toString());
            }


            CompletableFuture.supplyAsync(() ->
            {
                return 3;
            }).whenComplete((a, b) ->    //a=3,b=null
            {
                Object aa = a;
                Object bb = b;
            });


            //thenAccept 返回 CompletableFuture<Void>
            CompletableFuture<Void> completableFuture1 = CompletableFuture.supplyAsync(() ->
            {
                return "t";
            }).thenAccept(System.out::println);

        } catch (Exception ex) {

        }
    }
}
