package Test.test2021;

import Model.DelayQueueModel;
import Model.Student;
import Test.test2018.Person;
import io.netty.util.concurrent.CompleteFuture;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.AbstractQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

/*
    /// RabbitMQ重试：工作队列设置通过死信进入重试队列，在重试队列设置TTL（达到延迟目的）进入工作队列达到重试目的。
    ///
    ///  死信:
    ///       消息被拒绝（channel.BasicNack或channel.BasicReject）并且requeue=false.
    ///       消息TTL过期
    ///       队列达到最大长度（队列满了，无法再添加数据到mq中）
    ///
    ///       设置:x-dead-letter-exchange 指定死信送往的交换机
    ///       设置:x-dead-letter-routing-key 指定死信的routingkey
    ///
    ///
    /// 重复消费：
    ///         原因：发生超时消费者未ack消息，造成消息重新投递。
    ///         解决：对消息加主键，消费前到Redis判断是否消费，消费成功主键加入Redis.

    消费顺序性问题：搭建集群一台服务只去取一个队列（消息在一个队列是有序的），具体做法类似kafka的消息根据key存储到partition。把订单ID进行取模然后放大不通queue里
                 相当于把一个大的queue分解成几个小的queue然后每个队列只分给一个consumer。
 */
/**
 * public class DelayQueue<E extends Delayed> extends AbstractQueue<E>
 * implements BlockingQueue<E>
 *
 *
 * 内存队列性能好点，稳定性还是用rabbitMQ队列设置死信进入重试队列.
 */
public class DelayQueueTest {

    DelayQueue<DelayQueueModel<Student>> delayQueue = new DelayQueue<DelayQueueModel<Student>>();

    public void test() {
        produce();
        consumer();
    }

    private void produce() {
        CompletableFuture.runAsync(() ->
        {
            Student student = new Student();
            student.setName("fancky");
            DelayQueueModel<Student> delayQueueModel = new DelayQueueModel<>(student, 5 * 1000);

            Student student1 = new Student();
            student1.setName("fancky1");
            DelayQueueModel<Student> delayQueueModel1 = new DelayQueueModel<>(student, 12 * 1000);

            Student student2 = new Student();
            student2.setName("fancky2");
            DelayQueueModel<Student> delayQueueModel2 = new DelayQueueModel<>(student, 20 * 1000);

            delayQueue.put(delayQueueModel);
            delayQueue.put(delayQueueModel1);
            delayQueue.put(delayQueueModel2);
        });
    }

    private void consumer() {
        CompletableFuture.runAsync(()->
        {
            while (true) {
                try {
                    DelayQueueModel<Student> model = delayQueue.take();
                    System.out.println(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }
}
