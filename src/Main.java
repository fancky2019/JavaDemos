
import Test.*;
import Test.rabbitMQ.RabbitMQTest;
import Test.reflection.AnnotationTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        try {

            //获取包名
            String packageName = ImpTest.class.getPackage().getName();
//        System.out.println("Hello World!");
//        Person person=new PersonImp();
//        person.getName();
//
            //  new SetTest().test();
//
//
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

            //    new LambdaTest().test();
            // new CalendarTest().test();
            // new Java8Test().test();
//               new OptionalTest().test();
            //   new Configs().test();

            // new RedisTest().test();

            // new RedisTest().test();


            //  new ProduceConsumerTest(100).Test();
            //  new ProducerConsumerTPSTest(100,5).Test();

            //   new AnonymousInternalClassTest().test();


//            ExtendTest extendTest=new ExtendTest() ;
//            extendTest.publicFunction();
//            extendTest.publicFunction1();


             new ConvertTest().test();
            //  new AnnotationTest().test();

           // new RabbitMQTest().test();
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
