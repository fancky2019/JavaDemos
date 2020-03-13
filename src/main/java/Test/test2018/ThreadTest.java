package Test.test2018;

import Model.Student;
import common.CallBackRunnable;

import javax.xml.bind.annotation.XmlType;
import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * callable和runnable 区别在VolatileTest类内有描述。
 *
 * ThreadPoolExecutor :线程池具体的执行内部逻辑。参照ThreadPoolExecutor类内的<dt>Queuing</dt>介绍
 *
 *
 * sleep()和wait()  区别
 * sleep()方法导致了程序暂停执行指定的时间,让出cpu该其他线程,但是他的监控状态依然保持者,当指定的时间到了又会自动恢复...
 * .最主要是sleep方法没有释放锁,而wait方法释放了锁,使得其他线程可以使用同步控制块
 */
public class ThreadTest {

    public void test() {
        try {
            Long threadID = Thread.currentThread().getId();
//            parameterThread();
            // futureTask();
//            CompletableFuture<Integer> completableFuture = completableFutureDemo();
//            Thread.sleep(11 * 1000);
//
//            Integer m = completableFuture.get();


            //   functionBlockingQueue();
            // threadPool();


//            threadException();


//            CompletableFuture<Integer> future = completeOnTimeout(() ->
//            {
//                try {
//                    Thread.sleep(5000);
//                    return 1;
//                } catch (Exception ex) {
//                    return -1;
//                }
//            }, 2, 3, TimeUnit.SECONDS);
//            Integer re = future.get();
//
//
//            CompletableFuture<String> a = supplyAsync(() -> "hi", 1,
//                    TimeUnit.SECONDS, "default");

//            interrupt();

            threadTimeOut();

            Integer n = 1;
            //whenComplete();
        } catch (Exception ex) {
            String str = ex.getMessage();
            Integer m = 0;
        }


    }


