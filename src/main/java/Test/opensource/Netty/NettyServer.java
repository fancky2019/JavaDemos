package Test.opensource.Netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


/**
netty线程模型：reactor:Nio 异步事件驱动的线程模型。避免每个socket连接占用一个线程。Reactor 单线程模型-->多线程模型-->主从多线程模型

主从多线程模型：Reactor分成两部分，mainReactor负责监听server socket，accept新连接；并将建立的socket分派给subReactor。
                              subReactor负责多路分离已连接的socket，读写网络数据，对业务处理功能，其扔给worker线程池完成。通常，subReactor个数上可与CPU个数等同


（一）、ByteBuf读写指针。直接内存
 */
public class NettyServer {
    private int port;

    public void test() {
        this.port = 9310;
        try {
            //打开Window的命令行，输入telnet命令:telnet localhost 8080,如果能够正确连接就代表成功
            run();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }

    }


    public void run() throws Exception {
          /*
            netty组件：
            Selector作为多路复用器
            EventLoop作为事件转发器
            Pipeline作为事件处理器。
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {

                            //事情处理器
                            ChannelPipeline channelPipeline = ch.pipeline();


//                    //利用Netty框架的解码器：防止TCP粘包。框架解码器并不能编解码发接收的消息，还需指定消息的编解码器。
//                            //必须指定解码器，不然收不到信息
//                            // 字符串解码和编码
                            //解决TCP粘包等产生的半包问题。
                            //消息体占4个字节：消息体长度
//                            channelPipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                            //LengthFieldPrepender只是将长度加入二进制头部，还需要指定编码器
//                            channelPipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
//
////                           //字符串解码和编码
////                         // encoder 编码器， decoder 解码器
//                            ch.pipeline().addLast("decoder",new StringDecoder());
//                            ch.pipeline().addLast("encoder",new StringEncoder());

//                            channelPipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
//                            channelPipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));

                            //MarshallingEncoder 继承LengthFieldBasedFrameDecoder，内部解决粘包问题
                            channelPipeline.addLast(MarshallingCodeFactory.buildMarshallingDecoder());
                            channelPipeline.addLast(MarshallingCodeFactory.buildMarshallingEncoder());


                            channelPipeline.addLast(new ServerHandler());

                        }
                    })

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

//class DiscardServerHandler  extends SimpleChannelInboundHandler<String>  {
class ServerHandler extends ChannelInboundHandlerAdapter {

    //    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//        // 收到消息直接打印输出
//        System.out.println(ctx.channel().remoteAddress() + " Say : " + msg);
//
//        // 返回客户端消息 - 我已经接收到了你的消息
//        ctx.writeAndFlush("Received your message !\n");
//    }
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println(String.format("Server received from %s , msg:%s", channelHandlerContext.channel().remoteAddress(), msg));
        //应答心跳
        if (msg instanceof MessageInfo) {
            MessageInfo messageInfo = (MessageInfo) msg;
            if (messageInfo.getMessageType() == MessageType.HeartBeat) {
                MessageInfo heartBeatInfo = new MessageInfo();
                heartBeatInfo.setMessageType(MessageType.HeartBeat);
                heartBeatInfo.setBody("HeartBeat");
                channelHandlerContext.writeAndFlush(heartBeatInfo);
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}
