package Test.opensource.Netty.mqtt.client;

import Test.test2021.designpattern.SingletonPattern;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MqttClient {
    private String hostServer="127.0.0.1";
    private int portServer=7100;
    private NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private Bootstrap bootstrap;

    private MqttClient()
    {

    }
    private static class StaticInnerClass{
        private static final MqttClient instance=new MqttClient();
    }

    public static MqttClient getInstance() {
        return MqttClient.StaticInnerClass.instance;
    }

    public void run() {
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        //客户端初始化
                        socketChannel.pipeline().addLast("decoder", new MqttDecoder(1024 * 8));
                        socketChannel.pipeline().addLast("encoder", MqttEncoder.INSTANCE);
                        socketChannel.pipeline().addLast(new ClientMqttHandler());
                    }
                });
        //连接netty服务器
        reconnect(hostServer, portServer);
    }

    /**
     * 功能描述: 断线重连，客户端有断线重连机制，就更不能使用异步阻塞了
     *
     * @param
     * @return void
     * @author zhouwenjie
     * @date 2021/3/19 14:53
     */
    public void reconnect(String host, Integer port) {
        bootstrap.remoteAddress(host, port);
        ChannelFuture channelFuture = bootstrap.connect();
        //使用最新的ChannelFuture -> 开启最新的监听器
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (future.cause() != null) {
                log.error("MQTT驱动服务端" + host + ":" + port + "连接失败。。。");
                future.channel().eventLoop().schedule(() -> reconnect(host, port), 3, TimeUnit.SECONDS);
            } else {

                log.info("MQTT驱动服务端" + host + ":" + port + "连接成功");
            }
        });
    }

    /**
     * 关闭 client
     */
    @PreDestroy
    public void shutdown() {
        // 优雅关闭 EventLoopGroup 对象
        eventLoopGroup.shutdownGracefully();
        log.info("[*MQTT客户端关闭]");
    }

}
