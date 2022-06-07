package Test.opensource.rabbitMQ.rabbitMQConsumer;

import Model.Student;
import Test.opensource.rabbitMQ.*;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.Map;

/*

重复消费：
          原因：发生超时消费者未ack消息，造成消息重新投递。
          解决：对消息加主键，消费前到Redis判断是否消费，消费成功主键加入Redis.
 */
public class DirectExchange {
    /*
     *消息  channel.basicPublish的时候就持久化
     * 持久化：
     * Exchange：ExchangeDeclare 参数durable: true，宕机只保存Exchange元数据 ，Queue、Message丢失
     * Queue:QueueDeclare 参数durable: true         宕机只保存Queue元数据，Message丢失
     * Message:BasicProperties 属性 deliveryMode = 2 //2:持久化，1：不持久化;   宕机只保存Queue元数据。
     */

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
//            autoDelete属性
//            @Queue: 当所有消费客户端连接断开后，是否自动删除队列 true：删除false：不删除
//
//            @Exchange：当所有绑定队列都不在使用时，是否自动删除交换器 true：删除false：不删除

            channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.DIRECT);

            Boolean durable = true;
            Boolean exclusive = false;
            Boolean autoDelete = false;
            Map<String, Object> var = null;
            channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, var);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
            //消息分发给消费者回调
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
//                Student student = JSONObject.parseObject(message, Student.class);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);//发送客户端消息任务完成的应答
                System.out.println("Received: key=" + delivery.getEnvelope().getRoutingKey() + "     msg=" + message);
            };
            //消费指定队列
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });

        } catch (Exception ex) {
            System.out.println(" Received:" + ex.getMessage());
        }

    }
}
