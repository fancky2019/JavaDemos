package Test.test2020;

import io.netty.util.concurrent.CompleteFuture;

import java.net.*;
import java.util.concurrent.CompletableFuture;

/*
TCP: Test.test2019;


直接将DatagramPacket（包含数据、目标机器IP，端口）发送出去。

不适用TCP做多播，广播：性能问题，TCP建立连接，连接可能要求丢包重发或者延时或重组顺序。这些操作可能很消耗资源。
           不适于很多使用多播的应用场景。（同一时候多播不知道发出的包是不是已经到达，这个也导致不能使用TCP）
 */
public class UDPTest {

    public void test() {
        CompletableFuture.runAsync(() ->
        {
//            server();
//            multicastClient();
            broadcastClient();
        });

        CompletableFuture.runAsync(() ->
        {
            try {
                Thread.sleep(1000);
//                client();
//                multicastServer();
                broadcastServer();
            } catch (Exception ex) {

            }
        });
    }

    // region 单播
    private void server() {
        try {
            DatagramSocket socket = new DatagramSocket(6001);//创建Socket,指定UDP接收信息的端口
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            while (true) {
                //This method blocks until a datagram is received
                socket.receive(packet);
                byte[] arr = packet.getData();//获取数据
                int len = packet.getLength();//获取有效的字节个数
                String ip = packet.getAddress().getHostAddress();//获取remote的ip地址
                int port = packet.getPort();//获取remote的端口号
                System.out.println("Server receives:" + ip + ":" + port + ":" + new String(arr, 0, len));

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
            DatagramSocket socket = new DatagramSocket(6000);
            //往指定 IP，端口号发送信息。设置UDP包小点，防止包过大容易丢包
            DatagramPacket packet = new DatagramPacket(str.getBytes(), str.getBytes().length, InetAddress.getByName("127.0.0.1"), 6666);
            socket.send(packet);
            DatagramPacket receivedDatagramPacket = new DatagramPacket(new byte[1024], 1024);
            while (true) {
                //This method blocks until a datagram is received
                socket.receive(receivedDatagramPacket);
                byte[] arr = receivedDatagramPacket.getData();//获取数据
                int len = receivedDatagramPacket.getLength();//获取有效的字节个数
                String ip = receivedDatagramPacket.getAddress().getHostAddress();//获取remote的ip地址
                int port = receivedDatagramPacket.getPort();//获取remote的端口号
                System.out.println("Client receives:" + ip + ":" + port + ":" + new String(arr, 0, len));

            }
//            socket.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
    //endregion

    // region 多播
    private void multicastServer() {
        try {

            MulticastSocket multicastSocket = new MulticastSocket(6001);
            //   multicastSocket.setNetworkInterface(NetworkInterface.getByInetAddress(InetAddress.getByName("127.0.0.1")));

            String message = "Multicast Multicast Message";
            //相当于C# 的IPAddress。IPEndPoint包括IP和端口
            InetAddress multicastInetAddress = InetAddress.getByName("225.0.0.1");

            byte[] bytes = message.getBytes("UTF-8");
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, multicastInetAddress, 6000);
            multicastSocket.send(datagramPacket);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }

    private void multicastClient() {
        try {
            InetAddress multicastInetAddress = InetAddress.getByName("225.0.0.1");
            MulticastSocket multicastSocket = new MulticastSocket(6000);
            //默认的
//            multicastSocket.setNetworkInterface(NetworkInterface.getByInetAddress(InetAddress.getByName("192.168.1.105")));
            multicastSocket.joinGroup(multicastInetAddress);

            //设置UDP包小点，防止包过大容易丢包
            // 构造DatagramPacket实例，用来接收最大长度为1024字节的数据包
            byte[] data = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(data, 1024);
            // 接收报文，此方法在接收到数据报前一直阻塞
            multicastSocket.receive(receivePacket);


            System.out.println("Client receive from " + receivePacket.getAddress().getHostAddress() + ":" +
                    receivePacket.getPort() + "  Message:" +
                    new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8"));

            multicastSocket.leaveGroup(multicastInetAddress);

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }
    //endregion

    // region 广播
    private void broadcastServer() {
        try {
            // 构造DatagramSocket实例，指定本地端口6000
            DatagramSocket datagramSocket = new DatagramSocket(6001);
            String msg = "Server Broadcast Message";
            //255.255.255.255,不用255，用该子网段的广播地址
            InetSocketAddress broadcastInetSocketAddress = new InetSocketAddress("192.168.1.255", 6000);
            byte[] bytes = msg.getBytes("UTF-8");
            // 构造数据报包，指定数据包的IP为广播地址，端口为6001
            DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, broadcastInetSocketAddress);
            //发送报文
            datagramSocket.send(sendPacket);
            datagramSocket.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }

    private void broadcastClient() {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(6000);

            //设置UDP包小点，防止包过大容易丢包
            // 构造DatagramPacket实例，用来接收最大长度为1024字节的数据包
            byte[] data = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(data, 1024);
            // 接收报文，此方法在接收到数据报前一直阻塞
            datagramSocket.receive(receivePacket);
            System.out.println("Client receive from " + receivePacket.getAddress().getHostAddress() + ":" +
                    receivePacket.getPort() + "  Message:" +
                    new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8"));

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }
    //endregion
}
