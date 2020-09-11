package Test.opensource.Netty.NettySample;

import Test.opensource.Netty.MessageInfo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.SocketAddress;

public class NettySampleServerHandler extends SimpleChannelInboundHandler<Object> {

    //建立连接
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        System.out.println("Server: client "+socketAddress.toString()+ " connected.");
        super.channelActive(ctx);
    }

    //断开连接
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        System.out.println("Server: client "+socketAddress.toString()+ " disconnected.");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // discard
        System.out.println(String.format("Server received from %s , msg:%s", ctx.channel().remoteAddress(), msg));

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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
