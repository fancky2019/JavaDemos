package Test.test2019;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
Lock:实现线程同步。

synchronized实现同步的基础：
普通同步方法（实例方法），锁是当前实例对象 ，进入同步代码前要获得当前实例的锁:instance
静态同步方法，锁是当前类的class对象 ，进入同步代码前要获得当前类对象的锁:  LockTest.class
同步方法块，锁是括号里面的对象，对给定对象加锁，进入同步代码库前要获得给定对象的锁:lockObj



静态变量：线程非安全:静态变量即类变量，位于方法区，为所有对象共享，共享一份内存，
          一旦静态变量被修改，其他对象均对修改可见，故线程非安全。

实例变量：单例模式（只有一个对象实例存在）线程非安全，非单例线程安全。
            实例变量为对象实例私有，在虚拟机的堆中分配，若在系统中只存在一个此对象的实例，在多线程环境下，
            “犹如”静态变量那样，被某个线程修改后，其他线程对修改均可见，故线程非安全；
            如果每个线程执行都是在不同的对象中，那对象与对象之间的实例变量的修改将互不影响，故线程安全。

局部变量：线程安全。每个线程执行时将会把局部变量放在各自栈帧的工作内存中，线程间不共享，故不存在线程安全问题。


    //局部变量:在线程栈内，属于调用线程。线程安全。每个线程执行时将会把局部变量放在各自栈帧的工作内存中，
    //         线程间不共享，故不存在线程安全问题。

    //全局变量：
    //        1):静态变量：非线程安全，是属于类级别，被所有对象共享，共享一份内存，一旦值被修改，
    //                     则其他对象均对修改可见，故线程非安全。
    //        2):实例变量：线程安全。每个线程执行都是在不同的对象中，那对象与对象之间的实例变量的修改将互不影响，故线程安全。

 */
public class LockTest {

    SynchronizedClass synchronizedClass1 = new SynchronizedClass();
    SynchronizedClass synchronizedClass2 = new SynchronizedClass();

    public static Object lockObj = new Object();

    public static int i = 0;

    public void test() {
        // synchronizedTest();
        //ReentrantLockTest();

        CompletableFuture.runAsync(() ->
        {
            synchronizedReentrantTest();
        });
    }

    private void synchronizedTest() {
        CompletableFuture.runAsync(() ->
        {
            for (int i = 0; i < 100; i++) {
//                synchronizedClass1.synchronizedMethod();
                // synchronizedClass1.synchronizedBlock();
                // synchronizedClass1.synchronizedBlockLockTest();
                synchronizedClass1.synchronizedStaticMethod();
            }

        });
        CompletableFuture.runAsync(() ->
        {
            for (int i = 0; i < 100; i++) {

                synchronizedClass2.synchronizedMethod();
//                synchronizedClass1.synchronizedMethod();
                //synchronizedClass2.synchronizedMethod();


                //synchronizedClass1.synchronizedBlock();
                //  synchronizedClass2.synchronizedBlock();


                // synchronizedClass1.synchronizedBlockLockTest();
                //  synchronizedClass2.synchronizedBlockLockTest();

                //  synchronizedClass1.synchronizedStaticMethod();
            }
        });
        try {
            Thread.sleep(3000);

            System.out.println(MessageFormat.format("synchronizedClass1.i:{0}", synchronizedClass1.i));
            System.out.println(MessageFormat.format("synchronizedClass1.j:{0}", synchronizedClass1.j));

            System.out.println(MessageFormat.format("synchronizedClass2.i:{0}", synchronizedClass2.i));
            System.out.println(MessageFormat.format("synchronizedClass2.j:{0}", synchronizedClass2.j));


            System.out.println(MessageFormat.format("SynchronizedClass.m:{0}", SynchronizedClass.m));
            System.out.println(MessageFormat.format("SynchronizedClass.n:{0}", SynchronizedClass.n));

        } catch (Exception ex) {

        }

    }


    /*
    参见demos2018下的
    ProduceConsumerConditionTest
     */
    private void ReentrantLockTest() {
        CompletableFuture.runAsync(() ->
        {
            ReentrantLockFunction();
        });
    }

    Lock lock = new ReentrantLock();
    int j = 1;

    /*
    可以递归调用（Recursive call）
     */
    private void ReentrantLockFunction() {
        try {
            lock.lock();
            j++;
            if (j == 2) {
                ReentrantLockFunction();
            }
            Thread.sleep(20);
            System.out.println(MessageFormat.format("i={0}", j));
        } catch (Exception ex) {

        } finally {
            lock.unlock();
        }

    }


    /*
    可以递归调用（Recursive call）
     */
    public synchronized void synchronizedReentrantTest() {
        try {
            j++;
            if (j == 2) {
                synchronizedReentrantTest();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            int m = 0;
        }
    }
}

class SynchronizedClass {

    int i = 0;
    volatile int j = 0;

    //局部变量:在线程栈内，属于调用线程。线程安全。每个线程执行时将会把局部变量放在各自栈帧的工作内存中，
    //         线程间不共享，故不存在线程安全问题。

    //全局变量：
    //        1):静态变量：非线程安全，是属于类级别，被所有对象共享，共享一份内存，一旦值被修改，
    //                     则其他对象均对修改可见，故线程非安全。
    //        2):实例变量：线程安全。每个线程执行都是在不同的对象中，那对象与对象之间的实例变量的修改将互不影响，故线程安全。


    static int m = 0;

    volatile static int n = 0;
    private Object lockObj = new Object();

    /*
    等同于
      synchronized (this) {

        }
     */
    public synchronized void synchronizedMethod() {
        try {
//            System.out.println(MessageFormat.format("Thread{0} enter ", Thread.currentThread().getId()));
            i++;
            j++;
            m++;
            n++;
//            Thread.sleep(200);
//            System.out.println(MessageFormat.format("Thread{0} leave ", Thread.currentThread().getId()));
        } catch (Exception ex) {

        }
    }

    /*
    类似C#的lock(lockObj)
    {
    }
     */
    public void synchronizedBlock() {
        synchronized (lockObj) {
            i++;
            j++;
            m++;
            n++;
        }
    }

    public void synchronizedBlockLockTest() {
        synchronized (LockTest.lockObj) {
            i++;
            j++;
            m++;
            n++;
        }

    }

    /*
    等同于
     synchronized (SynchronizedClass.class) {
        }
     */
    public static synchronized void synchronizedStaticMethod() {
        m++;
        n++;
    }
}


