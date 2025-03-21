package Test.opensource.Netty;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import utility.CallBackRunnable;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
netty线程模型：reactor:Nio 异步事件驱动的线程模型。避免每个socket连接占用一个线程。Reactor 单线程模型-->多线程模型-->主从多线程模型

主从多线程模型：Reactor分成两部分，mainReactor负责监听server socket，accept新连接；并将建立的socket分派给subReactor。
                              subReactor负责多路分离已连接的socket，读写网络数据，对业务处理功能，其扔给worker线程池完成。通常，subReactor个数上可与CPU个数等同
 */
public class NettyClient {

    public void test() {
        try {
            run();
        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }

    }
    /*
    LengthFieldPrepender
    作用：将当前发送消息的二进制字节长度，添加到缓冲区头部；这样消息就有了固定长度，长度存储在缓冲头中
     */

    void run() throws Exception {
        String host = "localhost";
        int port = 9310;
        /*
        Selector作为多路复用器，EventLoop作为转发器，Pipeline作为事件处理器。
         */
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {

                    ChannelPipeline channelPipeline = ch.pipeline();
                    channelPipeline.addLast(new IdleStateHandler(2, 2, 2, TimeUnit.SECONDS));


//                    //利用Netty框架的解码器：防止TCP粘包。框架解码器并不能编解码发接收的消息，还需指定消息的编解码器。
//                    //解决TCP粘包等产生的半包问题。
//                    //消息体占4个字节：消息体长度
//                    channelPipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                    //LengthFieldPrepender只是将长度加入二进制头部，还需要指定编码器
//                    channelPipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
//                    // 字符串解码和编码
//                    // encoder 编码器， decoder 解码器
//                    ch.pipeline().addLast("decoder", new StringDecoder());
//                    ch.pipeline().addLast("encoder", new StringEncoder());

//                    channelPipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
//                    channelPipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));


//                    MarshallingEncoder 继承LengthFieldBasedFrameDecoder，内部解决粘包问题
                    channelPipeline.addLast(MarshallingCodeFactory.buildMarshallingDecoder());
                    channelPipeline.addLast(MarshallingCodeFactory.buildMarshallingEncoder());
                    channelPipeline.addLast(new ClientHandler());
                }
            });


            // Start the client.
            ChannelFuture channelFuture = b.connect(host, port).sync(); // (5)
            Channel channel = channelFuture.channel();


            String line = "sendMessage";
            for (Integer i = 1; i <= 20; i++) {
                MessageInfo msg = new MessageInfo();
//                msg.setMessageType(MessageType.HeartBeat);
                msg.setBody(line + i.toString());
                channel.writeAndFlush(msg);


//                channel.writeAndFlush(line + i.toString());
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
