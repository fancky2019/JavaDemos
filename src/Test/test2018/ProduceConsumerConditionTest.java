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