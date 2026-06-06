package Test.test2025;

import net.bytebuddy.asm.Advice;

import java.util.UUID;
import java.util.concurrent.ThreadFactory;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class ThreadFactoryImpl implements ThreadFactory {
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String poolName;
    private final String serviceName;
    private final boolean daemon;

    public ThreadFactoryImpl() {
        this("pool", UUID.randomUUID().toString().replaceAll("-", ""), false);
    }

    public ThreadFactoryImpl(String poolName) {
        this(poolName, UUID.randomUUID().toString().replaceAll("-", ""), false);

//        // 1. 获取当前线程的 ThreadLocalRandom 实例
//        ThreadLocalRandom random = ThreadLocalRandom.current();
//
//        // 2. 生成一个随机的 int 值 (范围: 0 到 100, 不包括 100) [0,100)
//        int randomInt = random.nextInt(100);

    }

    public ThreadFactoryImpl(String poolName, String serviceName) {
        this(poolName, serviceName, false);
    }

    public ThreadFactoryImpl(String poolName, String serviceName, boolean daemon) {
        this.poolName = poolName;
        this.serviceName = serviceName;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        String threadName = String.format("%s-%s-%d", serviceName, poolName, threadNumber.getAndIncrement());

        Thread thread = new Thread(r, threadName);
        thread.setDaemon(daemon);
        thread.setPriority(Thread.NORM_PRIORITY);

        // 生产环境必备：设置未捕获异常处理器
        thread.setUncaughtExceptionHandler((t, e) -> {
            System.err.printf("[ERROR] Uncaught exception in thread %s: %s%n",
                    t.getName(), e.getMessage());
            // 这里可以集成到日志系统、监控系统
            // Metrics.counter("thread.uncaught.exception").increment();
        });

        return thread;
    }
}

//// 使用示例
//ThreadFactory factory = new ThreadFactoryImpl(
//        "db-pool",          // 线程池名称
//        "order-service",    // 服务名称
//        false               // 非守护线程
//);
