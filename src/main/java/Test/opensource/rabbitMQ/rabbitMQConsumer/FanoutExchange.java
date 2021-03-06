package Test.opensource.rabbitMQ.rabbitMQConsumer;

import Test.opensource.rabbitMQ.ExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.Map;

public class FanoutExchange {
    public static final String EXCHANGE_NAME = "FanoutExchangeJava";
    public static final String QUEUE_NAME = "FanoutExchangeQueueJava";

    public void consumer() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.FANOUT);

            Boolean durable = true;
            Boolean exclusive = false;
            Boolean autoDelete = false;
            Map<String, Object> var = null;
            channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, var);
            //String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);//发送客户端消息任务完成的应答
                System.out.println("Received:" + message);
            };
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }
}
