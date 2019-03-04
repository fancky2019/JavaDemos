package Test.test2019;

public class StaticInnerClassTest {


    public void test() {
        StaticInnerClass staticInnerClass = new StaticInnerClass();
    }

    private Integer age = 1;
    private static Integer n = 1;

    public StaticInnerClassTest() {
        age = 2;
    }

    /*
    Java不能声明静态类：只能声明静态内部类
    静态类内可以有非静态成员
    只能访问外部类的静态成员
     */
    public static class StaticInnerClass {
        public static String Name;
        public String testStr = "test";

        static {
            n = 2;
        }

        /*
        只能访问外部类的静态成员
         */
        public static void staticFun() {
            Integer age = n;
        }

        public void fun() {
            //Non-static field 'age' cannot be referenced from a static context
            // Integer age1 = age;
            Integer m = n;
        }

    }

    /*
    内部类不能声明静态成员
    可以访问外部类的静态、非静态成员
     */
    public class InnerClass {
        public String address;

        //static String home="";
        public InnerClass() {
            age = 3;
            n = 3;
        }

        /*
      Inner classes cannot have static declarations
    */
//        public static void staticFun() {
//            Integer age = n;
//        }

        public void fun() {
            Integer age1 = age;
            Integer m = n;

        }
    }


}

class InnerClassTest {
    public void test() {
        //静态内部类实例化
        StaticInnerClassTest.StaticInnerClass staticInnerClass = new StaticInnerClassTest.StaticInnerClass();
        String testStr = staticInnerClass.testStr;
        String name = StaticInnerClassTest.StaticInnerClass.Name;

        //非静态内部类实例化
        StaticInnerClassTest staticInnerClassTest = new StaticInnerClassTest();
        StaticInnerClassTest.InnerClass innerClass = staticInnerClassTest.new InnerClass();
    }
}




