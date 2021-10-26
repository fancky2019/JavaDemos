package Test.test2021;

import Model.Student;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.ConverterUtil;
import utility.TXTFile;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;

/*
按照流的方向分为输入流（InputStream）与输出流(outputStream)：

输入流：只能读取数据，不能写入数据。能读入
输出流：只能写入数据，不能读取数据。能写出


InputStream(字节输入流)和Reader(字符输入流)通俗的理解都是读（read）的。
OutputStream(字节输出流)和Writer(字符输出流)通俗的理解都是写(writer)的。


//Write、Reader 针对字符操作 单位字符(2byte=16bit)
BufferedWriter BufferedReader 缓冲区 性能会好点

//针对流操作  单位字节(byte=8bit)
BufferedInputStream  BufferedOutputStream



抽象基类	InputStream	OutputStream	Reader	Writer
访问文件	FileInputStream	FileOutputStream	FileReader	FileWriter
访问数组	ByteArrayInputStream	ByteArrayOutputStream	CharArrayReader	CharArrayWriter
访问字符串	 	 	StringReader	StringWriter
缓冲流	BufferedInputStream	BufferedOutputStream	BufferedReader	BufferedWriter
转换流	 	 	InputStreamReader	OutputStreamWriter
对象流	(JDK默认序列化)ObjectInputStream	ObjectOutputStream

基本数据类型转换成流 DataInputStream DataOutputStream


注：java里内存流用：ByteArrayInputStream:将内容写入到内存中，
                 ByteArrayOutputStream：将内存中数据输出
 */
public class IOStreamTest {
    private static final Logger logger = LogManager.getLogger(Log4J2Test.class);

    public void test() {
//        objectStreamTest();
//        bufferedStreamTest();
        dataStreamTest();
    }

    // region 文件流
    private void fileStreamTest() {
        //  utility.TXTFile
        //	FileInputStream	FileOutputStream	FileReader	FileWriter
        TXTFile txtFile = new TXTFile();
    }
    //endregion

    // region 内存流--字节流
    //ByteArrayInputStream:将内容写入到内存中，
    //ByteArrayOutputStream：将内存中数据输出

    //参见对象流objectStreamTest
    //endregion

    // region 缓冲流

    /*
    基于缓冲区操作，每次可以操作缓冲区大小读写，
    文当件比较大的时候可减少io读写次数提高性能。
     */
    private void bufferedStreamTest() {

        try {
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(arrayOutputStream);
            String content = "123456";
            bos.write(content.getBytes(), 0, content.getBytes().length);
            bos.flush();
            bos.close();


            byte[] sourceArray = arrayOutputStream.toByteArray();
            ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(sourceArray);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(arrayInputStream);

//            //socket接收报文长度及报文数据
//            //  bufferedInputStream.read();//每次读取一个byte
//            //接收发送的数据长度：约定四个字节
//            int len = 256;
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            DataOutputStream dos = new DataOutputStream(byteArrayOutputStream);
//            dos.writeInt(len);
//            dos.flush();
//            dos.close();
//            byte[] lenBytes = byteArrayOutputStream.toByteArray();
//
//            ByteArrayInputStream arrayInputStream1 = new ByteArrayInputStream(lenBytes);
//            BufferedInputStream bufferedInputStream1 = new BufferedInputStream(arrayInputStream1);
//            byte[] buffer = new byte[4];
//            int receivedLength = -1;
//            while ((receivedLength = bufferedInputStream1.read(buffer)) != -1) {
//                //约定4个字节
////                 byte[] temp = new byte[receivedLength];
////                 System.arraycopy(buffer, 0, temp, 0, receivedLength);
//                DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(buffer, 0, receivedLength));
//                int length1 = dataInputStream.readInt();
//
//                //  int length = ConverterUtil.bytesToInt(buffer);
//                byte[] data = new byte[length1];//避免每次都创建可以在while创建一个byte[]使用完clear
//                //list.clear()
//                // bufferedInputStream.read(readInBytes);//读入readInBytes，读取readInBytes的长度的byte
//            }


            //read()会block,available()不会block
            byte[] buffer = new byte[4];
            StringBuilder sb = new StringBuilder();
            while (bufferedInputStream.available() > 0) {
                int readLength = bufferedInputStream.read(buffer);
                sb.append(new String(buffer, 0, readLength));
            }
            String str = sb.toString();


            arrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //endregion

    // region 对象流--jdk默认序列化
    private void objectStreamTest() {
        //序列化
        try (
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream)) {
            //jdk默认序列化
            //待序列化的类不许要实现Serializable接口，否则报java.io.NotSerializableException:
            Student student = new Student("fancky2", 4);
            oos.writeObject(student);
            oos.flush();
            byte[] objectArrays = byteArrayOutputStream.toByteArray();
            int length = objectArrays.length;

//            byteArrayOutputStream.close();


            //反序列化
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objectArrays);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Student student1 = (Student) objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
            int m = 0;
        } catch (Exception ex) {
            logger.error("", ex);
        }


    }
    //endregion

    // region DataInputStream 将基本类型转换成流
    /*
    DataOutputStream数据输出流允许应用程序将基本Java数据类型写到基础输出流中,
    DataInputStream数据输入流允许应用程序以机器无关的方式从底层输入流中读取基本的Java类型.
     */
    private void dataStreamTest() {

        //BigInteger、BigDecimal用ObjectOutputStream处理，待验证
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            int num = 10;
            char ch = 'd';
            byte[] strByte = "abc".getBytes();
            dataOutputStream.writeInt(num);
            byte[] intBytes = byteArrayOutputStream.toByteArray();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(intBytes);
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
            int num1 = dataInputStream.readInt();
            int m = 0;
//            DataInputStream dataInputStream=new DataInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //endregion
}
