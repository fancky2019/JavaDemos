package Test.test2018;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 有最大长度的并发队列，JDK中LinkedBlockingQueue有最大长度限制重载
 * Condition 的await、signal要获得的锁内
 */
public class ProduceConsumerConditionTest {
    private ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
    private Integer maxLength;
    private final ReentrantLock producerLock = new ReentrantLock();
    private Condition notFull = producerLock.newCondition();

    private final ReentrantLock consumerLock = new ReentrantLock();
    private Condition notEmpty = consumerLock.newCondition();

    public ProduceConsumerConditionTest(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public void Test() {

        try {


            //生产者线程
//            CompletableFuture.runAsync(() ->
//            {
//                while (true) {
//                    for (int i = 1; i <= 1000; i++) {
//                        producer(i);
//                    }
//
//                }
//            });


            //消费者线程
            CompletableFuture.runAsync(() ->
            {
                while (true) {
                    consumer();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }

                }
            });
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    void producer(Integer num) {
        producerLock.lock();
        try {
            while (queue.size() == maxLength) {
                try {
                    notFull.await();
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }

            }

            //poll：将首个元素从队列中弹出，如果队列是空的，就返回null
            //peek：查看首个元素，不会移除首个元素，如果队列是空的就返回null
            //element：查看首个元素，不会移除首个元素，如果队列是空的就抛出异常NoSuchElementException

           //offer，add区别：
            //一些队列有大小限制，因此如果想在一个满的队列中加入一个新项，多出的项就会被拒绝。
            //这时新的 offer 方法就可以起作用了。它不是对调用 add() 方法抛出一个 unchecked 异常，而只是得到由 offer() 返回的 false。
            //poll，remove区别：
            //remove() 和 poll() 方法都是从队列中删除第一个元素。remove() 的行为与 Collection 接口的版本相似，
            //但是新的 poll() 方法在用空集合调用时不是抛出异常，只是返回 null。因此新的方法更适合容易出现异常条件的情况。
            //peek，element区别：
            //element() 和 peek() 用于在队列的头部查询元素。与 remove() 方法类似，在队列为空时， element() 抛出一个异常，而 peek() 返回 null
            queue.offer(num);
            System.out.printf("enqueue %d \n", num);
            // notEmpty.signal();
        } finally {
            producerLock.unlock();
        }
        signalNotEmpty();


    }

    void consumer() {
        consumerLock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    notEmpty.await();
                    //等待5S如果还没有收到通知，继续执行
                    //  notEmpty.await(5*1000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            Integer num = queue.poll();
            System.out.printf("dequeue %d \n", num);
            // notFull.signal();
        } finally {
            consumerLock.unlock();
        }
        signalNotFull();
    }

    private void signalNotEmpty() {
        consumerLock.lock();
        try {
            notEmpty.signal();
        } finally {
            consumerLock.unlock();
        }
    }

    /**
     * Signals a waiting put. Called only from take/poll.
     */
    private void signalNotFull() {
        producerLock.lock();
        try {
            notFull.signal();
        } finally {
            producerLock.unlock();
        }
    }

}