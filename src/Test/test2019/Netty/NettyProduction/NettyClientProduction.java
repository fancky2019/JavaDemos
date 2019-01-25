package Test.test2019.Netty.NettyProduction;


import Test.test2019.Netty.MarshallingCodeFactory;
import Test.test2019.Netty.MessageInfo;
import common.Action;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NettyClientProduction {
    Bootstrap bootstrap;
    Channel channel;
    String host = "localhost";
    int port = 8080;

    public NettyClientProduction(String host, Integer port) {
        this.host = host;
        this.port = port;
        try {
            init();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public NettyClientProduction() {
        try {
            init();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    void init() throws Exception {
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
                ch.pipeline().addLast(new ClientBusinessHandler(NettyClientProduction.this));
            }
        });

    }

    public void connect(Action action) {

        if (channel != null && channel.isActive()) {
            return;
        }
        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port);
            //实现监听通道连接的方法
            channelFuture.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture channelFuture) {
                    if (channelFuture.isSuccess()) {
                        channel = channelFuture.channel();
                        System.out.println("连接成功");
                        if (action != null) {
                            action.callBack();
                        }
                    } else {
                        System.out.println("每隔2s重连....");
                        channelFuture.channel().eventLoop().schedule(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                connect(action);
                            }
                        }, 2, TimeUnit.SECONDS);
                    }


                }


            });

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void sendData(MessageInfo messageInfo) {
        channel.writeAndFlush(messageInfo);
    }

}
