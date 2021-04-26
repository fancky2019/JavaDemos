package Test.opensource.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/*
      *  Kafka 每个topic下有多个partion,每个partion有一个leader多个follower，
      *  Kafka 采用leader读写，follower备份，leader宕机，从zookeeper从follower中选举一个leader.
      *        producer往partion的leader写，follower发起同步。
      *        comsumer从partion的leader读
      *
      *
      *
      * enable.offset.commit设置为false,
      * auto.offset.reset=earliest/latest
      *  默认是latest。
      *
      *  Earliest
         当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
         Latest （默认）
         当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
        （之前的数据不消费，只消费当前新生产的数据）
         Error
         topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常


         消费：
         每个分区内的同一消息只会分同一消费组内的一个消费者，该分区的不同消息可分配给部分消费组的不同消费者
         *
         *

          顺序性消费：生产时候指定key
         *        //  Messages 中Key 决定消息的partion,内部hash(key)，如果不指定Key将随机指定分区（partition）
                //一个topic的partition数量最好大于消费者的数量
                /*如果客户端开启多个线程消费：要指定Key（可以是订单ID），因为同一Key数据分配到同一partition。
                  同一partition的数据只会分配给一个消费之，这样可以保证一个订单的新增、修改、删除的有序进行。

      */
public class KafkaConsumerClient {
    public void consumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "DemoConsumer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");//每次消费poll的个数

//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        String topic = "javaTest";

        //  Messages 中Key 决定消息的partion,内部hash(key)，如果不指定Key将随机指定分区（partition）
        //partition数量最好大于消费者的数量
                /*如果客户端开启多个线程消费：要指定Key（可以是订单ID），因为同一Key数据分配到同一partition。
                  同一partition的数据只会分配给一个消费之，这样可以保证一个订单的新增、修改、删除的有序进行。
                */
        consumer.subscribe(Collections.singletonList(topic));
        while (true) {
            //max.poll.records参数，默认拉取记录是500
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
//
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
                //手动提交
                consumer.commitSync();
            }
        }
    }
}
