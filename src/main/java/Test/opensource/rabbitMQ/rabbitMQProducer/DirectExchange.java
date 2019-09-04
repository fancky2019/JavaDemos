package Test.opensource.rabbitMQ.rabbitMQProducer;

import Model.Student;
import Test.opensource.rabbitMQ.ExchangeType;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 官方API:
 * https://www.rabbitmq.com/api-guide.html
 */
public class DirectExchange {

    /*
     * 持久化：
     * Exchange：ExchangeDeclare 参数durable: true，宕机只保存Exchange元数据 ，Queue、Message丢失
     * Queue:QueueDeclare 参数durable: true         宕机只保存Queue元数据，Message丢失
     * Message:BasicProperties 属性 deliveryMode = 2 //2:持久化，1：不持久化;   宕机只保存Queue元数据。
     */

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
                Student student = new Student();
                student.setName("fancky");
                message = JSONObject.toJSONString(student);


                Map<String, Object> headers = new HashMap<String, Object>();
                headers.put("my1", "1111");
                headers.put("my2", "2222");

                AMQP.BasicProperties basicProperties1 = new AMQP.BasicProperties().builder()
                        //  .deliveryMode(2) //   // Sets RabbitMQ.Client.IBasicProperties.DeliveryMode to either persistent (2)  or non-persistent (1).
                        //                //2:持久化，1：不持久化
                        //   .contentEncoding("UTF-8") // 编码方式
                        //  .expiration("10000") // 过期时间
                        // .headers(headers) //自定义属性
                        .build();

/*
回退采用此方法。
void basicPublish(String exchange, String routingKey, boolean mandatory, BasicProperties props, byte[] body) throws IOException;


下面这个方法被官方废弃，性能问题
void basicPublish(String var1, String var2, boolean mandatory, boolean immediate, BasicProperties props, byte[] body) throws IOException;
 */



                //  BasicProperties 默认为：   MessageProperties.MINIMAL_BASIC,不持久化
                //mandatory设置为true,第三个参数
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,true, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
              //没有路由的消息返回。RabbitMQ会调用Basic.Return命令将消息返回给生产者。当mandatory参数设置为false时，出现上述情形的话，消息直接被丢弃
              //  //没有路由的消息将会回退,消息没有找到可路由转发的队里，立即回发给生产者。
                channel.addReturnListener((int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties basicProperties, byte[] body) -> {
                    String msg = new String(body);
                    System.out.println("Basic.Return返回的结果是：" + msg);
                });

                System.out.println(" Sent:" + message);

            }
        } catch (Exception ex) {

        }
    }
}