    //region  Thread
    private void declareStartThread() {

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

    //region Interrupt
    private void interrupt() {
        Thread thread = new Thread(() ->
        {
            try {
                int i = 0;
                for (; ; ) {
                    System.out.println(++i);
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {

            }
        });
        thread.setDaemon(true);
        thread.start();
        try {
            thread.join(10 * 1000);//阻塞调用线程30s.
            thread.interrupt();//相当于C#的Abort，此时线程可能还在继续执行，并没有立即终止。
            thread.join();//等待直到thread终止，
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
    //endregion

    //region 子线程内的异常，不会被外部捕捉。

    /**
     * //线程内的异常
     */
    private void threadException() {
//        Thread thread=new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });


        try {
            Thread thread = new Thread(() ->
            {
                //线程内的异常，不会被外部捕捉。
                try {
                    Integer m = Integer.parseInt("m");
                    Integer n = 0;
                } catch (Exception ex) {
                    System.out.println(MessageFormat.format("Thread inner :{0}", ex.getMessage()));
                    throw ex;
                }

//                Integer n = Integer.valueOf("m");
            });

            thread.start();
        } catch (Exception ex) {
            System.out.println(MessageFormat.format("threadException inner :{0}", ex.getMessage()));
            Integer n = 0;
        }

        //异常抛不出都主线程
        try {

            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(() ->
            {
                try {
                    Integer m = Integer.parseInt("m");
                    Integer n = 0;
                } catch (Exception ex) {
                    System.out.println(MessageFormat.format("newCachedThreadPool inner :{0}", ex.getMessage()));
                    throw ex;
                }
            });


        } catch (Exception ex) {
            System.out.println(MessageFormat.format("threadException inner 2 :{0}", ex.getMessage()));
            Integer n = 0;
        }


        //异常可以抛出到主线程
        try {
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
            Future<Integer> future = scheduledExecutorService.submit(() ->
            {
                try {
                    Integer m = Integer.parseInt("m");
                    Integer n = 0;
                    return 1;
                } catch (Exception ex) {
                    // sleep interrupted；
                    System.out.println(MessageFormat.format("newScheduledThreadPool inner  :{0}", ex.getMessage()));
                    throw ex; //可以把异常抛到主线程
                }
            });


            Integer re = future.get();

        } catch (Exception ex) {
            System.out.println(MessageFormat.format("threadException inner 3 :{0}", ex.getMessage()));
            Integer m = 0;
        }
    }
    //endregion

    //endregion

    //region ParameterThread
    private void parameterThread() {
        String param = "ParameterThread";
        new Thread(new ParameterRunnable(param)).start();
        CompletableFuture.runAsync(new ParameterRunnable(param + "_CompletableFuture"));

        CompletableFuture.runAsync(new CallBackRunnable(() ->
        {
            System.out.println("CallBackRunnable");
        }));

        CompletableFuture.runAsync(new CallBackRunnable<>((p) ->
        {
            System.out.print("CallBackRunnable<> ");
            System.out.println(p);
        }, "ParameterThread"));
    }

    class ParameterRunnable implements Runnable {
        private Object param;

        public ParameterRunnable(Object param) {
            this.param = param;
        }

        @Override
        public void run() {
            System.out.println(param.toString());
        }
    }


    //endregion

    //region synchronized
    private Object lockObj = new Object();

    private synchronized void synchronizedTest() {
        synchronized (lockObj) {
            float f = 3.4f;
            double d = 3.4;
        }
    }
    //endregion

    //region  threadPool
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
//endregion

    //region futureTask
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
    //endregion

    //region CompletableFuture
    private CompletableFuture<Integer> completableFutureDemo() {
        try {

            //CompletableFuture 内部默认最多启动处理器个数 -1 个线程执行任务。
            CompletableFuture.runAsync(() ->
            {
            });
            // 指定Executor， CompletableFuture 内部创建 Integer.MAX_VALUE个线程
            CompletableFuture.runAsync(() ->
            {
            },Executors.newCachedThreadPool());

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
    //endregion

    //region 阻塞队列生产者消费者 LinkedBlockingQueue
    LinkedBlockingQueue<Student> linkedBlockingQueue = null;

    private void functionBlockingQueue() {
        linkedBlockingQueue = new LinkedBlockingQueue<Student>(100000);
        producer();
        consumer();
        //   LinkedTransferQueue<Student> linkedTransferQueue = new LinkedTransferQueue<>();

    }

    private void producer() {
        //没有返回值
//        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() ->
        CompletableFuture.runAsync(() ->
        {
            for (Integer i = 1; i <= 10000; i++) {
                try {
                    Random rd = new Random();
                    Integer next = rd.nextInt(10000);
                    Student student = new Student();
                    student.setAge(next);
                    student.setName("test" + next);
                    linkedBlockingQueue.put(student);
                    Thread.sleep(500);
                } catch (Exception ex) {
                    String msg = ex.toString();
                    Integer m = 0;
                }
            }
        });
    }

    private void consumer() {
        CompletableFuture.runAsync(() ->
        {
            try {
                Integer i = 0;
                while (true) {
                    Student student = linkedBlockingQueue.take();
                    i++;
                    System.out.printf("%d:%s\n", student.getAge(), student.getName());
                    if (i == 100) {
                        break;
                    }
                }
            } catch (Exception ex) {
                String msg = ex.toString();
            }

        });
    }
    //endregion


    //region  threadTimeOut
    private void threadTimeOut() {
        try {

            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
            Future<Integer> future = scheduledExecutorService.submit(() ->
            {
                try {
                    Thread.sleep(5000);
                    return 1;
                } catch (Exception ex) {
                    // sleep interrupted；
                    System.out.println(ex.getMessage());
                    throw ex; //可以把异常抛到主线程
//                    return -1;
                }
            });


            scheduledExecutorService.schedule(() ->
            {
                if (!future.isDone()) {
                    //sleep interrupted；终断线程
                    future.cancel(true);
                }
            }, 3, TimeUnit.SECONDS);

            Integer re = future.get();
            //void:execute没有返回值无法取消
            scheduledExecutorService.execute(() ->
            {
                //();
            });
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }

    private <T> CompletableFuture<T> completeOnTimeout(Supplier<T> supplier, T defaultValue, Integer timeOut, TimeUnit timeUnit) {
        CompletableFuture<T> completableFuture = CompletableFuture.supplyAsync(supplier);
        ScheduledExecutorService schedulerExecutor = Executors.newScheduledThreadPool(10);
        schedulerExecutor.schedule(() ->
        {
            if (!completableFuture.isDone()) {
                //使任务完成，并返回初始值
                completableFuture.complete(defaultValue);
                //取消任务并跑出异常，任务完成
                //  completableFuture.cancel(true);
            }
        }, timeOut, timeUnit);
        return completableFuture;
    }

    private static final ScheduledExecutorService schedulerExecutor = Executors.newScheduledThreadPool(10);
    private static final ExecutorService executorService = Executors.newCachedThreadPool();


    public static <T> CompletableFuture<T> supplyAsync(final Supplier<T> supplier, long timeoutValue, TimeUnit timeUnit, T defaultValue) {

        final CompletableFuture<T> cf = new CompletableFuture<T>();

        // as pointed out by Peti, the ForkJoinPool.commonPool() delivers a
        // ForkJoinTask implementation of Future, that doesn't interrupt when cancelling
        // Using Executors.newCachedThreadPool instead in the example
        // submit task
        Future<?> future = executorService.submit(() -> {
            try {
                cf.complete(supplier.get());
            } catch (Throwable ex) {
                cf.completeExceptionally(ex);
            }
        });

        //schedule watcher
        schedulerExecutor.schedule(() -> {
            if (!cf.isDone()) {
                cf.complete(defaultValue);
                future.cancel(true);
            }

        }, timeoutValue, timeUnit);

        return cf;
    }
    //endregion
}

