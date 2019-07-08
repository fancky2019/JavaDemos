
import Test.opensource.rabbitMQ.RabbitMQTest;
import Test.opensource.redis.RedisTest;
import Test.opensource.redis.RedissonTest;
import Test.test2018.*;
import Test.test2019.*;
import common.Configs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;

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

            //  new FileTest().test();
            // new ReflectDemo().test();
            //  new EnumTest().test();

//              new ClassExecutionSequenceTest().test();


            //  new PropertiesTest().test1();


            //   new LambdaTest().test();
//             new CalendarTest().test();
            // new Java8Test().test();
//               new OptionalTest().test();
            //   new Configs().test();

            //  new RedisTest().test();

            //    new RedisTest().test();

            //  new ProduceConsumerTest(100).Test();
            //   new ProduceConsumerConditionTest(100).Test();

            //  new ProducerConsumerTPSTest(100,5).Test();

            //   new AnonymousInternalClassTest().test();


//            ExtendTest extendTest=new ExtendTest() ;
//            extendTest.publicFunction();
//            extendTest.publicFunction1();


//              new ConvertTest().test();
            //  new AnnotationTest().test();

            //  new RabbitMQTest().test();
            //  new  QueueTest().test();

//               new  GenericTest().test();

            //endregion


            //region test2019
            //  new TryCatchFinallyReturnTest().test();
            //  new  ProxyTest().test();


            //  new  StreamOptionalTest().test();
            // new  EqualsOperatorTest().test();
            //    new  BoxingUnboxingTest().test();

            //    new NettyTest().test();
//            new StringTest().test();
            //  new TimerTest().test();
            //endregion

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
            new ReferenceDemo().test();

            BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
            String str = strin.readLine();

            Integer a = 0;
        } catch (Exception ex) {
            String str = ex.getMessage();
            System.out.println( MessageFormat.format("main :{0}",str));
            Integer a = 0;
        }
    }
}
