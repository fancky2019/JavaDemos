package Test.test2021;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4J2Test {

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

     邮箱--设置--POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV服务--开启服务：
     POP3/SMTP服务 (如何使用 Foxmail 等软件收发邮件？)已开启 |  关闭

    3、
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


    }
}
