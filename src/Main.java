import Test.*;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class Main {

    public static void main(String[] args) {
        try {


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

            //    new FileTest().test();
            // new ReflectDemo().test();
            //  new EnumTest().test();

            //   new ExecutionSequenceTest().test();
            //   ExecutionSequenceTest.testStatic();

            //  new PropertiesTest().test1();
            //    new JDBCTest().test();

            //    new LambdaTest().test();

            // new Java8Test().test();
            new OptionalTest().tetst();

            Integer a = 0;
        } catch (Exception ex) {
            String str = ex.getMessage();
            Integer a = 0;
        }
    }
}
