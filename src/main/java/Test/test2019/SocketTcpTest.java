package Test.test2019;

import Test.opensource.Netty.MessageInfo;
import Test.opensource.Netty.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import utility.ConverterUtil;
import utility.Hex;

import java.io.*;
import java.net.*;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/*
TCP:ServerSocket、Socket
UDP：Test.test2020; UDPTest DatagramSocket DatagramPacket
MTU:1500 byte


按照流的方向分为输入流（InputStream）与输出流(outputStream)：

输入流：只能读取数据，不能写入数据。能读入
输出流：只能写入数据，不能读取数据。能写出


InputStream(字节输入流)和Reader(字符输入流)通俗的理解都是读（read）的。
OutputStream(字节输出流)和Writer(字符输出流)通俗的理解都是写(writer)的。

//Write、Reader 针对字符操作 单位字符(2byte=16bit)
BufferedWriter BufferedReader 缓冲区 性能会好点

//针对流操作  单位字节(byte=8bit)
BufferedInputStream  BufferedOutputStream
 */
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


//        CompletableFuture.runAsync(() ->
//        {
//            serverReceiveByte();
//        });
//        CompletableFuture.runAsync(this::clientSendByte);
    }

    List<Socket> connectedSockets = new LinkedList<>();

    private void server() {
        try {
            ServerSocket serverSocket = new ServerSocket(8031);
            System.out.println("启动服务器....");

            //广播消息
            CompletableFuture.runAsync(() ->
            {
                while (true) {
                    connectedSockets.forEach(client ->
                    {
                        try {

                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(client.getOutputStream());
                            String msg = MessageFormat.format("send message{0}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                            System.out.println(MessageFormat.format("server sends message:{0}", msg));
                            //发送信息给客户端
//                            outputStreamWriter.write(msg);
//                            outputStreamWriter.flush();//任何流进行写入完成后请调用flush()方法推送下

                            //使用BufferedWriter缓冲区包装OutputStreamWriter
                            BufferedWriter bw = new BufferedWriter(outputStreamWriter);
                            bw.write(msg);
                            bw.flush();//任何流进行写入完成后请调用flush()方法推送下
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
                connectedSockets.add(clientSocket);
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
            Socket clientSocket = new Socket("127.0.0.1", 8031);
//            clientSocket.connect(new InetSocketAddress("127.0.0.1", 8888));

            boolean isConnected = clientSocket.isConnected();
            //构建IO
            if (!clientSocket.isConnected()) {
                throw new Exception("unconnected!");
            }
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
                //定长、固定头部长度，分隔符

                //region项目中采用几个固定字节长度的报文，然后读取发送的长度报文.
                /*C#处理demo
                     while ((i = stream.Read(bytes, 0, 2)) != 0)
                        {
                            byte[] lengthBytes = bytes.Take(2).Reverse().ToArray();

                            //stream.Read(bytes, 0, 2);
                            var msgLength = BitConverter.ToInt16(lengthBytes, 0);
                            Array.Clear(bytes, 0, 2);
                            stream.Read(bytes, 0, msgLength);
                            var data1 = System.Text.Encoding.ASCII.GetString(bytes, 0, 2);//1C
                            var data2 = bytes.Skip(2).Take(1).ToArray()[0];//5档
                            var data3 = bytes.Skip(3).Take(1).ToArray()[0]; //1档
                            var data4 = BitConverter.ToUInt64(bytes.Skip(4).Take(8).ToArray(), 0);//timestamp
                            var data5 = System.Text.Encoding.ASCII.GetString(bytes, 12, msgLength - 12);//消息

                            data = $"{data1}{data2}{data3}{data4}{data5}";
                            Console.WriteLine("Server Received: {0}", data);

                            _nLog.Info(data);
                        }
                */
                //endregion
                byte[] recvBuf = new byte[1024];
                try {
                    //region约定报文长度
//                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//                    //  bufferedInputStream.read();//每次读取一个byte
//                    //接收发送的数据长度：约定四个字节
//                    byte[] buffer = new byte[4];
//                    int receivedLength = -1;
//                    while ((receivedLength = bufferedInputStream.read(buffer)) != -1) {
//                        //约定4个字节
////                 byte[] temp = new byte[receivedLength];
////                 System.arraycopy(buffer, 0, temp, 0, receivedLength);
//                        int length = ConverterUtil.bytesToInt(buffer);
//                        byte[] data = new byte[length];//避免每次都创建可以在while创建一个byte[]使用完clear
//                        ArrayUtils.removeAll(data);//避免每次都创建可以在while创建一个byte[]使用完clear
//                        // bufferedInputStream.read(readInBytes);//读入readInBytes，读取readInBytes的长度的byte
//                    }
//endregion

                    while ((recvMsgSize = inputStream.read(recvBuf)) != -1) {
//                        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                        // bufferedReader.readLine(); // 注意事项：readLine()要求有换行标识，write()要输出换行标识，要调用flush()刷新缓冲区。
                        // bufferedReader.read(temp, 0, recvMsgSize);

//                        BufferedInputStream  bufferedInputStream=new  BufferedInputStream(inputStream);
//                        int readLength=  bufferedInputStream.read(recvBuf);

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

    //MessagePack
    private void clientSendByte() {
        try {

            //服务器的IP，端口
            Socket clientSocket = new Socket("127.0.0.1", 8031);
//            clientSocket.connect(new InetSocketAddress("127.0.0.1", 8888));

            boolean isConnected = clientSocket.isConnected();
            //构建IO
            if (!clientSocket.isConnected()) {
                throw new Exception("unconnected!");
            }
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
//            //向服务器端发送一条消息
//            bw.write("client send str");
//            bw.flush();

            DataOutputStream dos = new DataOutputStream(outputStream);


            MessageInfo msg = new MessageInfo();
            msg.setMessageType(MessageType.HeartBeat);
            msg.setBody("data");

            ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
//            objectMapper.setAnnotationIntrospector(new JsonArrayFormat());
            while (true) {
                //33 byte
                byte[] bytes = objectMapper.writeValueAsBytes(msg);
                MessageInfo deserialized = objectMapper.readValue(bytes, MessageInfo.class);

                System.out.println(bytes.toString());
                System.out.println(Hex.encodeHexString(bytes, true));

                dos.write(bytes, 0, bytes.length);
                dos.flush();
                BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
                String str = strin.readLine();
            }
//            dos.flush();
//            dos.close();


        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void serverReceiveByte() {
        try {
            ServerSocket serverSocket = new ServerSocket(8031);
            System.out.println("启动服务器....");


            int recvMsgSize;
            //约定好定长，方便处理粘包
            //定长、固定头，分隔符
            byte[] recvBuf = new byte[1024];
            //用心跳检测客户端是否断开连接
            while (true) {
                Socket clientSocket = serverSocket.accept();
                //
                connectedSockets.add(clientSocket);
                System.out.println(MessageFormat.format("客户端:{0}:{1}已连接到服务器", clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort()));
                InputStream in = clientSocket.getInputStream();
                //   recvMsgSize = in.read(recvBuf);
                while ((recvMsgSize = in.read(recvBuf)) != -1) {
                    byte[] temp = new byte[recvMsgSize];
                    System.arraycopy(recvBuf, 0, temp, 0, recvMsgSize);


                    ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());

                    MessageInfo deserialized = objectMapper.readValue(temp, MessageInfo.class);
                    System.out.println(MessageFormat.format("server receives message:{0}", deserialized.toString()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {

        }
    }
}
