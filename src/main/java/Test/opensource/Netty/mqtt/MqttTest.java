package Test.opensource.Netty.mqtt;

import Test.opensource.Netty.mqtt.protocol.MqttNettyClient;
import Test.opensource.Netty.mqtt.protocol.MqttNettyServer;

import java.util.concurrent.CompletableFuture;

/*
client,server 是从网上下载的代码，未调试通。

pro

broker: Mosquitto
spring 集成
spring-integration-mqtt

mqtt 客户端工具 mqtt.fx
 */
public class MqttTest {
    public void test() {
//        CompletableFuture.runAsync(() ->
//        {
//            try {
//                new MqttServer().start();
//            } catch (Exception ex) {
//                String msg = ex.getMessage();
//            }
//        });
//        CompletableFuture.runAsync(() ->
//        {
//            try {
//                Thread.sleep(2000);
//                MqttClient.getInstance().run();
//            } catch (Exception ex) {
//                String msg = ex.getMessage();
//            }
//        });

        nettyProductionTest();
    }

    private void nettyProductionTest() {

//        GeneratorCertKey.Generator();

        CompletableFuture.runAsync(() ->
        {
            new MqttNettyServer().test();

        });
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {

        }
        CompletableFuture.runAsync(() ->
        {

            MqttNettyClient nettyClientProduction = new MqttNettyClient();
            nettyClientProduction.connect(() ->
            {
                nettyClientProduction.sendData();
            });
        });
    }
}
