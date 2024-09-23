package Test.opensource.redis;

import Test.test2021.Log4J2Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
Distributed locks with Redis:https://redis.io/topics/distlock
Redisson github:https://github.com/redisson/redisson
redisson-examples：https://github.com/redisson/redisson-examples
doc:https://github.com/redisson/redisson/wiki/%E7%9B%AE%E5%BD%95
 */

/**
 * 红锁问题： redis 网络脑裂问题
 * 主从设计；一主多从
 * N:为奇数，  至少三台机器
 * <p>
 * redisson ：redis 集群存在脑裂问题，master A 数据未同步到 slave B C,A 宕机了  B成为Master，
 * 此时 其他线程就能获取锁。设置redis 写 min-slaves-to-write=2，否则主节点A  拒绝写入。
 * 强一致性 考虑ZK
 * <p>
 * redisson中的watchdog只有在没有指定锁过期时间的时候才会被使用，如果指定了过期时间，业务超时锁自动释放会
 * watch dog 自动延期机制
 * 锁到期任务未完成：看门狗线程会不断的延长锁超时时间，锁不会因为超时而被释放。
 * 默认情况下，看门狗的续期时间是30s，也可以通过修改Config.lockWatchdogTimeout来另行指定。
 * <p>
 * TimerTask 定时任务不听续期，新版本采用HashedWheelTimer （netty 内实现的工具类） 定时任务
 * private void scheduleExpirationRenewal(long threadId) {
 * if (EXPIRATION_RENEWAL_MAP.containsKey(getEntryName())) {
 * return;
 * }
 * <p>
 * Timeout task = commandExecutor.getConnectionManager().newTimeout(new TimerTask() {
 *
 * @Override public void run(Timeout timeout) throws Exception {
 * <p>
 * RFuture<Boolean> future = renewExpirationAsync(threadId);
 * future.onComplete((res, e) -> {
 * EXPIRATION_RENEWAL_MAP.remove(getEntryName());
 * if (e != null) {
 * log.error("Can't update lock " + getName() + " expiration", e);
 * return;
 * }
 * <p>
 * if (res) {
 * // reschedule itself
 * scheduleExpirationRenewal(threadId);
 * }
 * });
 * }
 * <p>
 * }, internalLockLeaseTime / 3, TimeUnit.MILLISECONDS);
 * <p>
 * if (EXPIRATION_RENEWAL_MAP.putIfAbsent(getEntryName(), new ExpirationEntry(threadId, task)) != null) {
 * task.cancel();
 * }
 * }
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * .watch dog 自动延期机制（前提没有设置锁的过期时间（leaseTime））
 * 1.watch dog 在当前节点存活时每10s给分布式锁的key续期 30s；  internalLockLeaseTime 重新设置租赁时间30s
 * 2.watch dog 机制启动，且代码中没有释放锁操作时，watch dog 会不断的给锁续期；
 * 3.从可2得出，如果程序释放锁操作时因为异常没有被执行，那么锁无法被释放，所以释放锁操作一定要放到 finally {} 中；
 * <
 * 写入hash类型数据：redisKey:lock hashKey  uuid:线程id  hashValue:thread id
 * redisson 可重入：  uuid+threadId  。
 * UUID id = UUID.randomUUID();
 * 同一个系统内线程id唯一，每个连接一个uuid，就保证key唯一。
 */
public class RedissonTest {
    private static final Logger logger = LogManager.getLogger(RedissonTest.class);
    static RedissonClient redisson;

    //  static  Condition condition;
    static {
        //默认数据库index:0
        //redisson-->connectionManager-->config-->database
        //单Redis节点模式
        Config config = new Config();
        ((SingleServerConfig) config.useSingleServer()
                .setTimeout(100 * 1000))
                .setAddress("redis://127.0.0.1:6379")
                .setPassword("fancky123456");


//        // connects to 127.0.0.1:6379 by default
        redisson = Redisson.create(config);


//        ReentrantLock reentrantLock=new ReentrantLock() ;
//        condition=reentrantLock.newCondition();
    }

