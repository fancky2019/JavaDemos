package Test.test2021;

public class JvmParamsTest {
    /*
     启动jar设置jvm参数:nohup java -jar -Xms512M -Xmx2048M -XX:PermSize=512M -XX:MaxPermSize=1024M project.jar
    -Xmx Java Heap最大值，默认值为物理内存的1/4，最佳设值应该视物理内存大小及计算机内其他内存开销而定；-Xmx512m 将最大堆大小设置为 512 兆字节
    -Xms Java Heap初始值，Server端JVM最好将-Xms和-Xmx设为相同值， -Xms512m 将初始堆大小设置为 512 兆字节



    java -Xms1024m -Xmx1024m -jar app.jar & 设置jvm参数
    建议初始化堆的大小和最大堆的大小设置一致，减少GC
     */

    /*
    将对象设置为 null 只是栈中指向的引用为 null，但是 new 出来的对象还是存在于堆里面的，等待survior1 or survior2 满的时候 JVM 才会调用 GC 命令清除对应 survior 区的对象，将没有栈指向的对象给回收掉
     */

}
