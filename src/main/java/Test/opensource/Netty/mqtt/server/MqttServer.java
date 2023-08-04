package Test.opensource.Netty.mqtt.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;

@Slf4j
public class MqttServer {

//    private MqttServerChannelInitializer mqttServerChannelInitializer;

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workerGroup;

    private ChannelFuture future;

    private int socketPort=7100;

    public void start() {
        //创建接收请求和处理请求的实例（默认线程数为 CPU 核心数乘以2也可自定义）
        bossGroup = new NioEventLoopGroup(3);
        workerGroup = new NioEventLoopGroup(6);
        try {
            //创建服务端启动辅助类(boostrap 用来为 Netty 程序的启动组装配置一些必须要组件，例如上面的创建的两个线程组)
            ServerBootstrap socketBs = new ServerBootstrap();
            //channel 方法用于指定服务器端监听套接字通道
            //socket配置
            socketBs.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //ChannelOption.SO_BACKLOG对应的是tcp/ip协议listen函数中的backlog参数，
                    // 函数listen(int socketfd,int backlog)用来初始化服务端可连接队列，
                    // 服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接，
                    // 多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //快速复用,防止服务端重启端口被占用的情况发生
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder", new MqttDecoder(1024 * 8));
                            pipeline.addLast("encoder", MqttEncoder.INSTANCE);
                            pipeline.addLast(new ServerMqttHandler());
                        }
                    })

                    //如果TCP_NODELAY没有设置为true,那么底层的TCP为了能减少交互次数,会将网络数据积累到一定的数量后,
                    // 服务器端才发送出去,会造成一定的延迟。在互联网应用中,通常希望服务是低延迟的,建议将TCP_NODELAY设置为true
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //默认的心跳间隔是7200s即2小时。Netty默认关闭该功能。
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            future = socketBs.bind(socketPort).sync();
            if (future.isSuccess()) {
                log.info("[*MQTT驱动服务端启动成功]");
                future.channel().closeFuture().sync();
            } else {
                log.info("[~~~MQTT驱动服务端启动失败~~~]");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 这里可以手动关闭，同时在整个springboot应用停止的时候，这里也得以调用【@PreDestroy】
     * 参考 https://www.cnblogs.com/CreatorKou/p/11606870.html
     */
    @PreDestroy
    public void shutdown() {
        // 优雅关闭两个 EventLoopGroup 对象
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.info("[*MQTT服务端关闭成功]");
    }
}
