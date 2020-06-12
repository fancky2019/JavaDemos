package Test.opensource.Netty.NettySample.NettyUdp;

import Test.opensource.Netty.MarshallingCodeFactory;
import Test.opensource.Netty.NettySample.NettySampleClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;


import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class NettyUDPServer {
    public void test() {
        CompletableFuture.runAsync(() ->
        {
            try {
                Thread.sleep(1000);
//                runServer();
//                runBroadcastServer();
                runMulticastServer();
//                server();
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }

        });


    }

    //region 单播
    private void runServer() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // InetSocketAddress相当于c#IPEndPoint
            InetSocketAddress inetSocketAddress = new    InetSocketAddress("127.0.0.1", 6000);
            Bootstrap bootstrap = new Bootstrap(); // (1)
            bootstrap.group(workerGroup); // (2)
            bootstrap.channel(NioDatagramChannel.class);
            bootstrap.handler(new NettyUDPServerHandler());
            ChannelFuture channelFuture = bootstrap.bind(6001).sync();


            //向目标端口发送信息
            channelFuture.channel().writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("单播信息", CharsetUtil.UTF_8),
                    inetSocketAddress )).sync();

            //广播
//            channelFuture.channel().writeAndFlush(new DatagramPacket(
//                    Unpooled.copiedBuffer("UDP Broadcast Message", CharsetUtil.UTF_8),
//                    new InetSocketAddress("255.255.255.255", 6000))).sync();

        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
    //endregion

    //region 广播
    private void runBroadcastServer() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //广播地址
            // InetSocketAddress相当于c#IPEndPoint
            InetSocketAddress broadcastAddress = new    InetSocketAddress("192.168.1.255", 6000);
            Bootstrap bootstrap = new Bootstrap(); // (1)
            bootstrap.group(workerGroup); // (2)
            bootstrap.channel(NioDatagramChannel.class);
//            bootstrap.option(ChannelOption.SO_BROADCAST, true);//广播
            bootstrap.handler(new NettyUDPServerHandler());

            ChannelFuture channelFuture = bootstrap.bind(6001).sync();


            //广播
            channelFuture.channel().writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("UDP Broadcast Message", CharsetUtil.UTF_8),
                    broadcastAddress)).sync();

        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
    //endregion

    //region 多播
    private void runMulticastServer() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //多播地址
            // InetSocketAddress相当于c#IPEndPoint
            InetSocketAddress multicastAddress = new    InetSocketAddress("192.168.1.255", 6000);

            Bootstrap bootstrap = new Bootstrap(); // (1)
            bootstrap.group(workerGroup); // (2)
            bootstrap.channel(NioDatagramChannel.class);
            bootstrap.handler(new NettyUDPServerHandler());
            ChannelFuture channelFuture = bootstrap.bind(6001).sync();

            //多播
            channelFuture.channel().writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("Multicast Message", CharsetUtil.UTF_8),
                    multicastAddress)).sync();

        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
    //endregion

//单播
//    public void server() {
//
//        EventLoopGroup group = new NioEventLoopGroup();
//        try {
//            Bootstrap b = new Bootstrap();
//            b.group(group)
//                    .channel(NioDatagramChannel.class)
//                    .handler(new NettyUDPServerHandler());
//
//            Channel ch = b.bind(0).sync().channel();
//
//            ch.writeAndFlush(new DatagramPacket(
//                    Unpooled.copiedBuffer("单播信息", CharsetUtil.UTF_8),
//                    new InetSocketAddress("127.0.0.1", 6000))).sync();
//
//            ch.closeFuture().await();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            group.shutdownGracefully();
//        }
//    }


}
