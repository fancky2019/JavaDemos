package Test.rabbitMQ.rabbitMQProducer;

import Test.rabbitMQ.ExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/// http://www.rabbitmq.com/tutorials/tutorial-five-dotnet.html
/// 通配符模式（Topic Exchange）
/// 路由键支持模糊匹配，符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词
public class TopicExchange {
    public static final String EXCHANGE_NAME = "TopicExchangeJava";
    public static final String ROUTING_KEY = "TopicExchangeRoutingKeyJava.test";

    public void producer() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.TOPIC);
                //公平调度：客户端未处理完，不会再给它发送任务
                channel.basicQos(0, 1, false);
                String message = "MSG_TopicExchange";
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes("UTF-8"));
                System.out.println("Sent:"+message);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
