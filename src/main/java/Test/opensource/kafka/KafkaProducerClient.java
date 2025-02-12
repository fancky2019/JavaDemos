package Test.opensource.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;


/**
 *
 * kafka 对于业务量不是太大有延迟：producer要等到一定消息数量才会发送到broker，异步批量思想
 *
 *kafka支持 延迟消息、死信队列、
 *
 * 优先级的队列暂时未找到：网上说在生产消息时候用key 代替优先级 1-10 数字
 *
 * kafka 一个topic可以有多个partition,这些partition可以放在kafka的集群上（多个kafka broker）
 * 达到高可用，这些partition中只有一个leader,其他都是follower。kafka采用leader读写，replica
 * 备份。
 * Topic:一个业务。
 * partition:对应一个消费者。资源利用最大化。
 * 增加broker、partition、consumer增加吞吐量。
 *
 *
 * kafka 没有提供同步刷盘机制，由操作系统刷盘，消息发到broker 没刷盘就可能丢失
 * 设置过半副本同步
 * kafka 没有提供同步刷盘机制，由操作系统刷盘，消息发到broker 没刷盘就可能丢失。可配置刷盘策略或者副本同步机制
 *
 * //log.flush.interval.messages //多少条消息刷盘1次
 * //log.flush.interval.ms //隔多长时间刷盘1次
 * //log.flush.scheduler.interval.ms //周期性的刷盘。
 * //
 * //# 设置至少有2个副本 同步了数据
 * //replication.factor=2
 *主写主读
 *同一个Partition可能会有多个Replica，Replication之间选出一个Leader，Producer和Consumer只与这个Leader交互，
 * 其它Replica作为Follower从Leader中复制数据
 *
 *
 生产者确认生产成功。
 ACKS_CONFIG：
 acks=0: producer 不等待 Leader 确认，只管发出即可；最可能丢失消息，适用于高吞吐可丢失的业务；
 acks=1(默认值): producer 等待 Leader 写入本地日志后就确认；之后 Leader 向 Followers 同步时，如果 Leader 宕机会导致消息没同步而丢失，producer 却依旧认为成功；
 acks=all/-1: producer 等待 Leader 写入本地日志、而且 Leader 向 Followers 同步完成后才会确认；最可靠。
 kafka采用主写主读，不为-1可能造成数据丢失
 Messages 中Key 决定消息的partition,内部hash(key)，如果不指定Key将随机指定分区（partition）
 一个topic的partition数量最好大于消费者的数量
 如果客户端开启多个线程消费：要指定Key（可以是订单ID），因为同一Key数据分配到同一partition。
 同一partition的数据只会分配给一个消费之，这样可以保证一个订单的新增、修改、删除的有序进行。
 */
public class KafkaProducerClient {

    public static final String TOPIC = "topic1";
    public static final String KAFKA_SERVER_URL = "localhost";
    public static final int KAFKA_SERVER_PORT = 9092;
    public static final int KAFKA_PRODUCER_BUFFER_SIZE = 64 * 1024;
    public static final int CONNECTION_TIMEOUT = 100000;
    public static final String TOPIC2 = "topic2";
    public static final String TOPIC3 = "topic3";
    public static final String CLIENT_ID = "SimpleConsumerDemoClient";

    public void producer() {
        KafkaProducer<String, String> producer;
        String topic = "javaTest";
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "DemoProducer");
        props.put(ProducerConfig.ACKS_CONFIG, "-1");//生产成功确认，默认1
        //整形序列化在Kafka Tool 工具中看不到Key的值
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //顺序
//        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");

        producer = new KafkaProducer<>(props);

        boolean isAsync = true;

        int messageNo = 1;
        while (messageNo < 10) {
            //发送消息指定key,消息保存在同一个分区。key可以设置唯一的业务主键如订单号，或者设置表名字
            //同一张表的消息都落在一个分区内，或者topic
            String keyStr = String.valueOf(messageNo);
            String messageStr = "Message_" + messageNo;
            long startTime = System.currentTimeMillis();
            if (isAsync) {
                //  Messages 中Key 决定消息的partition,内部hash(key)，如果不指定Key将随机指定分区（partition）
                //一个topic的partition数量最好大于消费者的数量
                /*如果客户端开启多个线程消费：要指定Key（可以是订单ID snowflakeId），因为同一Key数据分配到同一partition。
                  同一partition的数据只会分配给一个消费者，这样可以保证一个订单的新增、修改、删除的有序进行。

                 topIC 业务队列：   1个topic就是一类数据，有多少种数据就建多少个topic就行
                  topic 有点像rabbitMQ的queue,存储消息，不过rabbitMQ消息是发到交换机根据生产指定的key路由到绑定的队列上
                        kafka直接将消息发送到指定的topic。
                         消费的时候跟rabbitMQ一样都是指定要消费的队列，kafka指定要消费的topic
                */
                // Send asynchronously  异步发送
                producer.send(new ProducerRecord<>(topic, keyStr, messageStr),
                        (RecordMetadata recordMetadata, Exception e) ->
                        {
                            long elapsedTime = System.currentTimeMillis() - startTime;
                            if (recordMetadata != null) {
                                //生产成功
                                System.out.println("message(" + keyStr + ", " + messageStr + ") sent to partition(" + recordMetadata.partition() +
                                        "), " +
                                        "offset(" + recordMetadata.offset() + ") in " + elapsedTime + " ms");
                            } else {
                                //生产异常
                                System.out.println(e.getMessage());
                            }
                        }
                );

            } else { // Send synchronously 同步发送
                try {

                    producer.send(new ProducerRecord<>(topic, keyStr, messageStr)).get();
                    System.out.println("Sent message: (" + messageNo + ", " + messageStr + ")");
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            ++messageNo;
            try {
                Thread.sleep(200);
            } catch (Exception ex) {

            }

        }

    }
}
