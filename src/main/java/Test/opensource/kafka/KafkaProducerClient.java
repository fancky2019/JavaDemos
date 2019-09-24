package Test.opensource.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

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
        //整形序列化在Kafka Tool 工具中看不到Key的值
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //顺序
//        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");

        producer = new KafkaProducer<>(props);

        Boolean isAsync = true;

        int messageNo = 1;
        while (messageNo<10) {
            String keyStr=String.valueOf(messageNo);
            String messageStr = "Message_" + messageNo;
            long startTime = System.currentTimeMillis();
            if (isAsync) { // Send asynchronously
                producer.send(new ProducerRecord<>(topic, keyStr, messageStr),
                        (RecordMetadata recordMetadata, Exception e) ->
                        {
                            long elapsedTime = System.currentTimeMillis() - startTime;
                            if (recordMetadata != null) {
                                System.out.println(    "message(" + keyStr + ", " + messageStr + ") sent to partition(" + recordMetadata.partition() +
                                                "), " +
                                                "offset(" + recordMetadata.offset() + ") in " + elapsedTime + " ms");
                            } else {
                              System.out.println(e.getMessage());
                            }
                        }
                );

            } else { // Send synchronously
                try {

                    producer.send(new ProducerRecord<>(topic,  keyStr,  messageStr)).get();
                    System.out.println("Sent message: (" + messageNo + ", " + messageStr + ")");
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            ++messageNo;
            try {
                Thread.sleep(200);
            }
            catch (Exception ex)
            {

            }

        }

    }
}
