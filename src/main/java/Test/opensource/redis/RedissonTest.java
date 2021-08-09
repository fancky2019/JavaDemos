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
 */

/*
红锁问题： redis 网络脑裂问题
主从设计；一主多从
N:为奇数，  至少三台机器
 */
public class RedissonTest {
    private static final Logger logger = LogManager.getLogger(RedissonTest.class);
    static RedissonClient redisson;

    //  static  Condition condition;
    static {
        //默认数据库index:0
        //redisson-->connectionManager-->config-->database
        Config config = new Config();
        ((SingleServerConfig) config.useSingleServer().setTimeout(1000000))
                .setAddress("redis://127.0.0.1:6379")
                .setPassword("fancky123456");


//        // connects to 127.0.0.1:6379 by default
        redisson = Redisson.create(config);


//        ReentrantLock reentrantLock=new ReentrantLock() ;
//        condition=reentrantLock.newCondition();
    }

    public void test() throws Exception {
        logger.info("info");
        // lock();
        CompletableFuture.runAsync(()->
        {
            try {
                useRedLock();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Thread.sleep(2000);
        CompletableFuture.runAsync(()->
        {
            try {
                useRedLock();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void lock() {
        RLock lock = redisson.getLock("lock");
        //判断是否有锁
        Boolean locked1 = lock.isLocked();//false
        lock.lock(10, TimeUnit.SECONDS);
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

        lock.unlock();
        //关闭客户端
        //   redisson.shutdown();
    }


    private void useRedLock() throws Exception {
        RLock lock = redisson.getLock("lock");
//        while (lock.isLocked()) {
//            condition.await(10,TimeUnit.SECONDS);
//        }

        //如果锁被占用,轮询
//        while (lock.isLocked()) {
//            Thread.sleep(1);
//        }
        try {

            //500MS 获取锁，3000锁维持时间
            //内部采用信号量控制等待时间  Semaphore
            lock.tryLock(500, 60*1000, TimeUnit.MICROSECONDS);
//            lock.lock(10, TimeUnit.SECONDS);
            //或者直接返回
            if (lock.isLocked()) {
                logger.error(MessageFormat.format("Thread - {0} 获得锁失败！",Thread.currentThread().getId()));
            }
            else {
                logger.info(MessageFormat.format("Thread - {0} 获得锁！",Thread.currentThread().getId()));
            }
        } catch (Exception ex) {
            String msg = ex.getMessage();
            Integer m = 0;
           logger.error(ex.toString());
        } finally {
            Thread.sleep(60*1000);
            lock.unlock();//释放锁
            // condition.notify();
        }

    }


}
