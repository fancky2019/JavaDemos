
import Test.opensource.Jackson.JacksonTest;
import Test.opensource.Netty.NettySample.NettySampleClient;
import Test.opensource.Netty.NettySample.NettySampleServer;
import Test.opensource.Netty.NettySample.NettyUdp.NettyUDPClient;
import Test.opensource.Netty.NettySample.NettyUdp.NettyUDPServer;
import Test.opensource.Netty.NettyTest;
import Test.opensource.msgpack.JacksonDataformatMsgpack;
import Test.opensource.msgpack.MsgPackTest;
import Test.opensource.orgapachecommons.CollectionsCommons;
import Test.opensource.orgapachecommons.CsvCommons;
import Test.opensource.orgapachecommons.StringCommons;
import Test.opensource.protobuf.ProtoBufTest;
import Test.opensource.rabbitMQ.RabbitMQTest;
import Test.opensource.redis.RedisTest;
import Test.opensource.sqlite.SQLiteTest;
import Test.opensource.zookeeper.ZooKeeperTest;
import Test.test2018.CalendarTest;
import Test.test2018.EnumTest;
import Test.test2018.JDBCTest;
import Test.test2018.Java8Test;
import Test.test2019.HashCodeTest;
import Test.test2019.MysqlTest;
import Test.test2019.SocketTcpTest;
import Test.test2020.ByteBase64String;
import Test.test2020.ByteConverterTest;
import Test.test2020.ConcurrentHashMapTest;
import Test.test2020.UDPTest;
import Test.test2021.*;
import Test.test2021.extendImplement.ExtendImplementTest;
import utility.BigInterTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


/*
Java 包名按惯例一律小写，即使中间的单词也需要小写，与驼峰命名不同
 */
public class Main {

    public static void main(String[] args) {
        try {

//          String dir=  System.getProperty("user.home");

//            Log4j2 完全异步模式
//            System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");


            //region test2018

            //获取包名
            //  String packageName = ImpTest.class.getPackage().getName();

            //region  根路径

            /**
             //            //:相对路径(即不写明时候到底相对谁)均可通过以下方式获得（不论是一般的java项目还是web项目）
             //            String relativelyPath = System.getProperty("user.dir");
             //            System.out.println(relativelyPath);
             //this.getClass().getClassLoader().getResource("").getPath(); // 经过测试，这种方法是安全的，最有效的
             //            File f = new File(Main.class.getResource("").getPath());
             //            System.out.println(f);
             //
             //            System.out.println(f.getParent());
             //
             **/
            //endregion


//        System.out.println("Hello World!");
//        Person person=new PersonImp();
//        person.getName();
//
            // new SetTest().test();
//               new ThreadTest().test();

//            new CalendarTest().test();

//            ReferenceTest referenceTest=new ReferenceTest();
//            String str="123";
//            referenceTest.stringReference(str);
//
//            Student student=new Student();
//            student.setName("li");
//            student.setAge(1);
//
//            referenceTest.classParam(student);
//            Student returnObj=referenceTest.changeObj(student);
            //  new SetTest().test();

            //  new FileTest().test();
//            new ReflectDemo().test();
//            new EnumTest().test();

//              new ClassExecutionSequenceTest().test();
//            new ClassLoaderTest().test();


            //  new PropertiesTest().test1();


            //   new LambdaTest().test();
//             new CalendarTest().test();
//             new Java8Test().test();
//               new OptionalTest().test();
            //   new Configs().test();

//              new RedisTest().test();

            //  new ProduceConsumerTest(100).Test();
            //   new ProduceConsumerConditionTest(100).Test();

            //  new ProducerConsumerTPSTest(100,5).Test();

            //   new AnonymousInternalClassTest().test();


//            ExtendTest extendTest=new ExtendTest() ;
//            extendTest.publicFunction();
//            extendTest.publicFunction1();


//              new ConvertTest().test();
            //  new AnnotationTest().test();

//            new RabbitMQTest().test();
            //  new  QueueTest().test();

//               new  GenericTest().test();

            //endregion

            //region test2019
            //    new TryCatchFinallyReturnTest().test();
            //  new  ProxyTest().test();


            //  new  StreamOptionalTest().test();
            // new  EqualsOperatorTest().test();
            //    new  BoxingUnboxingTest().test();

//            new NettyTest().test();

//            new NettyTest().nettyWebSocket();

//            new StringTest().test();
            //  new TimerTest().test();


            //   new CharTest().test();
            //   new BlockingQueueTest().test();
            // new TypeJudgeTest().test();

            //  new DivisionTest().test();
//            new HashCodeTest().test();
            //   new LeftMoveTest().test();
            //  new RedissonTest().test();
//              new ConstructorTest().test();
//            new JDBCTest().test();
//            new MysqlTest().test();
//            new ReferenceDemo().test();

//            new SerializableTest().test();
//            new JwtTest().test();

//            new KafkaTest().test();
//            new SemaphoreTest().test();
//            new AtomicIntegerTest().test();
//              new VolatileTest().test();

//            new BlockingQueueTest().delayQueueFun();
            // new ParamsTest().test();

//            new JestTest().test();
//            new ThreadLocalTest().test();
//            new Log4j2Demo().test();

//            new LinkedBlockingQueueTest().test();

//            new LockTest().test();
//            new SocketTcpTest().test();

            //endregion

            //region test2020

//            new ClassCastTest().test();

//            new UDPTest().test();
//            new ParallelTest().test();
//            new StopwatchTest().test();


//            new MsgPackTest().test();

//            new JacksonDataformatMsgpack().test();
//            new ByteConverterTest().test();
//            new JacksonTest().test();

//            new SQLiteTest().test();
//            new ZooKeeperTest().test();
//            new ConcurrentHashMapTest().test();
//            new ProtoBufTest().test();

//            new ByteBase64String().test();
//            new BigInterTest().test();

            //endregion

            //region 20121
//            new StringCommons().test();
//            new CollectionsCommons().test();

//            new CsvCommons().test();
//           new ThreadExceptionTest().test();
//            new ExtendImplementTest().test();
//            new FormatTest().test();

//            new BigDecimalTest().test();
//            new DefaultValueTest().test();

//            new StringJoinerTest().test();
            new RandomTest().test();

            //endregion


//            System.out.println(MessageFormat.format("main :{0}", "started!"));
            BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
            String str = strin.readLine();
//            System.exit(0) ;
            Runtime.getRuntime().exit(0);
            Integer a = 0;
        } catch (Exception ex) {
            String str = ex.getMessage();
            System.out.println(MessageFormat.format("main :{0}", str));
            Integer a = 0;
        }
    }
}
