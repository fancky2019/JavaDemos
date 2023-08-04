package Test.opensource.Netty.mqtt.protocol;


import Test.opensource.Netty.NettyProduction.MqttNettyClientHandler;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import utility.Action;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MqttNettyClient {
    Bootstrap bootstrap;
    Channel channel;
    String host = "localhost";
    int port = 23450;
    private String userName="test";


    private String password="test";
    public MqttNettyClient(String host, Integer port) {
        this.host = host;
        this.port = port;
        try {
            init();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public MqttNettyClient() {
        try {
            init();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    void init() throws Exception {
        //SSL
        boolean SSL=false;
        final SslContext sslCtx;
        if (SSL) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }



        EventLoopGroup workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap(); // (1)
        bootstrap.group(workerGroup); // (2)
        bootstrap.channel(NioSocketChannel.class); // (3)
//        bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {

                //SSL
                ChannelPipeline p = ch.pipeline();
                if (sslCtx != null) {
                    p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                }



                //设置allIdleTime=readerIdleTime*3,如果三次都没收到心跳信息就认为断线了
                ch.pipeline().addLast(new IdleStateHandler(2, 2, 6, TimeUnit.SECONDS));
                //Marshalling 优化jdk序列化，内部进行粘包处理。不用再设置frameDecoder、frameEncoder
                //其他编码解码器参见 NettySampleClient NettySampleServer 类设置的frameDecoder和frameEncoder进行粘包处理
                ch.pipeline().addLast("encoder", MqttEncoder.INSTANCE);
                ch.pipeline().addLast("decoder", new MqttDecoder());

                //注意不new ，通过this访问某个类的实例
                ch.pipeline().addLast(new MqttNettyClientHandler(MqttNettyClient.this));
            }
        });

    }

    public void connect(Action action) {

        if (channel != null && channel.isActive()) {
            return;
        }
        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port);

            /*
            Netty 操作都是异步的。
            connect 方法返回ChannelFuture，当连接完成，执行ChannelFutureListener的匿名内部类的operationComplete
           ChannelFutureListener 接口继承GenericFutureListener接口
            少用lambda表达式，不然看不到起实现原理
             */
//            channelFuture.addListener(new GenericFutureListener<ChannelFuture>() {
//                @Override
//                public void operationComplete(ChannelFuture channelFuture) throws Exception {
//
//                }
//            });
//            channelFuture.addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture channelFuture) throws Exception {
//
//                }
//            });
            //Channel连接成功，执行Listener方法。
            channelFuture.addListener(p -> {
                ChannelFuture ch = (ChannelFuture) p;
                if (ch.isSuccess()) {
                    channel = ch.channel();
                    System.out.println("连接成功");
                    if (action != null) {
                        action.callBack();
                    }
                } else {
                    System.out.println("每隔2s重连....");
                    ch.channel().eventLoop().schedule(() -> connect(action), 2, TimeUnit.SECONDS);
                }
            });

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /*
    注：消息要实现Serializable接口
     */
    public void sendData() {

//        if(channel.isActive()&&channel.isActive())
//        {
//
//        }
//        else {
//            //进入失败队列，重新发送
//        }



//        MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader("MQTT", 4, true, true, false, 0, false, true, 60);
//        String uuid = UUID.randomUUID().toString().replace("-", "");
//        MqttConnectPayload connectPayload = new MqttConnectPayload(uuid, null, null, userName, password.getBytes(CharsetUtil.UTF_8));
//        MqttFixedHeader mqttFixedHeaderInfo = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_LEAST_ONCE, false, 0);
//        MqttConnectMessage connectMessage = new MqttConnectMessage(mqttFixedHeaderInfo, mqttConnectVariableHeader, connectPayload);
////        ctx.writeAndFlush(connectMessage);
//        channel.writeAndFlush(connectMessage);

//        channel.writeAndFlush("dssddssddsdsdssd");
    }

}
