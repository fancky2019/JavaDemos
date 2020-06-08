package Test.opensource.Netty.NettyProduction;

import Test.opensource.Netty.MarshallingCodeFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NetttyServerProduction {
    private int port;

    public void test() {
        this.port = 8080;
        try {
            //打开Window的命令行，输入telnet命令:telnet localhost 8080,如果能够正确连接就代表成功
            run();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }

    }


    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
//                            //必须指定解码器，不然收不到信息
                            ch.pipeline().addLast(new IdleStateHandler(2, 2, 6, TimeUnit.SECONDS));

                            ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder());
                            ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingEncoder());

//                            ch.pipeline().addLast("decoder", new StringDecoder());
//                            ch.pipeline().addLast("encoder", new StringEncoder());

                            ch.pipeline().addLast(new ServerBusinessHandler());

                        }
                    })
                    /*
                    ChannelOption.SO_BACKLOG对应的是tcp/ip协议listen函数中的backlog参数，
                    函数listen(int socketfd,int backlog)用来初始化服务端可连接队列，服务端处理客户端连接请求是顺序处理的，
                    所以同一时间只能处理一个客户端连接，多个客户端来的时候，
                    服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
                     */
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
