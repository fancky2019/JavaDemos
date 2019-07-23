package Test.test2019.Netty.NettyProduction;

import Test.test2019.Netty.MessageInfo;
import Test.test2019.Netty.MessageType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.SocketAddress;

public class ServerBusinessHandler extends ChannelInboundHandlerAdapter {


    //建立连接
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        super.channelActive(ctx);
    }

    //断开连接
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        super.channelInactive(ctx);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {

                case READER_IDLE:
                    break;
                case WRITER_IDLE:
                    break;
                case ALL_IDLE:
                    //6s都未收到客户端的心跳就关闭
                    Channel channel = ctx.channel();
                    SocketAddress socketAddress = ctx.channel().remoteAddress();
                    System.out.println("关闭连接");
                    channel.close();

                    break;
            }


        }
        super.userEventTriggered(ctx, evt);
    }

    //读取信息
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
            } else {
                //doWork()
                //reply    channelHandlerContext.writeAndFlush(msg);
                MessageInfo heartBeatInfo = new MessageInfo();
                heartBeatInfo.setMessageType(MessageType.Data);
                heartBeatInfo.setBody("Data");
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