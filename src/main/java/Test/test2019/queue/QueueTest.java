package Test.test2019.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/*
LinkedBlockingQueue:阻塞队列，主要适用生产者消费者 ReentrantLock
ConcurrentLinkedQueue:非阻塞队列，主要适用多个线程访问数据，性能好 CAS
 */
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

    private void fun1() {
        //
        ArrayList arrayList = new ArrayList();
        List<Integer> list = new ArrayList<>();
        List<String> stringList = new LinkedList<>();

        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();
        ConcurrentLinkedQueue<Integer> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();
//        List<Integer>  list1=
    }


}
