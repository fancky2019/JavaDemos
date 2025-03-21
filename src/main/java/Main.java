
import Test.opensource.Jackson.JacksonTest;
import Test.opensource.Netty.mqtt.MqttTest;
import Test.opensource.log.Log4j2Demo;
import Test.opensource.orgapachecommons.CollectionsCommons;
import Test.opensource.orgapachecommons.commonpool.CommonPoolTest;
import Test.opensource.protobuf.JprotobufTest;
import Test.opensource.redis.RedisTest;
import Test.opensource.redis.RedissonTest;
import Test.test2018.Java8Test;
import Test.test2019.TryCatchFinallyReturnTest;
import Test.test2019.TryTest;
import Test.test2019.proxy.ProxyTest;
import Test.test2021.CompletableFutureTest;
import Test.test2021.RandomTest;
import Test.test2021.designpattern.TemplateMethodPattern;
import Test.test2022.ArrayTest;
import Test.test2023.TimestampTest;
import Test.test2023.flinkcdc.ClickHouseTest;
import Test.test2024.DateTimeSequence;
import Test.test2024.ThreadStateDemo;
import Test.test2025.HashedWheelTimerTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


/*
Java 包名按惯例一律小写，即使中间的单词也需要小写，与驼峰命名不同
 */
public class Main {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            long epochMillis = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long epochMillis1 = LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            LocalDateTime tempTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault());
            LocalDateTime tempTime1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(1644651159696L), ZoneId.systemDefault());


            String dateString = "2021-10-01T12:00:00.965Z";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);


            //Main
            String className = Main.class.getSimpleName();
            int m = -131;
            int n = -m;
            int mmm = 0;

            long l1 = 1;
            if (l1 == 1) {
                int mtt = 0;
            }
            int yy = 21;
//          String dir=  System.getProperty("user.home");

//            Log4j2 完全异步模式
//            System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");

//            1
            //8

            //主线程设置捕获子线程的异常
            Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
            {
                String msg = throwable.getMessage();
                String msg1 = throwable.toString();
                LOGGER.error("", throwable);//用此重载，打印异常
                int m10 = 0;
            });

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
//            Person person = new PersonImp();
//            person.getName();
//
//            String s = "@person.getName()";
//
//             new SetTest().test();
//            new ThreadTest().test();

//            new CalendarTest().test();
//            new ReferenceTest().test();

            //  new FileTest().test();
//            new ReflectDemo().test();
//            new EnumTest().test();

//              new ClassExecutionSequenceTest().test();
//            new ClassLoaderTest().test();


            //  new PropertiesTest().test1();


//               new LambdaTest().test();
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

//               new GenericTest().test();
//            new  GenericsErasureTest().test();

            //endregion


            //region test2019
//                new TryCatchFinallyReturnTest().test();
//              new ProxyTest().test();

//            new CglibProxy<ProxyImp>().test();

            //  new  StreamOptionalTest().test();
            // new  EqualsOperatorTest().test();
            //    new  BoxingUnboxingTest().test();


            //2
            //222
            //333
            //444
            //555
//            new NettyTest().test();

//            new NettyTest().nettyWebSocket();

//            new StringTest().test();
            //  new TimerTest().test();
//              new TryTest().test();


            //   new CharTest().test();
            //   new BlockingQueueTest().test();
            // new TypeJudgeTest().test();

//              new DivisionTest().test();
//            new HashCodeTest().test();
            //   new LeftMoveTest().test();
//              new RedissonTest().test();
//              new ConstructorTest().test();
//            new JDBCTest().test();
//            new MysqlTest().test();
//            new ReferenceDemo().test();

//            new SerializableTest().test();
//            new JwtTest().test();

//            new KafkaTest().test();
//            new SemaphoreTest().test();
//            new AtomicIntegerTest().test();
//            new AtomicStampedReferenceTest().test();
//            new AtomicMarkableReferenceTest().test();

//              new VolatileTest().test();

//            new BlockingQueueTest().delayQueueFun();
            // new ParamsTest().test();

//            new JestTest().test();
//            new ThreadLocalTest().test();
            new Log4j2Demo().test();

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

            //region 2021
//            new StringCommons().test();
//            new CollectionsCommons().test();

//            new CsvCommons().test();
//           new ThreadExceptionTest().test();
//            new ExtendImplementTest().test();
//            new FormatTest().test();

//            new BigDecimalTest().test();
//            new DefaultValueTest().test();

//            new StringJoinerTest().test();
//            new RandomTest().test();
//            new TemplateMethodPattern().test();

//            new Log4J2Test().test();
//            new CountdownLatchTest().test();
//            new CompletableFutureTest().test();
//            new LockSupportTest().test();
//            new Test.test2018.ThreadTest().test();
//            new Test.test2021.ThreadTest().test();
//            new DelayQueueTest().test();
//            new MathTest().test();

//            new PrimitiveTypeCacheTest().test();
//            new CollectionTest().test();
//            new IOStreamTest().test();
//            new HashMapSetTest().test();

//            BuilderTest builderTest=new BuilderTest.BuilderTestBuilder().age(1).name("sdsd").builder();
//            new RateLimiterTest().test();
//            new EnumJacksonTest().test();
//            new SynchronousQueueTest().test();
//            new StringBufferTest().test();
//            SingletonPattern.test();
//            new ListArrayTest().test();
//            new ArrayTest().test();
//            new OverRideVarTest().test();
//            new Test.test2022.FinalTest().test();
//            new EhcacheTest().test();

//            new CommonPoolTest().test();
//            new ThreadSafetyCollectionTest().test();
//            new AnnotationTest().test();
//            new MqttTest().test();
            //endregion

            //region 2023
//            new TimestampTest().test();
            //            new ClickHouseTest().test();
            //endregion

            //region 2023
//            new ThreadStateDemo().test();
//            new JprotobufTest().test();

            //endregion

            //region 2024
//            new DateTimeSequence().test();

            //endregion

            //region 2024
//            new HashedWheelTimerTest().test();

            //endregion


            System.out.println(MessageFormat.format("main:{0}", "completed !"));
            BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
            //阻塞主线程用
            String str = strin.readLine();

            //System.exit()或Runtime.exit()可以直接导致当前JVM进程退出

            /*
              status是非零参数，那么表示是非正常退出。 System.exit(1); System.exit(0) ;
             System.exit(0)是正常退出程序，而System.exit(1)或者说非0表示非正常退出程序。
             */
//            System.exit(1);
//            Runtime.exit(1);
//            System.exit(0) ;
            Runtime.getRuntime().exit(0);
            Integer a = 0;
        } catch (Exception ex) {
           // String str = ex.getMessage();
            System.out.println(MessageFormat.format("main :{0}", ex));
            Integer a = 0;
        }
    }
}
