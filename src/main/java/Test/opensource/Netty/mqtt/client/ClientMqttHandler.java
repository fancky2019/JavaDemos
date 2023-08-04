package Test.opensource.Netty.mqtt.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
//@ChannelHandler.Sharable
public class ClientMqttHandler extends SimpleChannelInboundHandler<MqttMessage> {
    private String hostServer="127.0.0.1";


    private int portServer=7100;

    public static ChannelHandlerContext context;


    private MqttMsgBack mqttMsgBack=MqttMsgBack.getInstance();

    /**
     * 连接成功
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        int port = ipSocket.getPort();
        String host = ipSocket.getHostString();
        if (hostServer.equals(host) && port == portServer) {
            context = ctx;
            mqttMsgBack.connect(ctx);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        int port = ipSocket.getPort();
        String host = ipSocket.getHostString();
        log.error("与设备" + host + ":" + port + "连接断开!" + "断开定时发送");
        ctx.close();
        ctx.deregister();
        ctx.pipeline().remove(this);
        super.channelInactive(ctx);

        MqttClient.getInstance().reconnect(ipSocket.getHostString(), ipSocket.getPort());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("[* Netty connection exception]:{}", cause.toString());
        cause.printStackTrace();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, MqttMessage mqttMessage) {
        if (null != mqttMessage) {
            log.info("接收mqtt消息：" + mqttMessage);
            MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
            switch (mqttFixedHeader.messageType()) {
                // ----------------------发送消息端（客户端）可能会触发的事件----------------------------------------------------------------
                case CONNACK:
                    mqttMsgBack.receiveConnectionAck(ctx, mqttMessage);

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
}
