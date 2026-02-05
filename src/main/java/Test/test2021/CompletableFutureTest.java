package Test.test2021;

import Test.test2025.ThreadFactoryImpl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * ForkJoinPool 的每个工作线程都维护着一个工作队列（WorkQueue），这是一个双端队列（Deque），
 * 里面存放的对象是任务（ForkJoinTask）
 *
 * 根据任务类型选择线程池
 * IO密集型：可以创建更多线程。线程池过多确实不能提高性能，反而会降低性能
 * CPU密集型： 线程数 ≈ CPU核心数
 *
 * // 错误的默认配置：
 *     // ForkJoinPool.commonPool()默认线程数 = CPU核心数-1
 *     // 对于IO任务来说严重不足！
 *
 * IO等待期间CPU是空闲的！
 *线程在IO等待时会被操作系统挂起！
 *  // 1. 线程运行状态（占用CPU）
 *         // 2. 遇到阻塞调用（如Thread.sleep、socket.read）
 *         // 3. 操作系统挂起该线程，释放CPU
 *         // 4. CPU调度其他就绪线程
 *         // 5. IO完成，线程恢复就绪状态
 *         // 6. 等待CPU调度执行
 *
 *微服务和分布式系统设计的核心原则，需要资源隔离的业务线程
 *1. 防止级联故障（最重要的原因）。整个链路崩溃
 *2. 不同业务的不同SLA要求。SLA（Service Level Agreement）SLA = 服务等级协议
 *
 *
 */
public class CompletableFutureTest {
    public void test() {
//        fun();
//        batchTask();
//        allOfTest();
//        thenApplyTest();
//        thenAcceptTest();
//        thenCombineTest();
//        whenCompleteTest();
//        joinGetTest();
        completableFutureStatusTest();
    }

    private int m = 0;

    private synchronized int add() {
        ++m;
        return m;
    }

    private void batchTask() {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() - 1,
                Runtime.getRuntime().availableProcessors() * 2,
                6000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadFactoryImpl("ThreadTest"),
                new ThreadPoolExecutor.AbortPolicy());
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
            CompletableFuture.supplyAsync(() -> {
                try {
                    int m = add();
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
            //有返回值
            CompletableFuture<String> completableFutureSupply = CompletableFuture.supplyAsync(() -> {
                return "t";
            });
            //没有返回值
            CompletableFuture<Void> completableFutureRunnable = CompletableFuture.runAsync(() -> {

            });


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

            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
                //可以把whenComplete放在里面
                return 3;
            });

            int re = completableFuture.get();

            CompletableFuture.runAsync(() -> {

            }).whenComplete((a, b) -> {
                Object aa = a;
                Object bb = b;
            });

            try {


                BiConsumer<String, Throwable> biConsumer = (a, b) -> {
                    int result = Integer.parseInt(a);
                };
                // Exception ex = new Exception("sddssd");
                //跟C#不一样biConsumer（3,4），java 函数接口只是一个函数接口（可能有多个方法）要指定调用方法
                //   biConsumer.accept("sd", ex);


                CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(() -> {
                    return "t";
                });

                completableFuture1.whenComplete(biConsumer);

                completableFuture1.exceptionally(ex -> {
                    String str = ex.getMessage();
                    return "";
                });
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }


            try {

                Consumer<String> consumer = a -> {
                    int result = Integer.parseInt(a);
                };
                //跟C#不一样biConsumer（3,4），java 函数接口只是一个函数接口（可能有多个方法）要指定调用方法
                consumer.accept("m");

            } catch (Exception ex) {
                System.out.println(ex.toString());
            }


            CompletableFuture.supplyAsync(() -> {
                return 3;
            }).whenComplete((a, b) ->    //a=3,b=null
            {
                Object aa = a;
                Object bb = b;
            });


            //thenAccept 返回 CompletableFuture<Void>
            CompletableFuture<Void> completableFuture1 = CompletableFuture.supplyAsync(() -> {
                return "t";
            }).thenAccept(System.out::println);

        } catch (Exception ex) {

        }
    }

    /**
     * allOf  ：等待所有任务都完成
     * anyOf
     */
    private void allOfTest() {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() - 1,
                Runtime.getRuntime().availableProcessors() * 2,
                6000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadFactoryImpl("ThreadTest"),
                new ThreadPoolExecutor.AbortPolicy());

        //    推荐使用：   1、 使用 allOf() 和 join()

        // 等待所有任务完成
        List<CompletableFuture<String>> futureList = Arrays.asList(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "结果11";
        }, executor), CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "结果22";
        }, executor), CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "结果33";
        }, executor));

        //allDone 只提供完成信号，不包含结果 .Void 返回类型。allOf完成在thenApply 获取结果
        CompletableFuture<Void> allDone = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));

        //所有任务都完成执行Runnable
        allDone.thenRun(() -> {
            String result0 = futureList.get(0).join();
            String result1 = futureList.get(1).join();
            String result2 = futureList.get(2).join();
            int n = 0;
        });
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


        //所有任务都完成执行Function
        // get() join() 在所有任务完成后提取结果.allDone 的回调thenApply 中获取结果
        //thenApply:转换结果
        CompletableFuture<List<String>> resultsFuture1 = allDone.thenApply(v -> {
            // 这里的join()不会阻塞，因为所有任务都已经完成
            return futureList.stream().map(CompletableFuture::join)  //获取CompletableFuture返回结果 get() join()  立即获取结果 // 从每个future中提取结果值
                    .collect(Collectors.toList());

            //// 使用join()，无需异常处理join 方法没有抛出异常.使用get 要强制要求异常处理，get 方法抛出了异常
//            return futureList.stream()
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
        List<CompletableFuture<String>> futures = Arrays.asList(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "结果1";
        }, executor), CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "结果2";
        }, executor), CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "结果3";
        }, executor));

        /*

         join() 会阻塞直到任务完成

        join 与 get() 的区别
        特性	join()	                            get()
        异常处理	抛出未经检查的CompletionException	抛出受检查的ExecutionException, InterruptedException
        代码简洁性	更简洁，不需要try-catch	   需要处理受检异常
        使用场景	通常用于Lambda表达式和Stream中	   需要精确异常处理时
         */

        // 等待所有任务完成并收集结果
        List<String> results = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());


        int m = 0;
    }

    //完成之后执行一个 Function 函数接口。通常转换返回结果。：将返回结果作为 Function 的参数
    private void thenApplyTest() {
        //完成之后执行一个Function 函数接口
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() - 1,
                Runtime.getRuntime().availableProcessors() * 2,
                6000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadFactoryImpl("ThreadTest"),
                new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "11";
        }, executor);

        //等待获取结果
