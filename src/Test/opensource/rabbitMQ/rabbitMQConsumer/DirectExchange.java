package Test.opensource.rabbitMQ.rabbitMQConsumer;

import Model.Student;
import Test.opensource.rabbitMQ.*;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.Map;

public class DirectExchange {
    public static final String EXCHANGE_NAME = "DirectExchangeJava";
    public static final String ROUTING_KEY = "DirectExchangeRoutingKeyJava";
    public static final String QUEUE_NAME = "DirectExchangeQueueJava";

    public void consumer() {
        try {
            //{ HostName = "192.168.1.105", Port = 5672, UserName = "fancky", Password = "123456" };
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");


            factory.setPort(5672);
            factory.setUsername("fancky");
            factory.setPassword("123456");

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();


            channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.DIRECT);

            Boolean durable = true;
            Boolean exclusive = false;
            Boolean autoDelete = false;
            Map<String, Object> var = null;
            channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, var);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                Student student= JSONObject.parseObject(message,Student.class);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);//发送客户端消息任务完成的应答
                System.out.println("Received: key=" + delivery.getEnvelope().getRoutingKey() + "     msg=" + message);
            };
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });

        } catch (Exception ex) {
            System.out.println(" Received:" + ex.getMessage());
        }

    }
}
