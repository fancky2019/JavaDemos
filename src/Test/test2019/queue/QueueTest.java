package Test.test2019.queue;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueTest {
    public  void test()
    {
        //并发队列
        ConcurrentLinkedQueue<Integer> concurrentLinkedQueue=new ConcurrentLinkedQueue<>() ;
        //阻塞队列
        LinkedBlockingQueue<Integer> linkedBlockingQueue=new LinkedBlockingQueue<>() ;
    }

}