//        future1.join();
        //执行完：将返回结果作为 Function 的参数
        future1.thenApply((futureResult) -> {
            return futureResult;
        });


    }

    //完成之后执行一个 Consumer 函数接口。通常消费返回结果。将返回结果作为 Consumer 的参数
    private void thenAcceptTest() {

        //完成之后执行一个Function 函数接口
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() - 1,
                Runtime.getRuntime().availableProcessors() * 2,
                6000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadFactoryImpl("ThreadTest"),
                new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "结果11";
        }, executor);
        //执行完：将返回结果作为 Consumer 的参数
        future1.thenAccept((futureResult) -> {

            int n = 0;
        });
    }

    //完成之后执行一个 Runnable 函数接口。
    private void thenRunTest() {

        //完成之后执行一个Function 函数接口
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() - 1,
                Runtime.getRuntime().availableProcessors() * 2,
                6000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadFactoryImpl("ThreadTest"),
                new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "结果11";
        }, executor);
        //执行完：将返回结果作为 Consumer 的参数
        future1.thenRun(() -> {

            int n = 0;
        });
    }

    //thenCombine 等待两个都完成之后执行一个 BiFunction 函数接口
    private void thenCombineTest() {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() - 1,
                Runtime.getRuntime().availableProcessors() * 2,
                6000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadFactoryImpl("ThreadTest"),
                new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "结果11";
        }, executor);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "结果11";
        }, executor);
