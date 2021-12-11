package Test.test2019;

import io.netty.util.concurrent.CompleteFuture;

import java.text.MessageFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueueTest {
    /*
    add/offer/put的区别：
1、 add: 和collection的add一样，当队列满时，调用add()方法抛出异常IllegalStateException异常
2、 调用offer()方法当队列满时返回的 false。
3、调用put方法时候，当当前队列等于设置的最大长度时，将阻塞，直到能够有空间插入元素

remove/poll/take的区别
1、remove，remove()是从队列中删除第一个元素。remove() 的行为与 Collection 接口的相似
2、poll，但是新的 poll() 方法时有值时返回值，在空集合调用时不是抛出异常，只是返回 null,
3、take: 获取并移除此队列的头部，在元素变得可用之前一直等待 。queue的长度 == 0 的时候，一直阻塞

peek，element区别：
检测操作，element() 和 peek() 用于在队列的头部查询元素。与 remove() 方法类似，在队列为空时， element() 抛出一个IllegalStateException异常，
而 peek() 返回 null，Queue.java源码



put--take  阻塞
offer-- poll  bool、值



add()	增加一个元素	如果队列已满，则抛出一个IIIegaISlabEepeplian异常
remove()	移除并返回队列头部的元素	如果队列为空，则抛出一个NoSuchElementException异常
element()	返回队列头部的元素	如果队列为空，则抛出一个NoSuchElementException异常
offer()	添加一个元素并返回true	如果队列已满，则返回false
poll()	移除并返问队列头部的元素	如果队列为空，则返回null
peek()	返回队列头部的元素	如果队列为空，则返回null
put()	添加一个元素	如果队列满，则阻塞
take()	移除并返回队列头部的元素	如果队列为空，则阻塞
     */
    BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(10);

    public void test() {
        CompletableFuture.runAsync(() ->
        {
            int i = 0;
            while (true) {
                i++;
                producer(MessageFormat.format("msg{0}", i));
            }

        });


        CompletableFuture.runAsync(() ->
        {
            while (true) {
                consumer();
            }
        });

    }

    private void producer(String msg) {
//        blockingQueue.offer(msg);//返回 true、false
        try {
            blockingQueue.put(msg);
            System.out.println(MessageFormat.format("sendMsg{0}", msg));
        } catch (Exception ex) {

        }
    }

    private void consumer() {
        try {
            String reciveMsg = blockingQueue.take();
            System.out.println(MessageFormat.format("receiveMsg:{0}", reciveMsg));
            Thread.sleep(250);
        } catch (Exception ex) {

        }


    }
}
