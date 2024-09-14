package Test.test2019;

import Model.Student;
import Test.test2018.Person;

import java.io.Console;

public class TryCatchFinallyReturnTest {
    public void test() throws Exception {
        /*
        一、finally没return:try、catch有return 执行return之后再执行finally块，try、catch内return的值如果
                                 是基本数据类型不受finally语句影响，如果是引用类型将改变暂存值。
        二、finally有return：如果finally里有return将会覆盖try、catch里的return值。C#return块内不允许有return。
        */

        //catch 可不要
//        try {
//
//        } finally {
//
//            //finally里的return会覆盖try、catch里的return值
//            // return m;
//        }


        try {
            int m = Integer.parseInt("ds");
        } catch (Exception ex) {
            throw new Exception(ex);
//            throw new Exception(ex.getMessage());
            //finally里的return会覆盖try、catch里的return值
            // return m;
        }

        throwTest();
        globalStudent.setName("global");
        System.out.printf("Get()=%d\n", get());//异常：2，正常：1。
        System.out.printf("a=%d\n", a);//2

        String str = getString();
        System.out.println(str);

        Student student = getStudent();
        System.out.println(student.getName());


    }

    Integer a = 1;

    Student globalStudent = new Student();

    Integer get() {
        Integer m = -1;
        try {
//            Integer i = Integer.parseInt("a");
            globalStudent.setName("try");
            m = 1;
            //代码执行到return 语句，会将return值暂存，然后去执行finally语句，
            // 如果finally语句有返回，就覆盖掉暂存的值了。
            //如果finally语句无返回，暂存的值:  基本数据类型：不受影响。
            //                                  引用类型：受影响。
            return m;
        } catch (Exception ex) {
            globalStudent.setName("catch");
            m = 0;
            a = 1;
            return m;
        } finally {
            globalStudent.setName("finally");
            a = 2;
            m = 2;
            //finally里的return会覆盖try、catch里的return值
            // return m;
        }
    }


    /*
    finally 语句不会改变return 语句中暂存的基本数据类型类型的返回值。
     */
    String getString() {
        String str = "";
        try {
            str = "try";
            return str;
        } catch (Exception ex) {
            str = "catch";
            return str;
        } finally {
            str = "finally";
        }


    }


    /*
    finally语句会改变引用类型的暂存的返回值。
     */
    Student getStudent() {
        Student student = new Student();
        try {
            student.setName("try");
            return student;
        } catch (Exception ex) {
            student.setName("catch");
            return student;
        } finally {
            student.setName("finally");
//            return student;
        }

    }

    int throwTest() {

        try {
            int m = Integer.parseInt("m");
            return m;
        } catch (Exception ex) {
            System.out.println("catch exception");
            throw ex;
        } finally {
            System.out.println("finally when catch throw");
//            return student;
        }

    }

}
