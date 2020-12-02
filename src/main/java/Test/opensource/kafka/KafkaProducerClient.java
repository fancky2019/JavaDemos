package Test.opensource.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;


/*
    * kafka 一个topic可以有多个partion,这些partion可以放在kafka的集群上（多个kafka broker）
    * 达到高可用，这些partion中只有一个leader,其他都是follower。kafka采用leader读写，replica
    * 备份。
    * Topic:一个业务。
    * partion:对应一个消费者。资源利用最大化。
    * 增加broker、partion、consumer增加吞吐量。
    *
    *    //生产者确认生产成功。

    ACKS_CONFIG：
    acks=0: producer 不等待 Leader 确认，只管发出即可；最可能丢失消息，适用于高吞吐可丢失的业务；
    acks=1(默认值): producer 等待 Leader 写入本地日志后就确认；之后 Leader 向 Followers 同步时，如果 Leader 宕机会导致消息没同步而丢失，producer 却依旧认为成功；
    acks=all/-1: producer 等待 Leader 写入本地日志、而且 Leader 向 Followers 同步完成后才会确认；最可靠。
    kafka采用主写主读，不为-1可能造成数据丢失

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

        Boolean isAsync = true;

        int messageNo = 1;
        while (messageNo < 10) {
            String keyStr = String.valueOf(messageNo);
            String messageStr = "Message_" + messageNo;
            long startTime = System.currentTimeMillis();
            if (isAsync) { // Send asynchronously
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

            } else { // Send synchronously
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
