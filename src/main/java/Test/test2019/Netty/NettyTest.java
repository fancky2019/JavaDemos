package Test.test2019.Netty;

import Test.test2019.Netty.NettyProduction.NetttyServerProduction;
import Test.test2019.Netty.NettyProduction.NettyClientProduction;
import sun.security.krb5.internal.NetClient;

import java.util.concurrent.CompletableFuture;

public class NettyTest {
    public void test() {

        CompletableFuture.runAsync(() ->
        {
            //  new NettyServer().test();
            new NetttyServerProduction().test();

        });
        CompletableFuture.runAsync(() ->
        {
            //   new NettyClient().test();
//            NettyClientProduction nettyClientProduction = new NettyClientProduction();
////            nettyClientProduction.connect(()->
////            {
////                for (int i = 1; i <= 2; i++) {
////                    MessageInfo messageInfo = new MessageInfo();
////
////                    messageInfo.setMessageType(MessageType.HeartBeat);
////                    messageInfo.setBody("HeartBeat");
////                    nettyClientProduction.sendData(messageInfo);
////                }
////            });
        });


    }
}
