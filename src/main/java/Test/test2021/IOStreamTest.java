package Test.test2021;

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
对象流	ObjectInputStream	ObjectOutputStream
 */
public class IOStreamTest {
    public void test()
    {

    }
}
