package Test.opensource.Netty.WebSocketDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/*
github:https://github.com/netty/netty/blob/4.1/example/src/main/java/io/netty/example/http/websocketx/server/WebSocketServer.java
 */
public class WebSocketsServer {

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
    final boolean SSL = false;
    final int PORT = 8031;

    EventLoopGroup bossGroup;
    EventLoopGroup workerGroup;
    Channel ch;

    public void runServer() throws Exception {
        // Configure SSL.
//        final SslContext sslCtx;
//        if (SSL) {
//            SelfSignedCertificate ssc = new SelfSignedCertificate();
//            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
//        } else {
//            sslCtx = null;
//        }

        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.handler(new LoggingHandler(LogLevel.INFO));
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                               private static final String WEBSOCKET_PATH = "/websocket";

                               @Override
                               public void initChannel(SocketChannel ch) throws Exception {
                                   ChannelPipeline pipeline = ch.pipeline();
                                   pipeline.addLast(new HttpServerCodec());
                                   pipeline.addLast(new HttpObjectAggregator(65536));
//                                   pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));

                                   pipeline.addLast(new WebSocketServerHandler());
                               }
                           }
            );

            ch = b.bind(PORT).sync().channel();

            System.out.println("Open your web browser and navigate to " +
                    (SSL ? "https" : "http") + "://127.0.0.1:" + PORT + '/');
            // Wait until the connection is closed.
//            ch.closeFuture().sync();

        } catch (Exception ex) {
        } finally {

//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
        }

    }

    public void close() {
        try {
            if (ch == null) {
                return;
            }
            ch.close();

            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
