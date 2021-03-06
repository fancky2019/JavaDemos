package Test.opensource.redis;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
Distributed locks with Redis:https://redis.io/topics/distlock
Redisson github:https://github.com/redisson/redisson
redisson-examples：https://github.com/redisson/redisson-examples
 */
public class RedissonTest {

    static RedissonClient redisson;

    //  static  Condition condition;
    static {
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
        // lock();

        useRedLock();
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
        while (lock.isLocked()) {
            Thread.sleep(1);
        }

//        //或者直接返回
//        if (lock.isLocked()) {
//          throw  new Exception("服务器繁忙!");
//        }
        try {
            lock.lock(10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            String msg = ex.getMessage();
            Integer m = 0;

        } finally {
            lock.unlock();//释放锁
            // condition.notify();
        }

    }
}
