package Test.test2019.queue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueTest {
    public void test() {
        CompletableFutureQueueTest();
    }

    private void CompletableFutureQueueTest() {
        //并发队列  可参照test2018下的 ProduceConsumerConditionTest 类
        ConcurrentLinkedQueue<Integer> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        //阻塞队列  可参照BlockingQueueTest
        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();
        CompletableFuture.runAsync(() ->
        {

        });
        CompletableFuture.supplyAsync(() ->
        {
            return 1;
        });

        //用返回值继续后面的操作，整个流程都是异步操作，这样就可以避免像C#里的事件回调，感觉被封装了
        CompletableFuture.supplyAsync(() ->
        {
            Integer n = 1;
            return n;
        }).thenAcceptAsync(p ->
        {
            Integer m = p;
            System.out.printf("thenAcceptAsync: m=%d\n", m);
        });
    }


}
