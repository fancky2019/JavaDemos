package Test.test2019.Netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

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
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {

                            ChannelPipeline channelPipeline = ch.pipeline();
//                            //必须指定解码器，不然收不到信息
//                            // 字符串解码和编码
                            //解决TCP粘包等产生的半包问题。
                            //消息体占4个字节：消息体长度
//                            channelPipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                            channelPipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
//                           //字符串解码和编码
//                         // encoder 编码器， decoder 解码器
//                            channelPipeline.addLast("decoder", new StringDecoder());
//                            channelPipeline.addLast("encoder", new StringEncoder());
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
