package Test.opensource.rabbitMQ.rabbitMQProducer;

import Test.opensource.rabbitMQ.ExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class DirectExchange {
    public static final String EXCHANGE_NAME = "DirectExchangeJava";
    public static final String ROUTING_KEY = "DirectExchangeRoutingKeyJava";

//    static ConnectionFactory factory;

//    static {
//        factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        factory.setPort(5672);
//        factory.setUsername("fancky");
//        factory.setPassword("123456");
//    }
//
//    public void sendMsg(String msg) {
//        try {
//            try (Connection connection = factory.newConnection();
//                 Channel channel = connection.createChannel()) {
//                channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.DIRECT);
//                //公平调度：客户端未处理完，不会再给它发送任务
//                channel.basicQos(0, 1, false);
//                String message = "MSG_DirectExchange" + msg;
//                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes("UTF-8"));
//                System.out.println(" Sent:" + message);
//            }
//        } catch (Exception ex) {
//
//        }
//    }


    public void producer() {
        try {
            //{ HostName = "192.168.1.105", Port = 5672, UserName = "fancky", Password = "123456" };
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");


            factory.setPort(5672);
            factory.setUsername("fancky");
            factory.setPassword("123456");


            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.DIRECT);
                //公平调度：客户端未处理完，不会再给它发送任务
                channel.basicQos(0, 1, false);
                String message = "MSG_DirectExchange";
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes("UTF-8"));
                System.out.println(" Sent:" + message);

            }
        } catch (Exception ex) {

        }
    }
}
