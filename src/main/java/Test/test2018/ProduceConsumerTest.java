package Test.test2018;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * java LinkedBlockingQueue Demo  在TheadTest类里
 *  * Condition 的await、signal要获得的锁内
 *  * object notify() wait()
 */
public class ProduceConsumerTest {
    private ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
    private Integer maxLength;
    private final Object lockObject = new Object();
    private final Object PRODUCER_LOCK_OBJECT = new Object();
    private final Object CONSUMER_LOCK_OBJECT = new Object();
    public ProduceConsumerTest(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public void Test() {

        //生产者线程
        CompletableFuture.runAsync(() ->
        {
            while (true) {
                for (int i = 1; i <= 1000; i++) {
                    producer(i);
                }

            }
        });


        //消费者线程
        CompletableFuture.runAsync(() ->
        {
            while (true) {
                consumer();
                try {
                    Thread.sleep(800);
                } catch (InterruptedException ex) {

                }

            }
        });

    }

    void producer(Integer num) {
        //synchronized (lockObject)如果队列达到最大长度，此时还没有消费者，此处将产生死锁。应该synchronized（PRODUCER_LOCK_OBJECT）和synchronized（CONSUMER_LOCK_OBJECT）
        //具体实现参见ProduceConsumerConditionTest 类似。
        synchronized (lockObject) {
            //貌似if也可以，没有像C#那么大的毛病，
            while (queue.size() == maxLength) {
                try {
                    //。调用wait()会使Java线程进入到WAITING状态，调用wait(long time)会使Java线程进入到TIMED_WAITING状态。
                    lockObject.wait();
                } catch (InterruptedException ex) {

                }
            }
            queue.offer(num);
            System.out.printf("enqueue %d \n", num);
            lockObject.notify();
        }

    }

    void consumer() {

        synchronized (lockObject) {
            while (queue.isEmpty()) {
                try {
                    lockObject.wait();
                } catch (InterruptedException ex) {
                    String msg = ex.getMessage();
                }
            }
            Integer num = queue.poll();
            System.out.printf("dequeue %d \n", num);
            lockObject.notify();

            //不用每次都通知，当队列满了再通知，释放锁
//            if(queue.size()==maxLength-1)
//            {
//                lockObject.notify();
//            }

        }

    }


}
