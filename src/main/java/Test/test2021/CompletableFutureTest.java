package Test.test2021;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * ForkJoinPool 的每个工作线程都维护着一个工作队列（WorkQueue），这是一个双端队列（Deque），
 * 里面存放的对象是任务（ForkJoinTask）
 */
public class CompletableFutureTest {
    public void test() {
//        fun();
//        batchTask();
        allOfTest();
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

    /**
     * allOf  anyOf
     */
    private  void allOfTest()
    {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() - 1,
                Runtime.getRuntime().availableProcessors() * 2,
                6000,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100));


        //    推荐使用：   1、 使用 allOf() 和 join()

        // 等待所有任务完成
        List<CompletableFuture<String>> futures1 = Arrays.asList(
                CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "结果11";
                }, executor),
                CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "结果22";
                }, executor),
                CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "结果33";
                }, executor)
        );

        //allDone 只提供完成信号，不包含结果 .Void 返回类型。allOf完成在thenApply 获取结果
        CompletableFuture<Void> allDone = CompletableFuture.allOf(
                futures1.toArray(new CompletableFuture[0])
        );


        //在 allDone.thenApply 回调中：
        //
        //get() join() 两种方式性能相同：都只是读取已完成的结果
        //没有阻塞开销
        //执行时间差异可以忽略不计
//        allDone.thenApply 中的行为
//        两种方式都不会阻塞，因为：
//
//        allDone 已经确保所有future都完成了
//
//        无论是 get() 还是 join() 都只是读取已存在的结果
//            执行速度几乎没有区别


        // get() join() 在所有任务完成后提取结果.allDone 的回调thenApply 中获取结果
        CompletableFuture<List<String>> resultsFuture1 = allDone.thenApply(v -> {
            // 这里的join()不会阻塞，因为所有任务都已经完成
            return futures1.stream()
                    .map(CompletableFuture::join)  //get() join()  立即获取结果 // 从每个future中提取结果值
                    .collect(Collectors.toList());

            //// 使用join()，无需异常处理
//            return futures1.stream()
//                    .map(p-> {
//                        try {
//                            return p.get();
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        } catch (ExecutionException e) {
//                            throw new RuntimeException(e);
//                        }
//                    })  // 立即获取结果 // 从每个future中提取结果值
//                    .collect(Collectors.toList());
        });

        // 获取最终结果（这里会阻塞直到所有任务完成）
        List<String> results1 = resultsFuture1.join();



//        2、使用 Stream API 和 join() .使用stream 不适用allof
        List<CompletableFuture<String>> futures = Arrays.asList(
                CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "结果1";
                }, executor),
                CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "结果2";
                }, executor),
                CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "结果3";
                }, executor)
        );

        /*

         join() 会阻塞直到任务完成

        join 与 get() 的区别
        特性	join()	get()
        异常处理	抛出未经检查的CompletionException	抛出受检查的ExecutionException, InterruptedException
        代码简洁性	更简洁，不需要try-catch	   需要处理受检异常
        使用场景	通常用于Lambda表达式和Stream中	   需要精确异常处理时
         */

        // 等待所有任务完成并收集结果
        List<String> results = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());


        int m=0;
    }
}
