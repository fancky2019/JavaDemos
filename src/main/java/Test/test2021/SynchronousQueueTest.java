package Test.test2021;

import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.SynchronousQueue;

/**
 * SynchronousQueue 只有take 之后才能put 也就是队列长度是1
 */
public class SynchronousQueueTest {
    SynchronousQueue<Integer> synchronousQueue = new SynchronousQueue<>();

    public void test() {
        fun1();
    }

    private void fun1() {
        CompletableFuture.runAsync(() ->
        {
            consumer();
        });
        CompletableFuture.runAsync(() ->
        {
            producer();
        });
    }

    private void consumer() {
        for (int i = 0; i < 20; i++) {
            try {
                synchronousQueue.put(i);
                System.out.println("put " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    private void producer() {
        try {
            while (true) {
                int i = synchronousQueue.take();
                System.out.println("take " + i);
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
