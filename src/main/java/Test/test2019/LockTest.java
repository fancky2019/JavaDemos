package Test.test2019;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
Lock:实现线程同步。

synchronized实现同步的基础：
普通同步方法（实例方法），锁是当前实例对象 ，进入同步代码前要获得当前实例的锁:instance
静态同步方法，锁是当前类的class对象 ，进入同步代码前要获得当前类对象的锁:  LockTest.class
同步方法块，锁是括号里面的对象，对给定对象加锁，进入同步代码库前要获得给定对象的锁:lockObj

synchronized：内部生成monitorEnter 和monitorExit 指令。


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



synchronized默认有jvm内部实现控制的，是非公平锁

synchronized，锁是保存在对象头里面的，根据对象头数据来标识是否有线程获得锁/争抢锁；
ReentrantLock锁的是线程，根据进入的线程和int类型的state标识锁的获得/争抢。


 * synchronized 同步语句块的实现使用的是 monitorenter 和 monitorexit 指令，
 * 其中 monitorenter 指令指向同步代码块的开始位置，monitorexit 指令则指明同步代码块的结束位置。
 *
 * synchronized 修饰的方法并没有 monitorenter 指令和 monitorexit 指令，
 * 取得代之的确实是 ACC_SYNCHRONIZED 标识，该标识指明了该方法是一个同步方法。
 */
public class LockTest {

    SynchronizedClass synchronizedClass1 = new SynchronizedClass();
    SynchronizedClass synchronizedClass2 = new SynchronizedClass();
    SynchronizedClass synchronizedClass3 = new SynchronizedClass();
    SynchronizedClass synchronizedClass4 = new SynchronizedClass();
    SynchronizedClass synchronizedClass5 = new SynchronizedClass();
    public static Object lockObj = new Object();

    public static int i = 0;

    public void test() {
        // synchronizedTest();
        //ReentrantLockTest();

//        CompletableFuture.runAsync(() ->
//        {
//            synchronizedReentrantTest();
//        });

//        synchronizedSleepTest();

        reentrantLockTest();

        // synchronized 使用区别
        //锁静态方法、 synchronized (SynchronizedClass.class)、synchronized (SynchronizedClass.class) 等同
        //锁普通方法、 synchronized (this) 等同
        //锁对象快锁被锁的对象，如果被锁的对象是静态的所有对象调用都将同步

//        CompletableFuture.runAsync(() ->
//        {
//            SynchronizedClass.staticFun();
//        });
//        CompletableFuture.runAsync(() ->
//        {
//            synchronizedClass1.synchronizedBlock1();
//        });
//        CompletableFuture.runAsync(() ->
//        {
//            synchronizedClass2.synchronizedBlock2();
//        });
//        CompletableFuture.runAsync(() ->
//        {
//            synchronizedClass3.objectFun();
//        });
//        CompletableFuture.runAsync(() ->
//        {
//            synchronizedClass3.thisFun();
//        });
//
//        CompletableFuture.runAsync(() ->
//        {
//            synchronizedClass5.thisFun1();
//        });
//
//        CompletableFuture.runAsync(() ->
//        {
//            synchronizedClass1.lockStaticObject();
//        });
//
//        CompletableFuture.runAsync(() ->
//        {
//            synchronizedClass2.lockStaticObject();
//        });
    }

    private final int lockInt = 5;
    private final String lockStr = "lockStr";

    //私有成员实现对象锁。保证该对象线程安全。
    private final Object lockObj1 = new Object();
    //类锁：锁静态变量，类锁保证该类的对象线程安全。
    private final static Object LOCKOBJECT = new Object();

    private void synchronizedBlock() {
        //要求参数是object。
//        synchronized (lockInt)
//        {
//
//        }

        //会造成死锁。
        synchronized (lockStr) {

        }
        synchronized (lockObj1) {

        }
        synchronized (LOCKOBJECT) {

        }
    }

    /*
    CompletableFuture 内部采用 ForkJoinPool
     */
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

