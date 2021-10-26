package Test.opensource.Netty.NettySample;


import Test.opensource.Netty.MarshallingCodeFactory;
import Test.opensource.Netty.MessageInfo;
import Test.opensource.Netty.NettyProduction.ServerBusinessHandler;
import Test.opensource.Netty.NettySample.codec.MessagePackDecoder;
import Test.opensource.Netty.NettySample.codec.MessagePackEncoder;
import Test.opensource.protobuf.model.PersonProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.jboss.marshalling.Marshalling;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/*
netty粘包处理：
固定长度的拆包器 FixedLengthFrameDecoder，每个应用层数据包的都拆分成都是固定长度的大小
行拆包器 LineBasedFrameDecoder，每个应用层数据包，都以换行符作为分隔符，进行分割拆分
分隔符拆包器 DelimiterBasedFrameDecoder，每个应用层数据包，都通过自定义的分隔符，进行分割拆分
基于数据包长度的拆包器 LengthFieldBasedFrameDecoder，将应用层数据包的长度，作为接收端应用层数据包的拆分依据。按照应用层数据包的大小，拆包。这个拆包器，有一个要求，就是应用层协议中包含数据包的长度
 */
public class NettySampleServer {
    public void test() {

        CompletableFuture.runAsync(() ->
        {
            runServer();
        });
    }

    ChannelFuture channelFuture;
    EventLoopGroup bossGroup;
    EventLoopGroup workerGroup;
    private static final int PORT = 8031;//Integer.parseInt(System.getProperty("port", "8080"));

    //    private static final NettySampleServerHandler handler = new NettySampleServerHandler();
    private void runServer() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))

                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
//                            //必须指定解码器，不然收不到信息
                            ch.pipeline().addLast(new IdleStateHandler(2, 2, 6, TimeUnit.SECONDS));


                            //框架解码器：防止TCP粘包。 FixedLengthFrameDecoder、LineBasedFrameDecoder、DelimiterBasedFrameDecoder和LengthFieldBasedFrameDecoder
                            ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                            //protobuf解码器：netty内部支持protobuf
//                            ch.pipeline().addLast("ProtobufDecoder", new ProtobufDecoder(PersonProto.Person.getDefaultInstance()));
//                            ch.pipeline().addLast("ProtobufEncoder", new ProtobufEncoder());


                            ch.pipeline().addLast("MessagePackDecoder", new MessagePackDecoder<>(MessageInfo.class));
                            ch.pipeline().addLast("MessagePackEncoder", new MessagePackEncoder());

//                            ch.pipeline().addLast("decoder", new StringDecoder());
//                            ch.pipeline().addLast("encoder", new StringEncoder());

//                             Marshalling 优化jdk序列化，内部进行粘包处理。不用再设置frameDecoder、frameEncoder
//                            其他编码解码器参要设置的frameDecoder和frameEncoder。进行粘包处理
//                            ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder());
//                            ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingEncoder());

                            ch.pipeline().addLast(new NettySampleServerHandler());

                        }
                    });
                    /*
                    ChannelOption.SO_BACKLOG对应的是tcp/ip协议listen函数中的backlog参数，
                    函数listen(int socketfd,int backlog)用来初始化服务端可连接队列，服务端处理客户端连接请求是顺序处理的，
                    所以同一时间只能处理一个客户端连接，多个客户端来的时候，
                    服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
                     ChannelOption.SO_BACKLOG：window 200,其他128
                     */
//                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
//                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            channelFuture = b.bind(PORT).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
//            f.channel().closeFuture().sync();//会一直阻塞直到对方断开网络。关闭应用 f.channel().close();
//            f.channel().close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {
//            workerGroup.shutdownGracefully();
//            bossGroup.shutdownGracefully();
        }
    }

    public void close() {
        channelFuture.channel().close();
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