    public void test() throws Exception {
//        lock();
//        useRedLock();
        reentrantLockTest();
//        CompletableFuture.runAsync(() ->
//        {
//            try {
//                useRedLock();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        Thread.sleep(2000);
//        CompletableFuture.runAsync(() ->
//        {
//            try {
//                useRedLock();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
    }

    /**
     * 参照useRedLock
     */
    private void lock() {
        RLock lock = redisson.getLock("lock");
        //判断是否有锁
        Boolean locked1 = lock.isLocked();//false

//        //使用默认看门狗延期机制       //加锁（阻塞等待），默认过期时间是30秒。   this.lockWatchdogTimeout = 30000L;
        //每隔lockWatchdogTimeout/3 s 进行时间重置30s.即不停重置30s的过期时间
//        lock.lock();

        //写入hash类型数据：redisKey:lock hashKey  uuid:线程id  hashValue:thread id
//    public void lock(long leaseTime, TimeUnit unit)
        lock.lock(90, TimeUnit.SECONDS);
        locked1 = lock.isLocked();//true
        lock.unlock();//释放锁
        locked1 = lock.isLocked();//false
//        RLock lock1 = redisson.getLock("lock");
//        Boolean locked=lock1.isLocked();
//       // lock1.tryLock()
//        lock1.lock();
//        lock1.unlock();
//        locked=lock1.isLocked();
        try {
            Thread.sleep(10 * 1000);
        } catch (Exception ex) {

        }

        lock.unlock();//释放锁 unlock 删除key
        //关闭客户端
        //   redisson.shutdown();
    }

    public void redissonTest() throws Exception {
        String operationLockKey="redissonTest";
        RLock lock = redisson.getLock(operationLockKey);
        //不释放锁，锁会一直占用
        lock.lock();
        throw new Exception("dssdsd");

//        try {
//            lock.lock();
//            throw new Exception("dssdsd");
//        }  finally {
//            //先释放锁，然后在统一异常处理的时候 ，捕获异常
//            lock.unlock();;
//        }


    }
    private void useRedLock() throws Exception {
        //写入hash类型数据：redisKey:lock hashKey  uuid:线程id  hashValue:thread id
        RLock lock = redisson.getLock("lock");
//        while (lock.isLocked()) {
//            condition.await(10,TimeUnit.SECONDS);
//        }

        //如果锁被占用,轮询
//        while (lock.isLocked()) {
//            Thread.sleep(1);
//        }


        //            long maxRetryTimes = 3;
//            int retryTimes = 1;
//            long step = 5;
//
//            while (true) {
//                if (lock.isLocked()) {
//                    Thread.sleep(step * retryTimes);
//                    retryTimes++;
//                    lock = redissonClient.getLock(lockKey);
//                } else {
//                    break;
//                }
//                if (retryTimes > maxRetryTimes) {
//                    log.info("Thread - {} 获得锁 {}失败！锁被占用！", Thread.currentThread().getId(), lockKey);
//                    return null;
//                }
//
//            }


        try {
//            boolean isLocked = lock.isLocked();
//            if (isLocked) {
//                logger.error(MessageFormat.format("Thread - {0} 获得锁失败！锁被占用！", Thread.currentThread().getId()));
//            }
            //500MS 获取锁，3000锁维持时间
            //内部采用信号量控制等待时间  Semaphore
            //    public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit)
            //注：waitTime 获取锁时间，leaseTime：持有锁时间，超时释放锁
            //写入hash类型数据：redisKey:lock hashKey  uuid:线程id  hashValue:thread id
            //只要不指定leaseTime 就会自动续期
            boolean lockSuccessfully = lock.tryLock(30, 60, TimeUnit.SECONDS);
            if (lockSuccessfully) {
                logger.info(MessageFormat.format("Thread - {0} 获得锁！", Thread.currentThread().getId()));
            }
//            boolean lockSuccessfully = lock.tryLock(30,  TimeUnit.SECONDS);
//            if(lockSuccessfully)
//            {
//                logger.info(MessageFormat.format("Thread - {0} 获得锁！", Thread.currentThread().getId()));
//            }

            //不会释放锁 leaseTime=-1
//            lock.lock();
            //模拟耗时任务，redis HashedWheelTimer 会自动续期。不能打断点调试看redis 过期时间，此时代码不执行其他代码（续期代码），redis
            //就不会续期
            Thread.sleep(60 * 1000);
//            lock.lock(10, TimeUnit.SECONDS);
            //或者直接返回
            boolean isLocked = lock.isLocked();
            int m = 0;

        } catch (Exception ex) {
            String msg = ex.getMessage();
            Integer m = 0;
            logger.error(ex.toString());
        } finally {
            //模拟耗时任务，redis HashedWheelTimer 会自动续期
            Thread.sleep(60 * 1000);
            //释放锁 unlock 删除key
            // 如果锁因超时（leaseTime）会抛异常
            lock.unlock();
            // condition.notify();
        }

    }


