package Test.opensource.kafka;

import java.util.concurrent.CompletableFuture;

public class KafkaTest {
    public void test() {
        CompletableFuture.runAsync(() ->
        {
            new KafkaConsumerClient().consumer();
        });
        CompletableFuture.runAsync(() ->
        {
            new KafkaProducerClient().producer();
        });
    }
}
