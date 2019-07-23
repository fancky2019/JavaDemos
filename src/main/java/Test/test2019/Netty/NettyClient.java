package Test.test2019.Netty;

import common.CallBackRunnable;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    public void test() {
        try {
            run();
        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }

    }

    void run() throws Exception {
        String host = "localhost";
        int port = 8080;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {


                    ch.pipeline().addLast(new IdleStateHandler(2, 2, 2, TimeUnit.SECONDS));

//                    // 字符串解码和编码
//                    // encoder 编码器， decoder 解码器
//                    ch.pipeline().addLast("decoder",new StringDecoder());
//                    ch.pipeline().addLast("encoder",new StringEncoder());
                    ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder());
                    ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingEncoder());
                    ch.pipeline().addLast(new ClientHandler());
                }
            });

            // Start the client.
            ChannelFuture channelFuture = b.connect(host, port).sync(); // (5)
            Channel channel = channelFuture.channel();


            String line = "sendMessage";
            for (Integer i = 1; i <= 20; i++) {
                MessageInfo msg = new MessageInfo();
                msg.setMessageType(MessageType.HeartBeat);
                msg.setBody(line + i.toString());
                channel.writeAndFlush(msg);


                // channel.writeAndFlush(line+i.toString());
                Thread.sleep(500);
            }
            // Wait until the connection is closed.
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}

//class TimeClientHandler extends SimpleChannelInboundHandler<String> {
class ClientHandler extends ChannelInboundHandlerAdapter {

//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
//        System.out.println( msg);
//    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println(String.format("Client received from %s , msg:%s", channelHandlerContext.channel().remoteAddress(), msg));


    }

    /**
     * 通道激活发送心跳检测
     *
     * @param ctx
     * @throws Exception
     */
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new CallBackRunnable<>(p ->
        {
            ChannelHandlerContext ch = (ChannelHandlerContext) p;
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessageType(MessageType.HeartBeat);
            messageInfo.setBody("HeartBeat");
            ch.writeAndFlush(messageInfo);
            //   channelHandlerContext
        }, ctx), 0, 2, TimeUnit.SECONDS);
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();

    }

}
