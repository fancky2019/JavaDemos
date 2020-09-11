package Test.opensource.Netty.NettySample;

import Test.opensource.Netty.MessageInfo;
import Test.opensource.Netty.MessageType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.SocketAddress;
import java.util.function.Consumer;

/*
SimpleChannelInboundHandler extends ChannelInboundHandlerAdapter
 */
public class NettySampleClientHandler extends SimpleChannelInboundHandler<Object> {

    long startTime = -1;


    public Consumer dicConnect;


    /**
     * 通道连接成功
     * 可设计发送心跳检测
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
        System.out.println("Client: channelActive - Connected to: " + ctx.channel().remoteAddress());
//        super.channelActive(ctx);
//        sendData(ctx);
    }

    /*
    连接通道断开
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        nettyClientProduction.connect(null);

        System.out.println("Client: Disconnected from: " + ctx.channel().remoteAddress());
//        super.channelInactive(ctx);
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

        super.channelUnregistered(ctx);
        dicConnect.accept(null);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println(String.format("Client received from %s , msg:%s", channelHandlerContext.channel().remoteAddress(), msg));

    }


    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (!(evt instanceof IdleStateEvent)) {
//            return;
//        }
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    //The connection was OK but there was no traffic for last period.
//
                    break;
                case WRITER_IDLE:
                    sentHeartBeat(ctx);
                    break;
                case ALL_IDLE:
                    Channel channel = ctx.channel();
                    SocketAddress socketAddress = ctx.channel().remoteAddress();
                    System.out.println("关闭连接");
                    //会触发未注册事件，在未注册内启用重连机制
                    channel.close();



                    //  nettyClientProduction.connect(null);

                    //     println("Sleeping for: " + UptimeClient.RECONNECT_DELAY + 's');

//                    ctx.channel().eventLoop().schedule(new Runnable() {
//                        @Override
//                        public void run() {
//                            println("Reconnecting to: " +  + ':' + UptimeClient.PORT);
//                            UptimeClient.connect();
//                        }
//                    }, 2, TimeUnit.SECONDS);

//                    dicConnect.accept(null);
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


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();

    }


    public void sendData(ChannelHandlerContext ctx) {

        try {
            if (!ctx.channel().isActive()) {
                return;
            }
            String line = "sendMessage";
            for (Integer i = 1; i <= 20; i++) {
                MessageInfo msg = new MessageInfo();
                msg.setMessageType(MessageType.HeartBeat);
                msg.setBody(line + i.toString());
                ctx.writeAndFlush(msg);


//                 channel.writeAndFlush(line+i.toString());
                Thread.sleep(500);
            }

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

}
