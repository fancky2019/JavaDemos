package Test.test2020;

import io.netty.util.concurrent.CompleteFuture;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;

/*
TCP: Test.test2019;
 */
public class UDPTest {

    public void test() {
        CompletableFuture.runAsync(() ->
        {
            server();
        });

        CompletableFuture.runAsync(() ->
        {
            try {
                Thread.sleep(100);
                client();
            } catch (Exception ex) {

            }
        });
    }

    private void server() {
        try {
            DatagramSocket socket = new DatagramSocket(6666);//创建Socket,指定UDP接收信息的端口
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            while (true) {
                //This method blocks until a datagram is received
                socket.receive(packet);
                byte[] arr = packet.getData();//获取数据
                int len = packet.getLength();//获取有效的字节个数
                String ip = packet.getAddress().getHostAddress();//获取remote的ip地址
                int port = packet.getPort();//获取remote的端口号
                System.out.println("Server receives:"+ip + ":" + port + ":" + new String(arr, 0, len));

                String responseStr = "Server";
                DatagramPacket sendedDatagramPacket = new DatagramPacket(responseStr.getBytes(), responseStr.getBytes().length, InetAddress.getByName(ip), port);
                socket.send(sendedDatagramPacket);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    private void client() {
        String str = "client";
        try {
            DatagramSocket socket = new DatagramSocket(7777);
            //往指定 IP，端口号发送信息
            DatagramPacket packet = new DatagramPacket(str.getBytes(), str.getBytes().length, InetAddress.getByName("127.0.0.1"), 6666);
            socket.send(packet);
            DatagramPacket receivedDatagramPacket = new DatagramPacket(new byte[1024], 1024);
            while (true)
            {
                //This method blocks until a datagram is received
                socket.receive(receivedDatagramPacket);
                byte[] arr = receivedDatagramPacket.getData();//获取数据
                int len = receivedDatagramPacket.getLength();//获取有效的字节个数
                String ip = receivedDatagramPacket.getAddress().getHostAddress();//获取remote的ip地址
                int port = receivedDatagramPacket.getPort();//获取remote的端口号
                System.out.println("Client receives:"+ip + ":" + port + ":" + new String(arr, 0, len));

            }
//            socket.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
