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
 *
 * rabbitmq则不支持批量生产,支持批量消费
 *
 * 可用性：rabbitmq 生产者默认异步发送消息路由到绑定队列，暂时没找到同步发送。不像rocketmq和kafka 支生产者持同步发送异步发送
 *
 *
 * RabbitMQ 的消费模式分为两种：推模式和拉模式。
 *
 * 推模式（Push）：消息中间件主动将消息推送给消费者，推模式采用 Basic.Consume 进行消费。
 * 拉模式（Pull）：消费者主动从消息中间件拉取消息，拉模式则是调用 Basic.Get 进行消费。
 *
 *
 *
 *
 */
public class RabbitMQTest {


    /*
     rabbitMQ集群：https://www.cnblogs.com/lonely-wolf/p/14397704.html
RabbitMQ的集群根据存储类型可以分成两种类型：内存节点和磁盘节点。这里存储的是metadata，内存节点保存在内存中，磁盘节点保存在磁盘中。

   普通模式：默认的集群模式。所谓的普通模式就是，多台机器上启动多个Rabbitmq实例，每台机器启动一个实例。但是创建的queue，
   只会放在一个Rabbitmq实例上，每个实例都会同步queue的元数据。消费的时候，
   如果连接到了另外一个实例，那么那个实例会从queue所在实例上拉取数据过来

     仲裁队列（Quorum Queue）

    元数据:指的是包括队列名字属性、交换机的类型名字属性、绑定信息、vhost等基础信息，不包括队列中的消息数据。
    集群主要有两种模式：普通集群模式和镜像队列模式。
                   普通集群：各节点只存储相同的元数据，消息存在于不同节点。消费消息只能从一个节点读取，消息则从存储的节点转发到读取的节点机器。
                            如果一个节点宕机则消息无法消费，只能等待重启，且消息磁盘持久化。
                   镜像队列模式：各个节点保存相同的元数据和消息。类似redis主从模式。由于各节点同步会消耗带宽。
                              搭建： HAProxy + Keepalived 高可用集群

//        消息状态：ready:准备发送给消费之
//        unacked:发送给消费者消费还没有ack
//        total：总消息数量=ready+unacked
        //prefetchCount:每次预取10条信息放在线程的消费队列里，该线程还是1条一条从从该线程的缓冲队列里取消费。直到
        //缓冲队列里的消息消费完，再从mq的队列里取。
        // 调试可到mq插件查看 ready unacked 消息数量，打印消费者消费线程的消息id
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
