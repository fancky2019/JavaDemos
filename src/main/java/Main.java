
import Test.opensource.redis.*;
import Test.test2018.ThreadTest;
import Test.test2020.ClassCastTest;
import Test.test2019.*;
import Test.test2020.*;
import Test.test2020.UDPTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;

public class Main {

    public static void main(String[] args) {
        try {

//            //62
//            String logHeader="20200113 13:25:55:777,3511039185599 [3] - 192.168.80.22:63130 ";
//           // String logString="20200113 13:25:55:777,3511039185599 [3] - 192.168.80.22:63130 {(len=166)CancStHK@00000@00011520LC000372@8205831@@CO020990001@HKEX@@@@0011957&HKEX_1@@CO020990001@123456@@00011520LC000372@13031146@HKEX@68457.HK@1@40000@0.040000@0@@7@@P@@@@@}{(len=165)CancStHK@00000@00011520LC000476@8205831@@CO020990001@HKEX@@@@0011958&HKEX_1@@CO020990001@123456@@00011520LC000476@13031250@HKEX@52600.HK@1@5000@0.075000@0@@7@@P@@@@@}";
//           // String logString= "20200113 13:25:10:933,3510955980409 [3] - 192.168.80.22:63130 {(len=166)CancStHK@00000@00011520LC000505@8205831@@CO020990001@HKEX@@@@0011934&HKEX_1@@CO020990001@123456@@00011520LC000505@13031309@HKEX@65663.HK@1@10000@0.119000@0@@7@@P@@@@@}";
//            String logString=   "20200113 13:25:55:859,3511039339361 [3] - 192.168.80.22:63130 {(len=166)CancStHK@00000@00011520LC000435@8205831@@CO020990001@HKEX@@@@0012005&HKEX_1@@CO020990001@123456@@00011520LC000435@13031209@HKEX@68689.HK@1@10000@0.044000@0@@7@@P@@@@@}{(len=165)CancStHK@00000@00011520LC000478@8205831@@CO020990001@HKEX@@@@0012006&HKEX_1@@CO020990001@123456@@00011520LC000478@13031252@HKEX@60098.HK@1@2000@0.097000@0@@7@@P@@@@@}{(len=165)CancStHK@00000@00011520LC000398@8205831@@CO020990001@HKEX@@@@0012007&HKEX_1@@CO020990001@123456@@00011520LC000398@13031172@HKEX@64285.HK@1@1000@0.041000@0@@7@@P@@@@@}{(len=166)CancStHK@00000@00011520LC000473@8205831@@CO020990001@HKEX@@@@0012008&HKEX_1@@CO020990001@123456@@00011520LC000473@13031247@HKEX@61250.HK@1@10000@0.163000@0@@7@@P@@@@@}{(len=166)CancStHK@00000@00011520LC000371@8205831@@CO020990001@HKEX@@@@0012009&HKEX_1@@CO020990001@123456@@00011520LC000371@13031145@HKEX@56897.HK@1@50000@0.031000@0@@7@@P@@@@@}{(len=165)CancStHK@00000@00011520LC000465@8205831@@CO020990001@HKEX@@@@0012011&HKEX_1@@CO020990001@123456@@00011520LC000465@13031239@HKEX@64982.HK@1@5000@0.054000@0@@7@@P@@@@@}";
//
//           String logContent= logString.substring(62);
//           int  logContentLength=logContent.length();
//           List<String> listString=new ArrayList<>();
//           if(logContentLength<100)
//           {
//
//           }
//           else {
//
//              int firstIndex= logContent.indexOf('}');
//               logContentLength=logContent.length();
//
//              if (firstIndex==logContentLength-1)
//              {
//                  listString.add(logContent);
//              }
//              else {
//                  String currentContent="";
//                  while (firstIndex!=logContentLength-1)
//                  {
//                      currentContent=logContent.substring(0,firstIndex+1);
//                      listString.add(currentContent);
//
//                      logContent=logContent.substring(firstIndex+1);
//                      logContentLength=logContent.length();
//                      firstIndex=logContent.indexOf('}');
//                  }
//                  listString.add(logContent);
//              }
//
//              int m=0;
//           }
//
//
//
//            int logHeaderLength = logHeader.length();
//            //  tomorrow.isBefore()
//            String len = "20200113 09:00:01:059,3481435889397 [1] - 2020-01-13 09:00:01.059 Enqueue:OrdeStHK@00000@00012980LC000001@1206106@@CO020990001@HKEX@13019716@1206106@&HKEX_1@@CO020990001@123456@C@HKEX@01809.HK@1@1500@8.16@@5@@@@1@0@@@@@@@@@0";
//
//            int length = len.length();
//            int rightParenthesisIndex = len.indexOf(":",66);
//            String subStr=len.substring(66,rightParenthesisIndex);
//            System.out.println("main");
//          String dir=  System.getProperty("user.home");
//          int m=0;
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
            //  new EnumTest().test();

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

//              new RabbitMQTest().test();
            //  new  QueueTest().test();

//               new  GenericTest().test();

            //endregion

            //region test2019
            //    new TryCatchFinallyReturnTest().test();
            //  new  ProxyTest().test();


            //  new  StreamOptionalTest().test();
            // new  EqualsOperatorTest().test();
            //    new  BoxingUnboxingTest().test();

            //    new NettyTest().test();
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
              new VolatileTest().test();

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