    /**
     * redisson 支持可重入
     * @throws Exception
     */
    public void reentrantLockTest() throws Exception {
        //写入hash类型数据：redisKey:lock hashKey  uuid:线程id  hashValue:thread id
        RLock lock = redisson.getLock("lock");
//        while (lock.isLocked()) {
//            condition.await(10,TimeUnit.SECONDS);
//        }

        //如果锁被占用,轮询
//        while (lock.isLocked()) {
//            Thread.sleep(1);
//        }


        //            long maxRetryTimes = 3;
//            int retryTimes = 1;
//            long step = 5;
//
//            while (true) {
//                if (lock.isLocked()) {
//                    Thread.sleep(step * retryTimes);
//                    retryTimes++;
//                    lock = redissonClient.getLock(lockKey);
//                } else {
//                    break;
//                }
//                if (retryTimes > maxRetryTimes) {
//                    log.info("Thread - {} 获得锁 {}失败！锁被占用！", Thread.currentThread().getId(), lockKey);
//                    return null;
//                }
//
//            }


        try {
//            boolean isLocked = lock.isLocked();
//            if (isLocked) {
//                logger.error(MessageFormat.format("Thread - {0} 获得锁失败！锁被占用！", Thread.currentThread().getId()));
//            }
            //500MS 获取锁，3000锁维持时间
            //内部采用信号量控制等待时间  Semaphore
            //    public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit)
            //注：waitTime 获取锁时间，leaseTime：持有锁时间，超时释放锁
            //写入hash类型数据：redisKey:lock hashKey  uuid:线程id  hashValue:thread id
            //只要不指定leaseTime 就会自动续期
            boolean lockSuccessfully = lock.tryLock(30, 60, TimeUnit.SECONDS);
            if (lockSuccessfully) {
                logger.info(MessageFormat.format("Thread - {0} 获得锁！", Thread.currentThread().getId()));
            }
//            boolean lockSuccessfully = lock.tryLock(30,  TimeUnit.SECONDS);
//            if(lockSuccessfully)
//            {
//                logger.info(MessageFormat.format("Thread - {0} 获得锁！", Thread.currentThread().getId()));
//            }

            //不会释放锁 leaseTime=-1
//            lock.lock();
            //模拟耗时任务，redis HashedWheelTimer 会自动续期。不能打断点调试看redis 过期时间，此时代码不执行其他代码（续期代码），redis
            //就不会续期

            useRedLock();
            Thread.sleep(60 * 1000);
//            lock.lock(10, TimeUnit.SECONDS);
            //或者直接返回
            boolean isLocked = lock.isLocked();
            int m = 0;

        } catch (Exception ex) {
            String msg = ex.getMessage();
            Integer m = 0;
            logger.error(ex.toString());
        } finally {
            //模拟耗时任务，redis HashedWheelTimer 会自动续期
            Thread.sleep(60 * 1000);
            //释放锁 unlock 删除key
            // 如果锁因超时（leaseTime）会抛异常
            lock.unlock();
            // condition.notify();
        }

    }



}
