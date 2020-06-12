package Test.opensource.Netty.NettySample.NettyUdp;

import Test.opensource.Netty.MarshallingCodeFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.NetUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.CompletableFuture;

public class NettyUDPClient {

    public void test() {
        CompletableFuture.runAsync(() ->
        {
            try {
                //报错：IPv6 socket cannot join IPv4 multicast group。加入下面设置
                System.setProperty("java.net.preferIPv4Stack", "true");


//                runClient();
//                client();
                runMulticastClient();
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }

        });


    }

    private void runClient() throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap(); // (1)
            bootstrap.group(workerGroup); // (2)
            bootstrap.channel(NioDatagramChannel.class); //
            bootstrap.handler(new NettyUDPClientHandler());
            ChannelFuture channelFuture = bootstrap.bind(6000).sync();

            channelFuture.channel().closeFuture().await();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    //region 单播,广播
    public void client() {
        try {
            Bootstrap b = new Bootstrap();
            EventLoopGroup group = new NioEventLoopGroup();
            b.group(group);
            b.channel(NioDatagramChannel.class);
            b.handler(new NettyUDPClientHandler());
            b.bind(6000).sync().channel().closeFuture().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

   //endregion

    //region 多播：可以接受单播、多播、广播信息
    private void runMulticastClient() throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            NetworkInterface ni = NetUtil.LOOPBACK_IF;
            // InetSocketAddress相当于c#IPEndPoint
            InetSocketAddress multicastAddress = new InetSocketAddress(InetAddress.getByName("225.0.0.1"), 6000);

            Bootstrap bootstrap = new Bootstrap(); // (1)
            bootstrap.group(workerGroup); // (2)
            bootstrap.channel(NioDatagramChannel.class);
            bootstrap.handler(new NettyUDPClientHandler());

           //加入多播组：和单播多播区别就是：加入了多播组。但是可以收到单播，多播的信息。
            NioDatagramChannel ch = (NioDatagramChannel) bootstrap.bind(multicastAddress.getPort()).sync().channel();
            ch.joinGroup(multicastAddress, ni).sync();

            ch.closeFuture().await();

        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
    //endregion
}
