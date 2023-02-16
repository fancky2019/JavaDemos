
import Test.opensource.Jackson.JacksonTest;
import Test.opensource.Netty.NettyTest;
import Test.opensource.jwt.JwtTest;
import Test.opensource.msgpack.MsgPackTest;
import Test.opensource.rabbitMQ.RabbitMQTest;
import Test.opensource.redis.RedisTest;
import Test.opensource.zookeeper.ZooKeeperTest;
import Test.test2018.*;
import Test.opensource.commonspool.CommonPoolTest;
import Test.opensource.encache.EhcacheTest;
import Test.test2019.*;
import Test.test2021.CollectionTest;
import Test.test2021.designpattern.SingletonPattern;
import Test.test2022.ThreadSafetyCollectionTest;
import Test.test2021.extendImplement.ExtendImplementTest;
import Test.test2022.ArrayTest;
import Test.test2022.ListArrayTest;
import Test.test2022.OverRideVarTest;
import Test.test2022.annotation.AnnotationTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;


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
//        Person person=new PersonImp();
//        person.getName();
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
            //    new TryCatchFinallyReturnTest().test();
//              new ProxyTest().test();

//            new CglibProxy<ProxyImp>().test();

            //  new  StreamOptionalTest().test();
            // new  EqualsOperatorTest().test();
            //    new  BoxingUnboxingTest().test();

//            new NettyTest().test();

//            new NettyTest().nettyWebSocket();

            new StringTest().test();
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
//            new RandomTest().test();

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
            String str = ex.getMessage();
            System.out.println(MessageFormat.format("main :{0}", str));
            Integer a = 0;
        }
    }
}
