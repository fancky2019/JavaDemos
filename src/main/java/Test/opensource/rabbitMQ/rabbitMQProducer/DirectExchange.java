package Test.opensource.rabbitMQ.rabbitMQProducer;

import Model.Student;
import Test.opensource.rabbitMQ.ExchangeType;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;

/**
 * 官方API:
 * https://www.rabbitmq.com/api-guide.html
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


    public void produceIndividually() {
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


                //在头部自定义一些信息，类似http的请求头
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
                //将信道设置成确认模式
                channel.confirmSelect();

                //测试RabbitMQ确认模式生产10000耗时 14s左右，性能远低于redis的队列：生产10W，5.5s左右
                Stopwatch stopwatch = Stopwatch.createStarted();
                for (int i = 0; i < 10000; i++) {
                    message = MessageFormat.format("message - {0}", i);

                    /*
                     投递消息的时候指定了交换机名称：就指定了交换机的类型，路由key ：根据交换机和队列的绑定关系交换机就可以将消息投递到对应的队列
                     */


                    /*
                    一、mandatory 参数
                        当mandatory 参数设为 true 时，交换器无法根据自身的类型和路由键找到一个符合条件 的队列，那么 RabbitMQ 会调用 Basic.Return 命令将消息返回给生产者 。当 mandatory 数设置为 false 时，出现上述情形，则消息直接被丢弃,那么生产者如何获取到没有被正确路由到合适队列的消息呢?这时候可以通过调用 channel addReturnListener 来添加 ReturnListener 监昕器实现。

                        二、immediate 参数  rabbitMQ3.0删除改参数
                        imrnediate 参数设为 true 时，如果交换器在将消息路由到队列时发现队列上并不存在 任何消费者，那么这条消息将不会存入队列中。当与路由键匹配的所有队列都没有消费者时 该消息会通过 Basic Return 返回至生产者。
                        。
                     */
                    //  BasicProperties 默认为：   MessageProperties.MINIMAL_BASIC,不持久化
                    //mandatory设置为true,第三个参数
                    channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, true, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
                    //没有路由的消息返回。RabbitMQ会调用Basic.Return命令将消息返回给生产者。当mandatory参数设置为false时，出现上述情形的话，消息直接被丢弃
                    //  //没有路由的消息将会回退,消息没有找到可路由转发的队里，立即回发给生产者。
                    channel.addReturnListener((int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties basicProperties, byte[] body) -> {
                        String msg = new String(body);
                        System.out.println("Basic.Return返回的结果是：" + msg);
                    });

                    //同步确认生产成功。
                    try {
                        channel.waitForConfirmsOrDie(5_000);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        System.out.println("生产失败！");
                    }


                }

                stopwatch.stop();
                System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));

            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void produceInBatch() {
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
                //将信道设置成确认模式
                channel.confirmSelect();

                //没有路由的消息返回。RabbitMQ会调用Basic.Return命令将消息返回给生产者。当mandatory参数设置为false时，出现上述情形的话，消息直接被丢弃
                //  //没有路由的消息将会回退,消息没有找到可路由转发的队里，立即回发给生产者。
                channel.addReturnListener((int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties basicProperties, byte[] body) -> {
                    String msg = new String(body);
                    System.out.println("Basic.Return返回的结果是：" + msg);
                });

                Student student = new Student();
                for (int i = 0; i < 100; i++) {
                    student.setName(MessageFormat.format("fancky{0}", i));
                    String message = JSONObject.toJSONString(student);
                    //  BasicProperties 默认为：   MessageProperties.MINIMAL_BASIC,不持久化
                    //mandatory设置为true,第三个参数
                    channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, true, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
                }
                try {
                    channel.waitForConfirmsOrDie(5_000);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("生产失败！");
                }


            }
        } catch (Exception ex) {

        }
    }


    //    ConcurrentHashMap<Long,String> outstandingConfirms=new ConcurrentHashMap<>();
    //ConcurrentSkipListMap的Key是有序的，操作性能好点
    ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

    /**
     * 批量异步插入，性能最好,
     * Waiting for a batch of messages to be confirmed improves throughput drastically over waiting for a confirm for individual message
     * (up to 20-30 times with a remote RabbitMQ node).
     */
    public void produceInBatchAsync() {
        try {
            //{ HostName = "192.168.1.105", Port = 5672, UserName = "fancky", Password = "123456" };
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");


            factory.setPort(5672);
            factory.setUsername("fancky");
            factory.setPassword("123456");


            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                //避免消息积压，采取流控
                //因为流控而阻塞：提高消费能力解决消息积压，而不是采用流控。
                connection.addBlockedListener(str ->
                        {
                            //阻塞
                        },
                        () ->
                        {
                            //为阻塞
                        });


                channel.exchangeDeclare(EXCHANGE_NAME, ExchangeType.DIRECT);
                //公平调度：客户端未处理完，不会再给它发送任务
                channel.basicQos(0, 1, false);


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
                //将信道设置成确认模式
                channel.confirmSelect();


                //生产确认成功回调
                //https://www.rabbitmq.com/tutorials/tutorial-seven-java.html
                channel.addConfirmListener((sequenceNumber, multiple) -> {
                    // code when message is confirmed
                    //如果是批量确认，从内存中移除小于该发布序列号的数据。channel.NextPublishSeqNo：自增的UInt64
                    //Broker返回的Multiple可能是false或true
                    if (multiple) {
                        ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(sequenceNumber, true);
                        confirmed.clear();
                    } else {
                        outstandingConfirms.remove(sequenceNumber);
                    }
                }, (sequenceNumber, multiple) -> {
                    // code when message is nack-ed

                    String body = outstandingConfirms.get(sequenceNumber);
                    System.err.format("Message with body %s has been nack-ed. Sequence number: %d, multiple: %b%n", body, sequenceNumber, multiple);
                });

                //没有路由的消息返回。RabbitMQ会调用Basic.Return命令将消息返回给生产者。当mandatory参数设置为false时，出现上述情形的话，消息直接被丢弃
                //  //没有路由的消息将会回退,消息没有找到可路由转发的队里，立即回发给生产者。
                channel.addReturnListener((int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties basicProperties, byte[] body) -> {
                    String msg = new String(body);
                    System.out.println("Basic.Return返回的结果是：" + msg);
                });

                Student student = new Student();
                for (int i = 0; i < 100; i++) {
                    student.setName(MessageFormat.format("fancky{0}", i));
                    String message = JSONObject.toJSONString(student);
                    //  BasicProperties 默认为：   MessageProperties.MINIMAL_BASIC,不持久化
                    //mandatory设置为true,第三个参数
                    channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, true, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
                }
                try {
                    channel.waitForConfirmsOrDie(5_000);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("生产失败！");
                }


            }
        } catch (Exception ex) {

        }
    }
}
