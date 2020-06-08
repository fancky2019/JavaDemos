package Test.opensource.Netty.NettySample;

import Test.opensource.Netty.MarshallingCodeFactory;
import Test.opensource.Netty.MessageInfo;
import Test.opensource.Netty.MessageType;
import Test.opensource.Netty.NettyProduction.ClientBusinessHandler;
import Test.opensource.Netty.NettyProduction.NettyClientProduction;
import common.Action;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class NettySampleClient {
    Bootstrap bootstrap;
    Channel channel;
    String host = "localhost";
    int port = 8031;

    public void test() {
        CompletableFuture.runAsync(() ->
        {
            try {
                Thread.sleep(100);
                runClient();
                connect(()->
                {
//                    sendData(null);
                });
                Thread.sleep(2000);
                //延迟2秒等待连接成功

                //在连接成功在发送数据，netty是异步的， connect(null);调用完不一定连接成功
//                sendData(null);
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }

        });


    }

    private void runClient() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap(); // (1)
        bootstrap.group(workerGroup); // (2)
        bootstrap.channel(NioSocketChannel.class); // (3)
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                //设置allIdleTime=readerIdleTime*3,如果三次都没收到心跳信息就认为断线了
                ch.pipeline().addLast(new IdleStateHandler(2, 2, 6, TimeUnit.SECONDS));
                ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder());
                ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingEncoder());
                NettySampleClientHandler nettySampleClientHandler = new NettySampleClientHandler();
                nettySampleClientHandler.dicConnect = new Consumer() {
                    @Override
                    public void accept(Object o) {
                        connect(null);
                    }
                };
                ch.pipeline().addLast(nettySampleClientHandler);
            }
        });
    }

    private void connect(Action action) {

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
            System.out.println(ex.toString());
        }
    }

    public void sendData(MessageInfo messageInfo) {

        try {

            if (channel == null && !channel.isActive()) {
                return;
            }
            String line = "sendMessage";
            for (Integer i = 1; i <= 20; i++) {
                MessageInfo msg = new MessageInfo();
                msg.setMessageType(MessageType.HeartBeat);
                msg.setBody(line + i.toString());
                channel.writeAndFlush(msg);


//                 channel.writeAndFlush(line+i.toString());
                Thread.sleep(500);
            }

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }


}
