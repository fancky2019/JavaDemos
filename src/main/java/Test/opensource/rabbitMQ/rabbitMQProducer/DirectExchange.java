package Test.opensource.rabbitMQ.rabbitMQProducer;

import Model.Student;
import Test.opensource.rabbitMQ.ExchangeType;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

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

                AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                      //  .deliveryMode(2) //   // Sets RabbitMQ.Client.IBasicProperties.DeliveryMode to either persistent (2)  or non-persistent (1).
                        //                //2:持久化，1：不持久化
                     //   .contentEncoding("UTF-8") // 编码方式
                      //  .expiration("10000") // 过期时间
                        // .headers(headers) //自定义属性
                        .build();


              //  BasicProperties 默认为：   MessageProperties.MINIMAL_BASIC,不持久化
                  channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.MINIMAL_PERSISTENT_BASIC, message.getBytes("UTF-8"));
                System.out.println(" Sent:" + message);

            }
        } catch (Exception ex) {

        }
    }
}
