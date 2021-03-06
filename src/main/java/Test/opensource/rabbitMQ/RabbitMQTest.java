package Test.opensource.rabbitMQ;

import java.util.concurrent.CompletableFuture;

/**
 * 官方API:
 * https://www.rabbitmq.com/api-guide.html
 * 测试RabbitMQ确认模式生产10000耗时 14s左右，性能远低于redis的队列：生产10W，5.5s左右
 */
public class RabbitMQTest {
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
