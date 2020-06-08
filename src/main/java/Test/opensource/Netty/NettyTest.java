package Test.opensource.Netty;

import java.util.concurrent.CompletableFuture;

public class NettyTest {
    public void test() {

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
}
