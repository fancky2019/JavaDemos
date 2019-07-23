package Test.opensource.rabbitMQ.rabbitMQProducer;

import Test.opensource.rabbitMQ.ExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class FanoutExchange {

    public static final String EXCHANGE_NAME = "FanoutExchangeJava";
    // public static final String ROUTING_KEY = "DirectExchangeRoutingKey";

    public void producer() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.FANOUT);
                //公平调度：客户端未处理完，不会再给它发送任务
                channel.basicQos(0, 1, false);
                String message = "MSG_FanoutExchangeJava";
                //参数：s:交换机,s1:RoutingKey
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
                System.out.println("Send:" + message);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
