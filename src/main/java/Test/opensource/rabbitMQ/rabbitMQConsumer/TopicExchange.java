package Test.opensource.rabbitMQ.rabbitMQConsumer;

import Test.opensource.rabbitMQ.ExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.Map;

/// http://www.rabbitmq.com/tutorials/tutorial-five-dotnet.html
/// 通配符模式（Topic Exchange）
/// 路由键支持模糊匹配，符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词
public class TopicExchange {
    public static final String EXCHANGE_NAME = "TopicExchangeJava";
    public static final String ROUTING_KEY = "TopicExchangeRoutingKeyJava.*";
    public static final String QUEUE_NAME = "TopicExchangeQueueJava";

    public void consumer() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.TOPIC);
            //    String queueName = channel.queueDeclare().getQueue();
            Boolean durable = true;
            Boolean exclusive = false;
            Boolean autoDelete = false;
            Map<String, Object> var = null;
            channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, var);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);//发送客户端消息任务完成的应答
                System.out.println("Received: key=" + delivery.getEnvelope().getRoutingKey() + "     msg=" + message);
            };
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
