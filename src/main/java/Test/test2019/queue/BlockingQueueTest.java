package Test.test2019.queue;

import Model.DelayQueueBean;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 内部通过ReentrantLock AtomicInteger shixian
 */
public class BlockingQueueTest {
    LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();

    public void test() {
        try {
            //生产者线程
            CompletableFuture.runAsync(() ->
            {
                while (true) {
                    for (int i = 1; i <= 10; i++) {
                        producer(i);
                    }

                }
            });


            //消费者线程
            CompletableFuture.runAsync(() ->
            {
                while (true) {

                    try {
                        consumer();
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

    private void producer(Integer i) {

        try {
            linkedBlockingQueue.put(i);
        } catch (Exception ex) {

        }


    }

    private void consumer() throws InterruptedException {
        //队列中没有值将会阻塞：notEmpty.await();
        Integer n = linkedBlockingQueue.take();
        System.out.println(n);
    }


    public void delayQueueFun() {
        DelayQueue<DelayQueueBean> delayQueue = new DelayQueue<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            //[0,30)
            long interval = random.nextInt(5000);
            DelayQueueBean delayQueueBean = new DelayQueueBean(interval, String.valueOf(i));
            delayQueue.offer(delayQueueBean);
        }

        //入队之后，会根据interval（延迟间隔）进行从小到大排序。

        //  Convert LocalDateTime to milliseconds since January 1, 1970, 00:00:00 GMT
        //   val milliseconds = now.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli();
        // epoch milliseconds to LocalDateTime
        // val newNow = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneOffset.UTC);


        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println(localDateTime.format(formatter));

        //阻塞队列出队方法
        //take :移除队首，队列没有元素，将阻塞直到生产者写入元素
        //poll :移除队首，队列没有元素，返回null。
        //peek :返回队首，不删除，队列没有元素，返回null。
        while (true) {
            try {
                //根据DelayQueueBean的Interval延迟时间出队，队列没有元素将阻塞。
                DelayQueueBean delayQueueBean = delayQueue.take();
                LocalDateTime executionTime = LocalDateTime.now();
                System.out.println(MessageFormat.format("interval:{0},createTime:{1} executionTime:{2},nowTime:{3}",
                        delayQueueBean.getInterval(),
                        delayQueueBean.getCreateTime().format(formatter),
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(delayQueueBean.getDelayTime()), ZoneOffset.UTC).format(formatter),
                        executionTime.format(formatter)));
            } catch (Exception ex) {

            }
        }

    }

}
