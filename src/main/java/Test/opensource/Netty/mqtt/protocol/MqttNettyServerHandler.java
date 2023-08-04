package Test.opensource.Netty.mqtt.protocol;

import Test.opensource.Netty.mqtt.server.MqttNettyMsgBacServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
//Object 不指定接收消息类型 ChannelInboundHandlerAdapter
///指定接收消息的类型 NettySampleServerHandler extends SimpleChannelInboundHandler<Object>
public class MqttNettyServerHandler extends ChannelInboundHandlerAdapter {

    public static final ConcurrentHashMap<String, ChannelHandlerContext> clientMap = new ConcurrentHashMap<String, ChannelHandlerContext>();


    private MqttNettyMsgBacServer mqttNettyMsgBacServer = MqttNettyMsgBacServer.getInstance();

    //    SimpleChannelInboundHandler
    //建立连接
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        Channel channel = ctx.channel();
//        SocketAddress socketAddress = channel.remoteAddress();
//        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
//
        //判断连接是否合法
        if (!clientMap.containsKey(ctx.channel().id().toString())) {
            clientMap.put(ctx.channel().id().toString(), ctx);
            // super.handlerAdded(ctx);
        }
//        else {
//            ctx.close();
//        }


        super.channelActive(ctx);
    }

    //断开连接
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        Channel channel = ctx.channel();
//        SocketAddress socketAddress = channel.remoteAddress();


        clientMap.remove(ctx.channel().id().toString());
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
//                    System.out.println("关闭连接");
                    //  channel.close();

                    break;
            }


        }
        super.userEventTriggered(ctx, evt);
    }

    //读取信息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MqttMessage mqttMessage = (MqttMessage) msg;
        if (null != mqttMessage) {
            log.info("接收mqtt消息：" + mqttMessage);
            MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();

            switch (mqttFixedHeader.messageType()) {
                // ----------------------接收消息端（服务端）可能会触发的事件----------------------------------------------------------------
                case CONNECT:
                    //	在一个网络连接上，客户端只能发送一次CONNECT报文。服务端必须将客户端发送的第二个CONNECT报文当作协议违规处理并断开客户端的连接
                    //	建议connect消息单独处理，用来对客户端进行认证管理等 这里直接返回一个CONNACK消息
                    mqttNettyMsgBacServer.connectionAck(ctx, mqttMessage);
                    break;
                case PUBLISH:
                    ByteBuf byteBuf = (ByteBuf) mqttMessage.payload();
                    byte[] tmp = new byte[byteBuf.readableBytes()];
                    byteBuf.readBytes(tmp);
                    String content= new String(tmp);
                    log.info("收到订阅消息:"+content);
                    //	收到消息，返回确认，PUBACK报文是对QoS 1等级的PUBLISH报文的响应,PUBREC报文是对PUBLISH报文的响应
                    mqttNettyMsgBacServer.publishAck(ctx, mqttMessage);
                    break;
                case PUBREL:
                    //	释放消息，PUBREL报文是对QoS 2等级的PUBREC报文的响应,此时我们应该回应一个PUBCOMP报文
                    mqttNettyMsgBacServer.publishComp(ctx, mqttMessage);
                    break;
                case SUBSCRIBE:
                    //	客户端订阅主题
                    //	客户端向服务端发送SUBSCRIBE报文用于创建一个或多个订阅，每个订阅注册客户端关心的一个或多个主题。
                    //	为了将应用消息转发给与那些订阅匹配的主题，服务端发送PUBLISH报文给客户端。
                    //	SUBSCRIBE报文也（为每个订阅）指定了最大的QoS等级，服务端根据这个发送应用消息给客户端
                    mqttNettyMsgBacServer.subscribeAck(ctx, mqttMessage);
                    break;
                case UNSUBSCRIBE:
                    //	客户端取消订阅
                    //	客户端发送UNSUBSCRIBE报文给服务端，用于取消订阅主题
                    mqttNettyMsgBacServer.unsubscribeAck(ctx, mqttMessage);
                    break;
                case PINGREQ:
                    //	客户端发起心跳
                    mqttNettyMsgBacServer.pingResp(ctx, mqttMessage);
                    break;
                case DISCONNECT:
                    //	客户端主动断开连接
                    //	DISCONNECT报文是客户端发给服务端的最后一个控制报文， 服务端必须验证所有的保留位都被设置为0
                    break;
                // ----------------------服务端作为发送消息端可能会接收的事件----------------------------------------------------------------
                case PUBACK:
                case PUBREC:
                    //QoS 2级别,响应一个PUBREL报文消息，PUBACK、PUBREC这俩都是ack消息
                    //PUBACK报文是对QoS 1等级的PUBLISH报文的响应，如果一段时间没有收到客户端ack，服务端会重新发送消息
                    mqttNettyMsgBacServer.receivePubAck(ctx, mqttMessage);
                    break;
                case PUBCOMP:
                    //收到qos2级别接收端最后一次发送过来的确认消息
                    mqttNettyMsgBacServer.receivePubcomp(ctx, mqttMessage);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}