package Test.test2021;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4J2Test {


//%d 日期格式，默认形式为2012-11-02 14:34:02,781
//%p 日志级别
//%c{1.} %c表示Logger名字，{1.}表示精确度。若Logger名字为org.apache.commons.Foo，则输出o.a.c.Foo。
//%t 处理LogEvent的线程的名字
//%m       日志内容
//%n 行分隔符。"\n"或"\r\n"。
    private static final Logger logger = LogManager.getLogger(Log4J2Test.class);

    public  void  test()
    {
        fun();
    }

    //region 配置邮件

    /*
    参考链接：https://blog.csdn.net/david_pfw/article/details/85846351
    1、
//       <!--        log4j2邮件-->
//        <dependency>
//            <groupId>javax.activation</groupId>
//            <artifactId>activation</artifactId>
//            <version>1.1.1</version>
//        </dependency>
//        <dependency>
//            <groupId>com.sun.mail</groupId>
//            <artifactId>javax.mail</artifactId>
//            <version>1.5.4</version>
//        </dependency>

    2、配置qq邮箱。开启POP3/SMTP服务

     邮箱--设置--账号--POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV服务--开启服务：
     POP3/SMTP服务 (如何使用 Foxmail 等软件收发邮件？)已开启 |  关闭
     注：过一段时间密码可能失效
    3、

    如果使用端口为465，将protocol的smtp改为smtps
    将配置文件端口改为587，则可以使用smtp。
    均为SSL连接端口，因为qq不支持非SSL端口。
    以下是配置文件，重点在于protocal（视端口而定）和开启SSL（必须）


    smtpHost="smtp.qq.com"
    smtpPassword="ipxczauxtutggecb" 开通smtp服务的授权码
            <smtp name="Mail" subject="Error Log" to="709737588@qq.com,517312606@qq.com" from="1513918351@qq.com"
              replyTo="1513918351@qq.com" smtpHost="smtp.qq.com"  smtpDebug="false" smtpProtocol="smtps"
              smtpUsername="1513918351@qq.com" smtpPassword="ipxczauxtutggecb" smtpPort="465" bufferSize="1024">
              <!--定义error级别日志才发-->
            <ThresholdFilter level="error" onMatch="ACCEPT" onMismatch="DENY"/>
    */


    //endregion
    private  void  fun()
    {
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
        logger.fatal("fatal");

        try {
            int m=Integer.parseInt("d");
        }
        catch (Exception ex){
            logger.error(ex);//不打印堆栈信息，java.lang.NumberFormatException: For input string: "d"
            logger.error("",ex);//打印异常信息并带堆栈信息
            logger.error("输出字符串测试",ex);//
        }

    }
}
