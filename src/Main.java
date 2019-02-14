
import Test.opensource.rabbitMQ.RabbitMQTest;
import Test.test2018.JDBCTest;
import Test.test2018.ProduceConsumerConditionTest;
import Test.test2018.ProduceConsumerTest;
import Test.test2018.SetTest;
import Test.test2019.CharTest;
import Test.test2019.Netty.NettyTest;
import Test.test2019.StringTest;
import Test.test2019.queue.BlockingQueueTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        try {

            //region 2018

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
            //   new ThreadTest().test();

//            new CalendarTest().operation();

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

            //    new FileTest().test();
            // new ReflectDemo().test();
            //  new EnumTest().test();

            //   new ExecutionSequenceTest().test();
            //   ExecutionSequenceTest.testStatic();

            //  new PropertiesTest().test1();
            //    new JDBCTest().test();

            //   new LambdaTest().test();
            // new CalendarTest().test();
            // new Java8Test().test();
//               new OptionalTest().test();
            //   new Configs().test();

            // new RedisTest().test();

            // new RedisTest().test();

            //  new ProduceConsumerTest(100).Test();
         //   new ProduceConsumerConditionTest(100).Test();

            //  new ProducerConsumerTPSTest(100,5).Test();

            //   new AnonymousInternalClassTest().test();


//            ExtendTest extendTest=new ExtendTest() ;
//            extendTest.publicFunction();
//            extendTest.publicFunction1();


            //  new ConvertTest().test();
            //  new AnnotationTest().test();

            //   new RabbitMQTest().test();
            //  new  QueueTest().test();
            //   new  ThreadTest().test();

            //endregion


            //region test2019
            //  new TryCatchFinallyReturnTest().test();
            //  new  ProxyTest().test();



           //  new  StreamOptionalTest().test();
            // new  EqualsOperatorTest().test();
            //    new  BoxingUnboxingTest().test();

       //    new NettyTest().test();
           // new StringTest().test();
          //  new TimerTest().test();
            //endregion

         //   new CharTest().test();
         //   new BlockingQueueTest().test();

            BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
            String str = strin.readLine();

            Integer a = 0;
        } catch (Exception ex) {
            String str = ex.getMessage();
            System.out.println(str);
            Integer a = 0;
        }
    }
}
