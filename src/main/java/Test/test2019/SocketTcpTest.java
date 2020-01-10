package Test.test2019;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SocketTcpTest {
    public void test() {

        CompletableFuture.runAsync(() ->
        {
            server();
        });
        CompletableFuture.runAsync(() ->
        {
            client();
        });
    }

    List<Socket> connnectedSockets = new LinkedList<>();

    private void server() {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("启动服务器....");

            //广播消息
            CompletableFuture.runAsync(() ->
            {
                while (true) {
                    connnectedSockets.forEach(client ->
                    {
                        try {
                            //发送信息给客户端
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                            bw.write(MessageFormat.format("send message{0}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
                            bw.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });

            int recvMsgSize;
            //约定好定长，方便处理粘包
            //定长、固定头，分隔符
            byte[] recvBuf = new byte[1024];
            //用心跳检测客户端是否断开连接
            while (true) {
                Socket clientSocket = serverSocket.accept();
                //
                connnectedSockets.add(clientSocket);
                System.out.println(MessageFormat.format("客户端:{0}:{1}已连接到服务器", clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort()));
                InputStream in = clientSocket.getInputStream();
                //   recvMsgSize = in.read(recvBuf);
                while ((recvMsgSize = in.read(recvBuf)) != -1) {
                    byte[] temp = new byte[recvMsgSize];
                    System.arraycopy(recvBuf, 0, temp, 0, recvMsgSize);
                    String receiveMsg = new String(temp);
                    System.out.println(MessageFormat.format("server receives message:{0}", receiveMsg));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {

        }
    }

    private void client() {
        try {

            //服务器的IP，端口
            Socket clientSocket = new Socket("127.0.0.1", 8888);

            //构建IO
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            //向服务器端发送一条消息
            bw.write("client send str");
            bw.flush();

            //读取服务器返回的消息
            CompletableFuture.runAsync(() ->
            {
                int recvMsgSize;
                //约定好定长，方便处理粘包
                //定长、固定头，分隔符
                byte[] recvBuf = new byte[1024];
                try {
                    while ((recvMsgSize = inputStream.read(recvBuf)) != -1) {
                        byte[] temp = new byte[recvMsgSize];
                        System.arraycopy(recvBuf, 0, temp, 0, recvMsgSize);
                        String receiveMsg = new String(temp);
                        System.out.println(MessageFormat.format("client receives message:{0}", receiveMsg));
                    }
                } catch (Exception ex) {

                }
            });

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
