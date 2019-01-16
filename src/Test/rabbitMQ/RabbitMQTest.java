package Test.rabbitMQ;

import java.util.concurrent.CompletableFuture;

public class RabbitMQTest {
    public void test() {
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


            Integer i = 0;
            for (; ; ) {
                //生产者不停推送，消费者会在DeliverCallback回调内收到消息
                new Test.rabbitMQ.rabbitMQProducer.DirectExchange().sendMsg(i.toString());
                i++;
                if (i == 10) {
                    break;
                }
                try {
                    Thread.sleep(3 * 1000);
                } catch (Exception ex) {

                }
            }

            //  new  Test.rabbitMQ.rabbitMQProducer.DirectExchange().producer();
//            new  Test.rabbitMQ.rabbitMQProducer.FanoutExchange().producer();
//            new Test.rabbitMQ.rabbitMQProducer.TopicExchange().producer();
        });


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

            new Test.rabbitMQ.rabbitMQConsumer.DirectExchange().consumer();
//            new Test.rabbitMQ.rabbitMQConsumer.FanoutExchange().consumer();
//            new Test.rabbitMQ.rabbitMQConsumer.TopicExchange().consumer();
        });
    }

}
