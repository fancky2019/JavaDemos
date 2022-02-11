package Test.opensource.rabbitMQ;

import java.util.concurrent.CompletableFuture;

/**
 * 官方API:
 * https://www.rabbitmq.com/api-guide.html
 * 测试RabbitMQ确认模式生产10000耗时 14s左右，性能远低于redis的队列：生产10W，5.5s左右
 *
 * rabbitMQ集群采用镜像模式
 *
 *避免重复消费：消费前messageId入redis ,ack 之后设置redis中的messageId的key ttl 可以设置1天，尽量大点
 */
public class RabbitMQTest {


    /*
     rabbitMQ集群：https://www.cnblogs.com/lonely-wolf/p/14397704.html

    元数据:指的是包括队列名字属性、交换机的类型名字属性、绑定信息、vhost等基础信息，不包括队列中的消息数据。
    集群主要有两种模式：普通集群模式和镜像队列模式。
                   普通集群：各节点只存储相同的元数据，消息存在于不同节点。消费消息只能从一个节点读取，消息则从存储的节点转发到读取的节点机器。
                            如果一个节点宕机则消息无法消费，只能等待重启，且消息磁盘持久化。
                   镜像队列模式：各个节点保存相同的元数据和消息。类似redis主从模式。由于各节点同步会消耗带宽。
                              搭建： HAProxy + Keepalived 高可用集群
     */

    public void test() {

        //消费者
        CompletableFuture.runAsync(() ->
        {
            //DEMO  链接：http://www.rabbitmq.com/getstarted.html
            //NuGet添加RabbitMQ.Client引用
            //RabbitMQ UI管理:http://localhost:15672/   账号:guest 密码:guest
            //先启动订阅，然后启动发布
            //var factory = new ConnectionFactory(){ HostName = "192.168.1.121", Port = 5672 }; //HostName = "localhost",
            //用下面的实例化，不然报 None of the specified endpoints were reachable
            //var factory = new ConnectionFactory() { HostName = "192.168.1.121", Port = 5672, UserName = "fancky", Password = "123456" };

            //http://www.rabbitmq.com/tutorials/tutorial-three-dotnet.html
            //NuGet安装RabbitMQ.Client

//            new Test.opensource.rabbitMQ.rabbitMQConsumer.DelayRetryConsumer().consumer();
            new Test.opensource.rabbitMQ.rabbitMQConsumer.DirectExchange().consumer();
//            new Test.opensource.rabbitMQ.rabbitMQConsumer.FanoutExchange().consumer();
//            new Test.opensource.rabbitMQ.rabbitMQConsumer.TopicExchange().consumer();
        });


        //生产者
        CompletableFuture.runAsync(() ->
        {
            //DEMO  链接：http://www.rabbitmq.com/getstarted.html
            //NuGet添加RabbitMQ.Client引用
            //RabbitMQ UI管理:http://localhost:15672/   账号:guest 密码:guest
            //先启动订阅，然后启动发布
            //var factory = new ConnectionFactory(){ HostName = "192.168.1.121", Port = 5672 }; //HostName = "localhost",
            //用下面的实例化，不然报 None of the specified endpoints were reachable
            //var factory = new ConnectionFactory() { HostName = "192.168.1.121", Port = 5672, UserName = "fancky", Password = "123456" };

            //http://www.rabbitmq.com/tutorials/tutorial-three-dotnet.html
            //NuGet安装RabbitMQ.Client


//            Integer i = 0;
//            for (; ; ) {
//                //生产者不停推送，消费者会在DeliverCallback回调内收到消息
//                new Test.rabbitMQ.rabbitMQProducer.DirectExchange().sendMsg(i.toString());
//                i++;
//                if (i == 10) {
//                    break;
//                }
//                try {
//                    Thread.sleep(3 * 1000);
//                } catch (Exception ex) {
//
//                }
//            }

            new Test.opensource.rabbitMQ.rabbitMQProducer.DirectExchange().produceIndividually();
//            new Test.opensource.rabbitMQ.rabbitMQProducer.DirectExchange().produceInBatch();
//            new Test.opensource.rabbitMQ.rabbitMQProducer.DirectExchange().produceInBatchAsync();


//            new Test.opensource.rabbitMQ.rabbitMQProducer.FanoutExchange().producer();
//            new Test.opensource.rabbitMQ.rabbitMQProducer.TopicExchange().producer();
        });


    }

}
