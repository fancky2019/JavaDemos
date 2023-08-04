package Test.opensource.Netty.NettyProduction;

import Test.opensource.Netty.MessageInfo;
import Test.opensource.Netty.MessageType;
import Test.opensource.Netty.mqtt.client.MqttMsgBack;
import Test.opensource.Netty.mqtt.protocol.MqttNettyClient;
import io.netty.channel.*;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

@Slf4j
public class MqttNettyClientHandler extends ChannelInboundHandlerAdapter {

    MqttNettyClient nettyClientProduction;
    MqttMsgBack mqttMsgBack = MqttMsgBack.getInstance();
    private static final String PROTOCOL_NAME_MQTT_3_1_1 = "MQTT";
    private static final int PROTOCOL_VERSION_MQTT_3_1_1 = 4;
    private String defaultTopicName = "topicName";
    private String userName = "test";


    private String password = "test";

    public MqttNettyClientHandler(MqttNettyClient nettyClientProduction) {
        this.nettyClientProduction = nettyClientProduction;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println(String.format("Client received from %s , msg:%s", channelHandlerContext.channel().remoteAddress(), msg));
        MqttMessage mqttMessage = (MqttMessage) msg;
        if (null != mqttMessage) {
            log.info("接收mqtt消息：" + mqttMessage);
            MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
            switch (mqttFixedHeader.messageType()) {
                // ----------------------发送消息端（客户端）可能会触发的事件----------------------------------------------------------------
                case CONNACK:
                    mqttMsgBack.receiveConnectionAck(ctx, mqttMessage);
                    // 订阅主题
                    mqttMsgBack.subscribe(ctx,defaultTopicName, MqttQoS.AT_MOST_ONCE);


                    break;
                case PUBREC:
                case PUBACK:
                    //接收服务端的ack消息
                    mqttMsgBack.receivePubAck(ctx, mqttMessage);
                    break;
                case PUBCOMP:
                    mqttMsgBack.receivePubcomp(ctx, mqttMessage);
                    break;
                case SUBACK:
                    mqttMsgBack.receiveSubAck(ctx, mqttMessage);
                    mqttMsgBack.publish(ctx,defaultTopicName,"MQTT",MqttQoS.AT_MOST_ONCE,false);

                    break;
                case UNSUBACK:
                    mqttMsgBack.receiveUnSubAck(ctx, mqttMessage);
                    break;
                case PINGRESP:
                    //客户端发起心跳
                    mqttMsgBack.pingReq(ctx, mqttMessage);
                    break;
                // ----------------------接收消息端（客户端）可能会触发的事件----------------------------------------------------------------
                case PUBLISH:
                    //	收到消息，返回确认，PUBACK报文是对QoS 1等级的PUBLISH报文的响应,PUBREC报文是对PUBLISH报文的响应
                    mqttMsgBack.publishAck(ctx, mqttMessage);
                    break;
                case PUBREL:
                    //	释放消息，PUBREL报文是对QoS 2等级的PUBREC报文的响应,此时我们应该回应一个PUBCOMP报文
                    mqttMsgBack.publishComp(ctx, mqttMessage);
                    break;
                default:
                    break;
            }
        }
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


//        MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader("MQTT", 4, true, true, false, 0, false, true, 60);
//        String uuid = UUID.randomUUID().toString().replace("-", "");
//        MqttConnectPayload connectPayload = new MqttConnectPayload(uuid, null, null, userName, password.getBytes(CharsetUtil.UTF_8));
//        MqttFixedHeader mqttFixedHeaderInfo = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_LEAST_ONCE, false, 0);
//        MqttConnectMessage connectMessage = new MqttConnectMessage(mqttFixedHeaderInfo, mqttConnectVariableHeader, connectPayload);
////        ctx.writeAndFlush(connectMessage);
//        ctx.writeAndFlush(connectMessage);

//        DUP是重发标志，0 否，1是
        MqttFixedHeader connectFixedHeader =
                new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttConnectVariableHeader connectVariableHeader =
                new MqttConnectVariableHeader(PROTOCOL_NAME_MQTT_3_1_1, PROTOCOL_VERSION_MQTT_3_1_1, true, true, false,
                        0, false, false, 20, MqttProperties.NO_PROPERTIES);
        MqttConnectPayload connectPayload = new MqttConnectPayload("clientId",
                MqttProperties.NO_PROPERTIES,
                null,
                null,
                userName,
                password.getBytes(CharsetUtil.UTF_8));
        MqttConnectMessage connectMessage =
                new MqttConnectMessage(connectFixedHeader, connectVariableHeader, connectPayload);
        ctx.writeAndFlush(connectMessage);

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
                  //  sentHeartBeat(ctx);
                   // mqttMsgBack.pingReq();
                    break;
                case WRITER_IDLE:
                    break;
                case ALL_IDLE:
                    Channel channel = ctx.channel();
                    SocketAddress socketAddress = ctx.channel().remoteAddress();
//                    System.out.println("关闭连接");
//                    channel.close();
//                    nettyClientProduction.connect(null);
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
