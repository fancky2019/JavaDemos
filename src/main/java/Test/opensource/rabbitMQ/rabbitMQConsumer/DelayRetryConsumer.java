package Test.opensource.rabbitMQ.rabbitMQConsumer;

import Model.Student;
import Test.opensource.rabbitMQ.ExchangeType;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    /// RabbitMQ重试：工作队列设置通过死信进入重试队列，在重试队列设置TTL（达到延迟目的）进入工作队列达到重试目的。
    ///
    ///  死信:
    ///       消息被拒绝（channel.BasicNack或channel.BasicReject）并且requeue=false.
    ///       消息TTL过期
    ///       队列达到最大长度（队列满了，无法再添加数据到mq中）
    ///
    ///       设置:x-dead-letter-exchange 指定死信送往的交换机
    ///       设置:x-dead-letter-routing-key 指定死信的routingkey
    ///
    ///
    /// 重复消费：
    ///         原因：发生超时消费者未ack消息，造成消息重新投递。
    ///         解决：对消息加主键，消费前到Redis判断是否消费，消费成功主键加入Redis.
 */
public class DelayRetryConsumer {
    public static final String EXCHANGE_NAME = "DirectExchangeJava";
    public static final String ROUTING_KEY = "DirectExchangeRoutingKeyJava";
    public static final String QUEUE_NAME = "DirectExchangeQueueJava";

    public static final String EXCHANGE_RETRY_NAME = "DirectRetryExchangeJava";
    public static final String ROUTING_RETRY_KEY = "DirectRetryExchangeRoutingKeyJava";
    public static final String QUEUE_RETRY_NAME = "DirectRetryExchangeQueueJava";

    public static final String EXCHANGE_FAILED_NAME = "DirectFailedExchangeJava";
    public static final String ROUTING_FAILED_KEY = "DirectFailedExchangeRoutingKeyJava";
    public static final String QUEUE_FAILED_NAME = "DirectFailedExchangeQueueJava";

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

            //region  重试
            channel.exchangeDeclare(EXCHANGE_RETRY_NAME, ExchangeType.DIRECT);
            Map<String, Object> mapRetry = new HashMap<>();
            ////设置死信进入失败队列
            mapRetry.put("x-message-ttl", 10000);
            mapRetry.put("x-dead-letter-exchange", EXCHANGE_NAME);
            mapRetry.put("x-dead-letter-routing-key", ROUTING_KEY);
            channel.queueDeclare(QUEUE_RETRY_NAME, true, false, false, mapRetry);
            channel.queueBind(QUEUE_RETRY_NAME, EXCHANGE_RETRY_NAME, ROUTING_RETRY_KEY);
            //endregion

            //region  失败
            channel.exchangeDeclare(EXCHANGE_FAILED_NAME, ExchangeType.DIRECT);
            channel.queueDeclare(QUEUE_FAILED_NAME, true, false, false, null);
            channel.queueBind(QUEUE_FAILED_NAME, EXCHANGE_FAILED_NAME, ROUTING_FAILED_KEY);
            //endregion

            //region 消费者
            channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.DIRECT);
            Boolean durable = true;
            Boolean exclusive = false;
            Boolean autoDelete = false;
            Map<String, Object> mapConsumer = new HashMap<>();
            //设置死信进入重试队列
            mapConsumer.put("x-dead-letter-exchange", EXCHANGE_RETRY_NAME);
            mapConsumer.put("x-dead-letter-routing-key", ROUTING_RETRY_KEY);
            channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, mapConsumer);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
            //endregion

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    String message = new String(delivery.getBody(), "UTF-8");
                    Student student = JSONObject.parseObject(message, Student.class);


                    Integer m = Integer.parseInt("m");

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);//发送客户端消息任务完成的应答
                    System.out.println("Received: key=" + delivery.getEnvelope().getRoutingKey() + "     msg=" + message);
                } catch (Exception ex) {
                    AMQP.BasicProperties basicProperties = delivery.getProperties();
                    Long retryCount = getRetryCount(basicProperties);
                    if (retryCount == 3) {
                        //把消息发送到失败队列同时Ack掉此条消息
                        //发送到失败队列
                        channel.basicPublish(EXCHANGE_FAILED_NAME, ROUTING_FAILED_KEY, null, delivery.getBody());
                        //Ack掉
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);//发送客户端消息任务完成的应答
                    } else {
                        //重试不足3次，继续拒绝（死信）以加入重试队列。
                        channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
                    }


                    try {
                        Thread.sleep(15000);
                    } catch (Exception exx) {

                    }

                }
            };
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });

        } catch (Exception ex) {
            System.out.println(" Received:" + ex.getMessage());
        }


    }

    public long getRetryCount(AMQP.BasicProperties properties) {
        long retryCount = 0L;
        Map<String, Object> header = properties.getHeaders();
        if (header != null && header.containsKey("x-death")) {
            List<Map<String, Object>> deaths = (List<Map<String, Object>>) header.get("x-death");
            if (deaths.size() > 0) {
                Map<String, Object> death = deaths.get(0);
                retryCount = (Long) death.get("count");
            }
        }
        return retryCount;
    }
}
