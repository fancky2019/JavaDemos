package Test.opensource.Netty.WebSocketDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.ssl.SslHandler;
import utility.Action;

import java.net.SocketAddress;
import java.util.Locale;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpMethod.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    WebSocketServerHandshaker handshaker = null;


    //建立连接
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        System.out.println("Server: client " + socketAddress.toString() + " connected.");
        super.channelActive(ctx);
    }

    //断开连接
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        System.out.println("Server: client " + socketAddress.toString() + " disconnected.");
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            this.HandleHttpRequest(ctx, request);
        } else if (msg instanceof WebSocketFrame) {
            WebSocketFrame frame = (WebSocketFrame) msg;
            this.HandleWebSocketFrame(ctx, frame);
        }
    }


    //region  WebSocketIndexPageHandler -- FullHttpRequest
    void HandleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // Handle a bad request.
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), BAD_REQUEST,
                    ctx.alloc().buffer(0)));
            return;
        }

        // Allow only GET methods.
        if (!GET.equals(req.method())) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), FORBIDDEN,
                    ctx.alloc().buffer(0)));
            return;
        }

        // Send the index page
        if ("/".equals(req.uri()) || "/index.html".equals(req.uri())) {
            String uri = req.uri();

//            String webSocketLocation = getWebSocketLocation(ctx.pipeline(), req, websocketPath);
//            ByteBuf content = WebSocketServerIndexPage.getContent(webSocketLocation);
//            FullHttpResponse res = new DefaultFullHttpResponse(req.protocolVersion(), OK, content);
//
//            res.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
//            HttpUtil.setContentLength(res, content.readableBytes());
//
//            sendHttpResponse(ctx, req, res);
        }
        if ("/favicon.ico".equals(req.uri())) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), NOT_FOUND, ctx.alloc().buffer(0)));
        }


        String websocketPath = "/websocket";
        //建立websocket 连接
        // Handshake
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                getWebSocketLocation(ctx.pipeline(), req, websocketPath), null, true, 5 * 1024 * 1024);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }//
    }

    /**
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // Generate an error page if response getStatus code is not OK (200).
        HttpResponseStatus responseStatus = res.status();
        if (responseStatus.code() != 200) {
            ByteBufUtil.writeUtf8(res.content(), responseStatus.toString());
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }
        // Send the response and close the connection if necessary.
        boolean keepAlive = HttpUtil.isKeepAlive(req) && responseStatus.code() == 200;
        HttpUtil.setKeepAlive(res, keepAlive);
        ChannelFuture future = ctx.writeAndFlush(res);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
        String protocol = "ws";
        if (cp.get(SslHandler.class) != null) {
            // SSL in use so use Secure WebSockets
            protocol = "wss";
        }
        return protocol + "://" + req.headers().get(HttpHeaderNames.HOST) + path;
    }
    //endregion


    //region  WebSocketFrameHandler -- WebSocketFrame
    protected void HandleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // Check for closing frame
        //框架的关闭处理没有找到
        if (frame instanceof CloseWebSocketFrame) {
            this.handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }

        // ping and pong frames already handled
        if (frame instanceof PingWebSocketFrame) {
            ctx.write(new PongWebSocketFrame((ByteBuf) frame.content().retain()));
            return;
        }

        if (frame instanceof BinaryWebSocketFrame) {
            // Echo the frame
            ctx.write(frame.retain());
            return;
        }

        if (frame instanceof TextWebSocketFrame) {
            // Send the uppercase string back.
            String request = ((TextWebSocketFrame) frame).text();
            System.out.println("收到客户端信息" + request);
            ctx.channel().writeAndFlush(new TextWebSocketFrame("服务端收到客户端信息" + request));
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }
    //endregion


}
