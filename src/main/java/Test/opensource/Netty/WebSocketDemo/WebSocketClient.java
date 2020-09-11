package Test.opensource.Netty.WebSocketDemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import utility.Action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.MessageFormat;

/**
 * @Auther fancky
 * @Date 2020-9-11 15:14
 * @Description
 */
public class WebSocketClient {

    static final String URL = System.getProperty("url", "ws://127.0.0.1:8031/websocket");
    public boolean handshakeComplete;

    Channel ch;
    EventLoopGroup group;

    public boolean isHandshakeComplete() {
        return handshakeComplete;
    }

    public void runClient() {
        try {
            URI uri = new URI(URL);

            String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
            final String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
            final int port;
            if (uri.getPort() == -1) {
                if ("ws".equalsIgnoreCase(scheme)) {
                    port = 80;
                } else if ("wss".equalsIgnoreCase(scheme)) {
                    port = 443;
                } else {
                    port = -1;
                }
            } else {
                port = uri.getPort();
            }

            if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
                System.err.println("Only WS(S) is supported.");
                return;
            }

            final boolean ssl = "wss".equalsIgnoreCase(scheme);
//        final SslContext sslCtx;
//        if (ssl) {
//            sslCtx = SslContextBuilder.forClient()
//                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
//        } else {
//            sslCtx = null;
//        }

            group = new NioEventLoopGroup();

            // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
            // If you change it to V00, ping is not supported and remember to change
            // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
            WebSocketClientHandler handler =
                    new WebSocketClientHandler(
                            WebSocketClientHandshakerFactory.newHandshaker(
                                    uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));
            handler.handshakeComplete = () -> this.handshakeComplete = true;

            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
//                            if (sslCtx != null) {
//                                p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
//                            }
                            p.addLast(
                                    new HttpClientCodec(),
                                    new HttpObjectAggregator(8192),
                                    WebSocketClientCompressionHandler.INSTANCE,
                                    handler);
                        }
                    });

            ch = b.connect(uri.getHost(), port).sync().channel();
            handler.handshakeFuture().sync();

//            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
//            while (true) {
//                String msg = console.readLine();
//                if (msg == null) {
//                    break;
//                } else if ("bye".equals(msg.toLowerCase())) {
//                    ch.writeAndFlush(new CloseWebSocketFrame());
//                    ch.closeFuture().sync();
//                    break;
//                } else if ("ping".equals(msg.toLowerCase())) {
//                    WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
//                    ch.writeAndFlush(frame);
//                } else {
//                    WebSocketFrame frame = new TextWebSocketFrame(msg);
//                    ch.writeAndFlush(frame);
//                }
//            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
//            group.shutdownGracefully();
        }
    }

    public boolean sendMessage() {
        if (!this.handshakeComplete) {
            System.out.println("Handshake is not completed.");
            return false;
        }
        String msg = "websocket test";
        //WebSocketFrame frame = new TextWebSocketFrame(msg);


        ByteBuf buffer = Unpooled.wrappedBuffer(msg.getBytes(Charset.forName("UTF-8")));
        WebSocketFrame frame = new TextWebSocketFrame(buffer);


        try {
            ch.writeAndFlush(frame).sync();
        } catch (Exception ex) {
            return false;
        }

        System.out.println(MessageFormat.format("Client sended :{0}", msg));
        return true;
    }

    public void close() {

        try {
            //一直阻塞：服务器同步连接断开时,这句代码才会往下执行
//            ChannelFuture future = ch.closeFuture().sync();
            ch.close();
            group.shutdownGracefully();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}