//        thenCombine 等待两个都完成之后执行一个 BiFunction 函数接口
        future1.thenCombine(future2, (future1Result, future2Result) -> {
            int n = 0;
            return future1Result.toString() + future1Result;
        });
    }


    //thenCombine 等待两个都完成之后执行一个 BiFunction 函数接口
    private void whenCompleteTest() {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() - 1,
                Runtime.getRuntime().availableProcessors() * 2,
                6000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadFactoryImpl("ThreadTest"),
                new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
//                int n = Integer.parseInt("n");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "结果11";
        }, executor);
        //whenComplete 内异常要处理掉，不能传播到主线程，thenApply/thenAccept join() get() 会传播到主线程。
        future1.whenComplete((futureResult, throwable) -> {
            if (throwable != null) {

                Throwable e = (Throwable) throwable;
                String msg = e.toString();
                String string = e.getStackTrace().toString();
                int n = 0;

            }
            int nn = 0;
        });
    }

    //thenCombine 等待两个都完成之后执行一个 BiFunction 函数接口
    private void handleTest() {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() - 1,
                Runtime.getRuntime().availableProcessors() * 2,
                6000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadFactoryImpl("ThreadTest"),
                new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
//                int n = Integer.parseInt("n");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "结果11";
        }, executor);
        //可以 try catch 捕获join  get 的异常
        //whenComplete 内异常要处理掉，不能传播到主线程，thenApply/thenAccept join() get() 会传播到主线程。
        future1.handle((futureResult, throwable) -> {
            if (throwable != null) {

                Throwable e = (Throwable) throwable;
                String msg = e.toString();
                String string = e.getStackTrace().toString();
                int n = 0;
                return "异常处理后的默认值";
            }
            int nn = 0;
            //
            return futureResult;
        });
    }

    private void joinGetTest() {

        try {


            ThreadPoolExecutor executor = new ThreadPoolExecutor(
                    Runtime.getRuntime().availableProcessors() - 1,
                    Runtime.getRuntime().availableProcessors() * 2,
                    6000, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(100),
                    new ThreadFactoryImpl("ThreadTest"),
                    new ThreadPoolExecutor.AbortPolicy());
            CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1000);
//                    int n = Integer.parseInt("n");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return "结果11";
            }, executor);
            //whenComplete 内异常要处理掉，不能传播到主线程，thenApply/thenAccept join() get() 会传播到主线程。
            //不适用join 不会抛异常到主线程
//            future1.join();
//            future1.get();
//            CompletableFuture future2 = future1.thenApply((r) ->
//            {
//
//                Object o = r;
//                int n = Integer.parseInt("n");
//                return o;
//            });
//            //future2 不调用 join 方法 不会抛异常到主线程
//            future2.join();

            CompletableFuture<Void> future3 = future1.thenAccept((r) ->
            {

                Object o = r;
                int n = Integer.parseInt("n");

            });
            //future3 不调用 join 方法 不会抛异常到主线程
//            future3.join();

//            future3.whenComplete()//参数 BiConsumer
            // future3.handle //参数 BiFunction
            future1.handle((result, ex) -> {
                if (ex != null) return "fallback";
                return result.toUpperCase();
            });
        } catch (Exception ex) {
            int n = 0;
        }
    }

    private void completableFutureStatusTest() {


        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() - 1,
                Runtime.getRuntime().availableProcessors() * 2,
                6000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadFactoryImpl("ThreadTest"),
                new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
//                    int n = Integer.parseInt("n");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "结果11";
        }, executor);

        // 是否完成（成功或异常）
        boolean isDone = future1.isDone();
        // 是否异常完成
        boolean isCompletedExceptionally = future1.isCompletedExceptionally();
        // 是否已取消
        boolean isCancelled = future1.isCancelled();

        future1.join();
        // 是否完成（成功或异常）
        boolean isDone2 = future1.isDone();
        //  是否异常完成
        boolean isCompletedExceptionally2 = future1.isCompletedExceptionally();
        // 是否已取消
        boolean isCancelled2 = future1.isCancelled();

        //throwable != null 正常完成
        future1.whenComplete((futureResult, throwable) -> {
            if (throwable != null) {

                Throwable e = (Throwable) throwable;
                String msg = e.toString();
                String string = e.getStackTrace().toString();
                int n = 0;

            }
            int nn = 0;
        });

        // 方法1：检查是否完成且没有异常
        if (future1.isDone() && !future1.isCompletedExceptionally()) {
            System.out.println("正常完成");
            // 安全获取结果
            String result = future1.join();
        }

        // 方法2：检查是否被取消
        if (!future1.isCancelled() && future1.isDone()) {
            // 可能是正常完成或异常完成，需要进一步判断
        }

        int n = 0;
    }

}
