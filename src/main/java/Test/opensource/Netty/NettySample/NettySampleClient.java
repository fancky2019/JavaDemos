package Test.opensource.Netty.NettySample;

import Model.JacksonPojo;
import Test.opensource.Netty.MarshallingCodeFactory;
import Test.opensource.Netty.MessageInfo;
import Test.opensource.Netty.MessageType;
import Test.opensource.Netty.NettySample.codec.MessagePackDecoder;
import Test.opensource.Netty.NettySample.codec.MessagePackEncoder;
import Test.opensource.protobuf.model.PersonProto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Any;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import utility.Action;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
/*
netty粘包处理：
固定长度的拆包器 FixedLengthFrameDecoder，每个应用层数据包的都拆分成都是固定长度的大小
行拆包器 LineBasedFrameDecoder，每个应用层数据包，都以换行符作为分隔符，进行分割拆分
分隔符拆包器 DelimiterBasedFrameDecoder，每个应用层数据包，都通过自定义的分隔符，进行分割拆分
基于数据包长度的拆包器 LengthFieldBasedFrameDecoder，将应用层数据包的长度，作为接收端应用层数据包的拆分依据。按照应用层数据包的大小，拆包。这个拆包器，有一个要求，就是应用层协议中包含数据包的长度
 */
public class NettySampleClient {
    Bootstrap bootstrap;
    Channel channel;
    EventLoopGroup workerGroup;
    String host = "127.0.0.1";
    int port = 8031;
    boolean closed;

    public void test() {
        CompletableFuture.runAsync(() ->
        {
            try {
                Thread.sleep(100);
                runClient();
                connect();
                sendData(null);
//                sendProtobufData();
                Thread.sleep(2000);
                //延迟2秒等待连接成功

                //在连接成功在发送数据，netty是异步的， connect(null);调用完不一定连接成功
//                sendData(null);
//                this.close();
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }

        });


    }

    private void runClient() {
        workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap(); // (1)
        bootstrap.group(workerGroup); // (2)
        bootstrap.channel(NioSocketChannel.class); // (3)
//        bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                //设置allIdleTime=readerIdleTime*3,如果三次都没收到心跳信息就认为断线了
                ch.pipeline().addLast(new IdleStateHandler(2, 2, 6, TimeUnit.SECONDS));


                //框架解码器：防止TCP粘包。 FixedLengthFrameDecoder、LineBasedFrameDecoder、DelimiterBasedFrameDecoder和LengthFieldBasedFrameDecoder
                // 4个字节消息长度
                //LengthFieldBasedFrameDecoder与LengthFieldPrepender一起配置
                ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
               //指定4个字节消息长度
                ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                //protobuf解码器：netty内部支持protobuf
//                ch.pipeline().addLast("ProtobufDecoder", new ProtobufDecoder(PersonProto.Person.getDefaultInstance()));
//                ch.pipeline().addLast("ProtobufEncoder", new ProtobufEncoder());


                ch.pipeline().addLast("MessagePackDecoder", new MessagePackDecoder<>(MessageInfo.class));
                ch.pipeline().addLast("MessagePackEncoder", new MessagePackEncoder());

//                            ch.pipeline().addLast("decoder", new StringDecoder());
//                            ch.pipeline().addLast("encoder", new StringEncoder());

//                 Marshalling 优化jdk序列化，内部进行粘包处理。不用再设置frameDecoder、frameEncoder
//                其他编码解码器参要设置的frameDecoder和frameEncoder。进行粘包处理
//                ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder());
//                ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingEncoder());
                NettySampleClientHandler nettySampleClientHandler = new NettySampleClientHandler();
                nettySampleClientHandler.dicConnect = new Consumer() {
                    @Override
                    public void accept(Object o) {
                        connect();
                    }
                };
                ch.pipeline().addLast(nettySampleClientHandler);
            }
        });
    }

    private void connect() {

        if (channel != null && channel.isActive()) {
            return;
        }
        try {

            //同步阻塞连接，直到连接成功或拒绝
//            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channel = channelFuture.channel();
            /*
            Netty 操作都是异步的。
            connect 方法返回ChannelFuture，当连接完成，执行ChannelFutureListener的匿名内部类的operationComplete
           ChannelFutureListener 接口继承GenericFutureListener接口
            少用lambda表达式，不然看不到起实现原理
             */
//            channelFuture.addListener(new GenericFutureListener<ChannelFuture>() {
//                @Override
//                public void operationComplete(ChannelFuture channelFuture) throws Exception {
//
//                }
//            });
//            channelFuture.addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture channelFuture) throws Exception {
//
//                }
//            });
            //Channel连接成功，执行Listener方法。


        } catch (Exception ex) {
            System.out.println(ex.toString());

            if (channel != null) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                connect();
            }
        }
    }

    public void sendData(MessageInfo messageInfo) {

        try {

            if (closed || (channel == null && !channel.isActive())) {
                return;
            }
            String line = "sendMessage";
            for (Integer i = 1; i <= 20; i++) {
                MessageInfo msg = new MessageInfo();
                msg.setMessageType(MessageType.Data);
                msg.setBody(line + i.toString());


//                ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
//                byte[] bytes = objectMapper.writeValueAsBytes(msg);
//                MessageInfo deserialized = objectMapper.readValue(bytes, MessageInfo.class);
//

                channel.writeAndFlush(msg);

                System.out.println(msg.toString());
//                 channel.writeAndFlush(line+i.toString());
                Thread.sleep(500);
            }

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }


    public void sendProtobufData() {
        PersonProto.Person.Builder builder = PersonProto.Person.newBuilder();
        builder.setId(1)
                .setAge(27)
                .setName("fancky")
                .addJobs(PersonProto.Job.newBuilder().setName("程序员").setSalary(777).build())//List<Job>
                .setGender(PersonProto.Gender.MAN)//枚举
                .addSons("li")//List<String>
                .addSons("zi")//List<String>
                .addAny(Any.pack(PersonProto.Job.newBuilder().setName("程序员any").setSalary(777).build())) //Any 必须是proto 文件里定义的message  类型
                .putSonJobs("li", PersonProto.Job.newBuilder().setName("程序员").setSalary(777).build())//Map<String,Job>
        ;
        PersonProto.Person person = builder.build();
        channel.writeAndFlush(person);
    }

    public void close() {
        closed = true;
        if (channel == null || !channel.isActive()) {
            return;
        }
        channel.close();
        workerGroup.shutdownGracefully();
    }


}
