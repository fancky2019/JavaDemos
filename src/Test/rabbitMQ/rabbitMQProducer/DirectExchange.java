package Test.rabbitMQ.rabbitMQProducer;

import Test.rabbitMQ.ExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class DirectExchange {
    public static final String EXCHANGE_NAME = "DirectExchangeJava";
    public static final String ROUTING_KEY = "DirectExchangeRoutingKeyJava";

    public void producer() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.DIRECT);
                String message = "MSG_DirectExchange";
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes("UTF-8"));
                System.out.println(" Sent:" + message);
            }
        } catch (Exception ex) {

        }
    }
}