    private void synchronizedSleepTest() {

        /*
        锁当前对象实例，多个线程操作同一对象可以锁住。
         */
//      CompletableFuture.runAsync(() ->
//      {
//          synchronizedClass1.synchronizedTest();
//      });
//      CompletableFuture.runAsync(() ->
//      {
//          synchronizedClass1.synchronizedTest();
//      });


        /*
         锁当前对象实例，多个线程操作不同对象锁不住。
         */
//        CompletableFuture.runAsync(() ->
//        {
//            synchronizedClass1.synchronizedTest();
//        });
//
//        CompletableFuture.runAsync(() ->
//        {
//            synchronizedClass2.synchronizedTest();
//        });


         /*
         锁当前对象实例，多个线程操作不同对象锁不住，尽管调用的不是同一个方法。
         */
        CompletableFuture.runAsync(() ->
        {
            synchronizedClass1.synchronizedTest();
        });
        CompletableFuture.runAsync(() ->
        {
            synchronizedClass1.synchronizedTest1();
        });

    }


    ReentrantLock reentrantLock = new ReentrantLock(true);

    final Object reentrantLockObj = new Object();
    volatile boolean sync = false;

    void reentrantLockTest() {

        CompletableFuture.runAsync(() ->
        {
            while (true) {
                synchronized (reentrantLockObj) {
                    System.out.println("ThreadId - " + Thread.currentThread().getId());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


                }
                if (sync) {
                    System.out.println("sync data ");
                    sync = false;
                }
            }
        });

        CompletableFuture.runAsync(() ->
        {
            lock.lock();
            lock.unlock();
//            synchronized (reentrantLockObj) {
//                System.out.println("ThreadId - " + Thread.currentThread().getId());
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//
//
//            }
            sync = true;
        });
    }


}

/*
 synchronized (obj) { }  obj 必须是引用类型
 */
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


    /*
    不加锁进行同步，两个线程都会进入。
     */
    public void synchronizedTest() {

        /*
        锁当前调用对象的实例，锁对调用对象起作用，不同调用对象不收限制。
         */
        synchronized (this) {
            try {
                System.out.println(MessageFormat.format("ThreadID{0} enter synchronizedTest", Thread.currentThread().getId()));
                Thread.sleep(5000);
                System.out.println(MessageFormat.format("ThreadID{0} exist synchronizedTest", Thread.currentThread().getId()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //从驻留池取
        synchronized ("123".intern()) {
            try {
                System.out.println("lock string");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*
        锁公共变量，有点类似分布式锁的原理
         */
//        synchronized (LockTest.lockObj) {
//            try {
//                System.out.println(MessageFormat.format("ThreadID{0} enter synchronizedTest", Thread.currentThread().getId()));
//                Thread.sleep(5000);
//                System.out.println(MessageFormat.format("ThreadID{0} exist synchronizedTest", Thread.currentThread().getId()));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

    }


    public void test() {

    }

    private static Object staticObject = new SynchronizedClass();

    public void synchronizedTest1() {

        /*
        锁当前调用对象的实例，锁对调用对象起作用，不同调用对象不收限制。
         */
        synchronized (this) {
            try {
                System.out.println(MessageFormat.format("ThreadID{0} enter synchronizedTest1", Thread.currentThread().getId()));
                Thread.sleep(5000);
                System.out.println(MessageFormat.format("ThreadID{0} exist synchronizedTest1", Thread.currentThread().getId()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public synchronized static void staticFun() {
        System.out.println("staticFun");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void synchronizedBlock1() {
        synchronized (SynchronizedClass.class) {
            System.out.println("SynchronizedClass.class");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void synchronizedBlock2() {
        synchronized (this.getClass()) {
            System.out.println("this.getClass()");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public synchronized void objectFun() {
        System.out.println("objectFun");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void thisFun() {
        synchronized (this) {
            System.out.println("thisFun");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public synchronized void thisFun1() {

        System.out.println("thisFun1");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void lockStaticObject() {
        synchronized (staticObject) {
            System.out.println("lockStaticObject");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}


