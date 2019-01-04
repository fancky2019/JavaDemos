package Test;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProducerConsumerTPSTest {
    private ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
    Queue<Date> executeTimeList = new LinkedList<>();
    private Integer maxLength;
    private Integer tps;
    private final Object lockObject = new Object();
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    public ProducerConsumerTPSTest(Integer maxLength, Integer tps) {
        this.maxLength = maxLength;
        this.tps = tps;
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
                    Thread.sleep(100);
                } catch (InterruptedException ex) {

                }

            }
        });

    }

    void producer(Integer num) {
        synchronized (lockObject) {
            //貌似if也可以，没有像C#那么大的毛病，
            while (queue.size() == maxLength) {
                try {
                    lockObject.wait();
                } catch (InterruptedException ex) {
                    String msg = ex.getMessage();
                }
            }
            queue.add(num);
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
                }
            }
            //如果执行等于TPS
            if (executeTimeList.size() == tps) {
                Date firstTime = executeTimeList.poll();
                Date now = new Date();
                Long millisecond = now.getTime() - firstTime.getTime();
                //执行间隔小于1s，等待
                if (millisecond < 1000) {
                    try {
                        Thread.sleep(1000 - millisecond + 1);
                    } catch (InterruptedException ex) {
                        String msg = ex.getMessage();
                    }
                }
            }

            Integer num = queue.poll();
            Date date=new Date();
            executeTimeList.add(date);
            System.out.printf("dequeue: %d  time:%s \n", num, simpleDateFormat.format(date));
            lockObject.notify();

            //不用每次都通知，当队列满了再通知，释放锁
//            if(queue.size()==maxLength-1)
//            {
//                lockObject.notify();
//            }

        }

    }
}
