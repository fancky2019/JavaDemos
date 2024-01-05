package Test.opensource.Netty;

import Test.opensource.Netty.NettyProduction.GeneratorCertKey;
import Test.opensource.Netty.NettyProduction.NettyServerProduction;
import Test.opensource.Netty.NettyProduction.NettyClientProduction;
import Test.opensource.Netty.NettySample.NettySampleClient;
import Test.opensource.Netty.NettySample.NettyUdp.NettyUDPClient;
import Test.opensource.Netty.NettySample.NettyUdp.NettyUDPServer;
import Test.opensource.Netty.WebSocketDemo.WebSocketClient;
import Test.opensource.Netty.WebSocketDemo.WebSocketsServer;
import io.netty.buffer.*;
import org.apache.http.nio.util.DirectByteBufferAllocator;

import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class NettyTest {

    /*
     NIO  同步非阻塞

     */
    /*
     1、基于epoll LT （Level trigger）的reactor线程模型
     2、0拷贝，直接内存（堆外内存），网络发送需要拷贝到网卡发送。
     3、堆栈内存，剩下的就都是堆外内存了，DirectByteBuffer。堆内内存需用用户态内核之间拷贝，
        直接内存不需要
     4、池化思想，其他框架都有
     5、protobuf，socket也可以使用
     6、面向buffer,传统基于stream
     */
    /*
    --add-opens java.base/jdk.internal.misc=ALL-UNNAMED
--add-opens java.base/sun.security.x509=ALL-UNNAMED
--add-opens java.base/java.nio=ALL-UNNAMED
-Dio.netty.tryReflectionSetAccessible=true


     <dependency>
            <groupId>org.bouncycastle</groupId>
            <artifactId>bcpkix-jdk15on</artifactId>
            <version>1.70</version>
        </dependency>
     */
    public void test() {
//        ByteBuf可以分为两类 DirectByteBuf 和HeapByteBuf
        ByteBuf byteBuf = Unpooled.buffer();//heapBuffer
        //PooledUnsafeDirectByteBuf.安卓平台unpooled，其他pooled
        byteBuf = ByteBufAllocator.DEFAULT.buffer();//PooledByteBufAllocator 或者UnpooledByteBufAllocator
        //PooledUnsafeDirectByteBuf(ridx: 0, widx: 0, cap: 256)
        byteBuf =   ByteBufAllocator.DEFAULT.directBuffer();
        //PooledUnsafeDirectByteBuf(ridx: 0, widx: 0, cap: 256)
        byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
        byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();
        //建议使用ByteBuf
        ByteBuffer byteBuffer =   DirectByteBufferAllocator.INSTANCE.allocate(100);
//        nettyTest();
//        nettyWebSocket();
//        nettySampleTest();
//        nettyUDPTest() ;
        nettyProductionTest();
    }

    private void nettyProductionTest() {

//        GeneratorCertKey.Generator();

        CompletableFuture.runAsync(() ->
        {
            new NettyServerProduction().test();

        });
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {

        }
        CompletableFuture.runAsync(() ->
        {

            NettyClientProduction nettyClientProduction = new NettyClientProduction();
            nettyClientProduction.connect(() ->
            {
                for (int i = 1; i <= 2; i++) {
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setMessageType(MessageType.Data);
                    messageInfo.setBody("测试消息体");
                    nettyClientProduction.sendData(messageInfo);
                }
            });
        });
    }

    private void nettyTest() {

        CompletableFuture.runAsync(() ->
        {
            new NettyServer().test();
//            new NetttyServerProduction().test();

        });
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {

        }
        CompletableFuture.runAsync(() ->
        {
            new NettyClient().test();
//            NettyClientProduction nettyClientProduction = new NettyClientProduction();
//            nettyClientProduction.connect(()->
//            {
//                for (int i = 1; i <= 2; i++) {
//                    MessageInfo messageInfo = new MessageInfo();
//
//                    messageInfo.setMessageType(MessageType.HeartBeat);
//                    messageInfo.setBody("HeartBeat");
//                    nettyClientProduction.sendData(messageInfo);
//                }
//            });
        });

    }

    public void nettyWebSocket() {
        try {
            /*
             * 客户端测试网页：在DW项目的websocketdemo.html。
             * 启动WebSocket服务程序：监听8031端口
             */

            //region 客户端测试相关代码
            /*
             * 浏览器控制台测试连接
             * 在浏览器控制台执行：
             * var ws = new WebSocket("ws://127.0.0.1:8031/");
                ws.onopen = function() {
                    ws.send('websocekt测试');
                };
                ws.onmessage = function(e) {
                    alert("收到服务端的消息：" + e.data);
                };




              // 客户端网页js代码
                $(function () {

                    var inc = document.getElementById('incomming');
                    var input = document.getElementById('sendText');
                    inc.innerHTML += "connecting to server ..<br/>";

                    // create a new websocket and connect
                    // window.ws = new wsImpl('ws://127.0.0.1:8031/');
                    let ws=null;
                    $('#btnConnect').on('click', function () {
                       // create a new websocket and connect
                         ws=new WebSocket('ws://127.0.0.1:8031/');
                        // when data is comming from the server, this metod is called
                        ws.onmessage = function (evt) {
                            inc.innerHTML += evt.data + '<br/>';
                        };

                        // when the connection is established, this method is called
                        ws.onopen = function () {
                            inc.innerHTML += '.. connection open<br/>';
                        };

                        // when the connection is closed, this method is called
                        ws.onclose = function () {
                            inc.innerHTML += '.. connection closed<br/>';
                        }
                    });


                    $('#btnSend').on('click', function () {
                        var val = input.value;
                        if(!window.WebSocket||ws==null){return;}
                        if(ws.readyState == WebSocket.OPEN){
                            ws.send(message);
                        }else{
                            alert("WebSocket 连接没有建立成功！");
                        }
                    });

                    $('#btnDisconnect').on('click', function () {
                        ws.close();
                    });
                });
             */
            //endregion

            //启动WebSocket服务端
            CompletableFuture.runAsync(() ->
            {
                try {
                    new WebSocketsServer().runServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CompletableFuture.runAsync(() ->
            {
                WebSocketClient webSocketClient = new WebSocketClient();
                webSocketClient.runClient();
                //var sendResult = webSocketClient.SendMessage();

                //socket连接成功，还要握手成功，才能发送消息
                if (!webSocketClient.isHandshakeComplete()) {
                    LocalDateTime localDateTime1 = LocalDateTime.now();
                    while (!webSocketClient.isHandshakeComplete()) {
//                    new SpinWait().SpinOnce();
                    }
                    LocalDateTime localDateTime2 = LocalDateTime.now();

                    Duration duration = Duration.between(localDateTime1, localDateTime2);

                    int mills = duration.getNano() / 1000000;
                    System.out.println(MessageFormat.format("mills:{0}", mills));
                    webSocketClient.sendMessage();
                } else {
                    webSocketClient.sendMessage();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                webSocketClient.close();
            });


        } catch (Exception e) {

        }
    }

    public void nettySampleTest() {
        try {
//            new NettySampleServer().test();
            new NettySampleClient().test();
        } catch (Exception e) {

        }
    }

    public void nettyUDPTest() {
        try {
            new NettyUDPClient().test();
            new NettyUDPServer().test();
        } catch (Exception e) {

        }
    }


}
