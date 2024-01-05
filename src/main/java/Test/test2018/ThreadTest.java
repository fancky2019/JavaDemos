package Test.test2018;

import Model.Student;
import utility.CallBackRunnable;
import utility.TXTFile;

import java.text.MessageFormat;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;

/*
 * callable和runnable 区别在test2019.VolatileTest类内有描述。
 *
 * ThreadPoolExecutor :线程池具体的执行内部逻辑。参照ThreadPoolExecutor类内的<dt>Queuing</dt>介绍
 *
 *
 * sleep()和wait()  区别
 * sleep()方法导致了程序暂停执行指定的时间,让出cpu该其他线程,但是他的监控状态依然保持者,当指定的时间到了又会自动恢复...
 * .最主要是sleep方法没有释放锁,而wait方法释放了锁,使得其他线程可以使用同步控制块
 *
 * 避免加锁用ThreadLocal
 *
 * 2021  CompletableFutureTest
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
            threadPool();


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

//            threadTimeOut();

//            deadLock();
//            userThreadDaemonThread();

//            threadState();

//            completableFutureDemo();
            Integer n = 1;
            //whenComplete();
        } catch (Exception ex) {
            String str = ex.getMessage();
            Integer m = 0;
        }


    }

    //region  Thread
    private void declareStartThread() {

//        Callable
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

        int N_CPUS = Runtime.getRuntime().availableProcessors();
        /*
        cpu 密集型：N_CPUS+1
        io密集型：N_CPUS*2
         */


        // 4个线程池差异看源码ThreadPoolExecutor实例的参数

        /*newCachedThreadPool:使用默认，如果短时间高并发会创建大量线程，核心线程参数:0,SynchronousQueue 队列长度0，就造成
                创建最大线程数Integer.MAX_VALUE,个线程
        SynchronousQueue是无界的，是一种无缓冲的等待队列，但是由于该Queue本身的特性，
        在某次添加元素后必须等待其他线程取走后才能继续添加；可以认为SynchronousQueue是一个缓存值为1的阻塞队列，
        但是 isEmpty()方法永远返回是true，remainingCapacity() 方法永远返回是0，remove()和removeAll()
        方法永远返回是false，iterator()方法永远返回空，peek()方法永远返回null。
         */
//        SynchronousQueue
        ExecutorService executorService = Executors.newCachedThreadPool();
        //submit() 返回一个 Future<T> 而execute()没有返回值
        executorService.execute(() ->
        {
            doWork();
        });

        ExecutorService cachedThreadPool
                = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() - 1,
                Runtime.getRuntime().availableProcessors() * 2,
                6000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());//LinkedBlockingQueue:默认容量Integer.MAX_VALUE

//        new LinkedBlockingQueue<>(1000)

        ExecutorService cachedThreadPool1
                = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() - 1,
                Runtime.getRuntime().availableProcessors() * 2,
                6000,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(1000));
