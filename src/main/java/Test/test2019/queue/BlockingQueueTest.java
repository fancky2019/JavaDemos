package Test.test2019.queue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

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
}
