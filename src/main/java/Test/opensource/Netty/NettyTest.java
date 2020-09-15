package Test.opensource.Netty;

import Test.opensource.Netty.NettySample.NettySampleClient;
import Test.opensource.Netty.NettySample.NettySampleServer;
import Test.opensource.Netty.NettySample.NettyUdp.NettyUDPClient;
import Test.opensource.Netty.NettySample.NettyUdp.NettyUDPServer;
import Test.opensource.Netty.WebSocketDemo.WebSocketClient;
import Test.opensource.Netty.WebSocketDemo.WebSocketsServer;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class NettyTest {
    public void test() {

//        nettyTest();
//        nettyWebSocket();
        nettySampleTest();
//        nettyUDPTest() ;

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