//                new ArrayBlockingQueue<>(Integer.MAX_VALUE));

        cachedThreadPool1.execute(() ->
        {

        });

        //submit 最终调用execute
        Future future = cachedThreadPool1.submit(() ->
        {

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

   // 线程池ThreadPoolExecutor 无法执行带有返回值的任务。
    private void futureTask() throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();

        //submit() 返回一个 Future<T> 而execute()没有返回值
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
            int par = ForkJoinPool.commonPool().getParallelism();
            //不指定线程池。内部采用 ForkJoinPool.makeCommonPool()
            //CompletableFuture 内部默认最多启动处理器个数 -1 个线程执行任务。
            CompletableFuture.runAsync(() ->
            {
            });


            // 指定Executor， CompletableFuture 内部创建 Integer.MAX_VALUE个线程
            /*
             不指定线程池，就用默认的ForkJoinPool
             */
            CompletableFuture.runAsync(() ->
            {
            }, Executors.newCachedThreadPool());


            ExecutorService cachedThreadPool1
                    = new ThreadPoolExecutor(
                    Runtime.getRuntime().availableProcessors() - 1,
                    Runtime.getRuntime().availableProcessors() * 2,
                    0,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<Runnable>(1000));


            ExecutorService cachedThreadPool11
                    = new ThreadPoolExecutor(
                    Runtime.getRuntime().availableProcessors() - 1,
                    Runtime.getRuntime().availableProcessors() * 2,
                    0,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(1000),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.AbortPolicy());

            CompletableFuture.runAsync(() ->
            {
            }, cachedThreadPool1);
//            Function
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

//            //所有任务都完成
//            CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2, future3);
//allFutures.join();
//if(allFutures.isDone())
//            {
//
//            }
//            allFutures.thenRun(() -> {
//                System.out.println("All tasks completed");
//                // 在这里执行下一步操作
//            });


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

    //region CountDownLatch
//    CountDownLatch 可用CompleteFuture allOf 方法代替
    //endregion

    //region Semaphore 同时访问共享资源的线程上限
//    Semaphore（信号量）可以用来限制能同时访问共享资源的线程上限，它内部维护了一个许可的变量，也就是线程许可的数量
//    Semaphore的许可数量如果小于0个，就会阻塞获取，直到有线程释放许可
//    Semaphore是一个非重入锁
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
                int i = 0;
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

    //region  ThreadTimeOut
    /*
    在指定时间内定时器监测Future是否执行完成，否则取消
   <T> Future<T> submit(Callable<T> task);
   Future<?> submit(Runnable task);
    void execute(Runnable command);
     */
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

    //region 死锁
    static final Object a = new Object();
    static final Object b = new Object();

    /*
    当发生的死锁后，JDK自带了两个工具(jstack和JConsole)，可以用来监测分析死锁的发生原因。
    JConsole目录位置:C:\Java\jdk1.8.0_151\bin
                     使用方法：1）、选中一个进程，连接
                               2）、在线程tab页下方，点击检测到死锁。
     */
    private void deadLock() {
        try {
           /*
           线程产生死锁
           输出：
               A-13 Enter Thread A
               B-14 Enter Thread B
           */
            CompletableFuture.runAsync(() ->
            {
                synchronized (a) {
                    try {
                        System.out.println(MessageFormat.format("A-{0} Enter Thread A", Thread.currentThread().getId()));
                        //睡100ms,确保下面线程执行，否则下面线程还没执行，此线程就执行完，无法锁住。
                        Thread.sleep(100);
                        int m = 0;
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    //线程会阻塞在此处。
                    synchronized (b) {
                        System.out.println("A Enter Thread B");
                    }
                }
                System.out.println("A  Thread  Complete");
            });

            CompletableFuture.runAsync(() ->
            {
                synchronized (b) {
                    try {
                        System.out.println(MessageFormat.format("B-{0} Enter Thread B", Thread.currentThread().getId()));
                        //   Thread.sleep(100);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    synchronized (a) {
                        System.out.println("B Enter Thread A");
                    }
                }
                System.out.println("B  Thread  Complete");
            });

//            CompletableFuture.runAsync(()->
//            {
//                while (true)
//                {
//
//                }
//            });

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }
    //endregion

    //region 用户线程、守护线程
    /*
    当程序有用户程序存在时候，守护线程就不会退出。
     */
    volatile int i = 1;

    private void userThreadDaemonThread() {
        Thread userThread = new Thread(() ->
        {
            try {
                while (true) {
                    TXTFile.writeText("UserThread.txt", "userThread - " + String.valueOf(i));
                    Thread.sleep(1000);
                    ++i;
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });
        userThread.start();

        Thread daemonThread = new Thread(() ->
        {
            try {
                while (true) {
                    TXTFile.writeText("DaemonThread.txt", "daemonThread - " + String.valueOf(i));
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });
        daemonThread.setDaemon(true);
        daemonThread.start();
    }

    //endregion

    //region  while(true)线程退出
    boolean _stop = false;
    //MSDN 官方实例
    // volatile  bool _stop=false;

    private void threadExit() {
        /*
         * 存储结构：寄存器缓存+主存
         * volatile不在线程内缓存，会强制刷新到主存
         * 每个线程内会有一个变量的副本，线程改变变量会将改变量刷新到主存。
         *
         *
         * threadStop可能执行_stop为true,可能未及时刷新到主存，所以
         * thread可能还会继续运行。所以做好把_stop声明为volatile  bool _stop
         */
        Thread thread = new Thread(() ->
        {
            while (!_stop) {
                //DoWork()
            }
        });
        thread.setDaemon(true);
        thread.start();

        Thread threadStop = new Thread(() ->
        {
            //当满足某个条件将_stop设为false。
            _stop = true;
        });
        thread.setDaemon(true);
        threadStop.start();
    }
    //endregion

    //region 线程状态
    private void threadState() {
        /*
         *ThreadState: NEW
         *ThreadState: TIMED_WAITING
         *ThreadState: TERMINATED
         */
        Thread newThread = new Thread(() ->
        {
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {

            }

        });

        System.out.println(MessageFormat.format("ThreadState: {0}", newThread.getState()));
        newThread.start();

        // Wait for newThread to start and go to sleep.
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {

        }
        System.out.println(MessageFormat.format("ThreadState: {0}", newThread.getState()));

        // Wait for newThread to restart.
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {

        }
        System.out.println(MessageFormat.format("ThreadState: {0}", newThread.getState()));
    }
    //endregion

    //region  Timer

    private void schedule() {
        //Timer
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("TimerTask");
                //doWork()
            }
        }, 0, 3 * 1000);


        //newScheduledThreadPool
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() ->
        {
            System.out.println("scheduleAtFixedRate");
            //doWork()
        }, 0, 2, TimeUnit.SECONDS);


    }
    // endregion

}

