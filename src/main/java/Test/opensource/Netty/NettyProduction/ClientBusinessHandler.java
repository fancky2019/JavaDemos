package Test.opensource.Netty.NettyProduction;

import Test.opensource.Netty.MessageInfo;
import Test.opensource.Netty.MessageType;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.SocketAddress;

public class ClientBusinessHandler extends ChannelInboundHandlerAdapter {

    NettyClientProduction nettyClientProduction;

    public ClientBusinessHandler(NettyClientProduction nettyClientProduction) {
        this.nettyClientProduction = nettyClientProduction;
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println(String.format("Client received from %s , msg:%s", channelHandlerContext.channel().remoteAddress(), msg));


    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    /**
     * 通道激活发送心跳检测
     *
     * @param ctx
     * @throws Exception
     */
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


//        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new CallBackRunnable<>(p ->
//        {
//            ChannelHandlerContext ch = (ChannelHandlerContext) p;
//            MessageInfo messageInfo = new MessageInfo();
//
//            messageInfo.setMessageType(MessageType.HeartBeat);
//            messageInfo.setBody("HeartBeat");
//            ch.writeAndFlush(messageInfo);
//            //   channelHandlerContext
//        }, ctx), 0, 2, TimeUnit.SECONDS);
        super.channelActive(ctx);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        nettyClientProduction.connect(null);
        super.channelInactive(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();

    }


    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    sentHeartBeat(ctx);
                    break;
                case WRITER_IDLE:
                    break;
                case ALL_IDLE:
                    Channel channel = ctx.channel();
                    SocketAddress socketAddress = ctx.channel().remoteAddress();
                    System.out.println("关闭连接");
                    channel.close();
                    nettyClientProduction.connect(null);
                    break;
            }


        }
        super.userEventTriggered(ctx, evt);
    }

    private void sentHeartBeat(ChannelHandlerContext channelHandlerContext) {
        MessageInfo messageInfo = new MessageInfo();

        messageInfo.setMessageType(MessageType.HeartBeat);
        messageInfo.setBody("HeartBeat");
        channelHandlerContext.writeAndFlush(messageInfo);
    }


}
