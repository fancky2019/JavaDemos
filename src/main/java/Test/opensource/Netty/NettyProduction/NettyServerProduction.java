package Test.opensource.Netty.NettyProduction;

import Test.opensource.Netty.MarshallingCodeFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;

import java.io.File;
import java.util.concurrent.TimeUnit;

//https://github.com/netty/netty/blob/4.1/example/src/main/java/io/netty/example/echo/EchoServer.java
public class NettyServerProduction {
    private int port;

    public void test() {
        this.port = 23450;
        try {
            //打开Window的命令行，输入telnet命令:telnet localhost 8080,如果能够正确连接就代表成功
            run();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }

    }

    //    static final boolean SSL = System.getProperty("ssl") != null;
    public void run() throws Exception {

        //SSL
        boolean SSL = true;
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            /*
            SelfSignedCertificate 内部生成证书文件和私钥文件
            C:\Users\86185\AppData\Local\Temp\keyutil_localhost_4249992926290491158.crt
             C:\Users\86185\AppData\Local\Temp\keyutil_localhost_2467559130859262100.key
             */
            SelfSignedCertificate ssc = new SelfSignedCertificate();

            //X509Certificate 一个证书文件、一个私钥文件

            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();

//              sslCtx = SslContextBuilder.forServer(GeneratorCertKey.certificate,GeneratorCertKey.privateKey).build();

            //本地文件配置
//            File certificate = new File("C:\\Users\\86185\\AppData\\Local\\Temp\\keyutil_localhost_7006298436648660336.crt");
//            File privateKey = new File("C:\\Users\\86185\\AppData\\Local\\Temp\\keyutil_localhost_6063666083091152427.key");
//            sslCtx = SslContextBuilder.forServer(certificate, privateKey).build();

        } else {
            sslCtx = null;
        }


        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                           //SSL
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc()));
                            }




//                            //必须指定解码器，不然收不到信息
                            ch.pipeline().addLast(new IdleStateHandler(2, 2, 6, TimeUnit.SECONDS));
//                           Marshalling 优化jdk序列化，内部进行粘包处理。不用再设置frameDecoder、frameEncoder
                            //其他编码解码器参见 NettySampleClient NettySampleServer 类设置的frameDecoder和frameEncoder进行粘包处理
                            ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder());
                            ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingEncoder());

//                            ch.pipeline().addLast("decoder", new StringDecoder());
//                            ch.pipeline().addLast("encoder", new StringEncoder());

                            ch.pipeline().addLast(new ServerBusinessHandler());
                        }
                    });
                    /*
                    ChannelOption.SO_BACKLOG对应的是tcp/ip协议listen函数中的backlog参数，
                    函数listen(int socketfd,int backlog)用来初始化服务端可连接队列，服务端处理客户端连接请求是顺序处理的，
                    所以同一时间只能处理一个客户端连接，多个客户端来的时候，
                    服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
                     */
//                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
//                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

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
