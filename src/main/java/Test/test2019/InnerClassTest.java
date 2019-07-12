package Test.test2019;

/*
Java不能声明静态类：只能声明静态内部类
静态内部类： 静态类内可以有非静态成员，只能访问外部类的静态成员
非静态内部类：内部类不能声明静态成员， 可以访问外部类的静态、非静态成员
    */
public class InnerClassTest {
    public void test() {
        //静态内部类实例化

        OuterClass.StaticInnerClass staticInnerClass = new OuterClass.StaticInnerClass();
//       // String name = OuterClass.StaticInnerClass.Name;

        //非静态内部类实例化
        OuterClass outerClass = new OuterClass();
        OuterClass.InnerClass innerClass = outerClass.new InnerClass();

    }
}

class OuterClass {

    private Integer outClassField = 1;
    private static Integer outClassStaticField = 1;

    public OuterClass() {
        outClassField = 2;
    }

    public void OuterClassFun() {
        //只能通过子类对象访问子类的公有成员
        StaticInnerClass staticInnerClass = new StaticInnerClass();
        staticInnerClass.staticInnerClassField = "StaticInnerClassField";
        StaticInnerClass.staticInnerClassStaticField = "StaticInnerClassStaticField";

    }

    public static void OuterClassStaticFun() {
        System.out.println("OuterClassStaticFun");
    }

    /*
    Java不能声明静态类：只能声明静态内部类
    静态类内可以有非静态成员
    只能访问外部类的静态成员
     */
    public static class StaticInnerClass {
        public static String staticInnerClassStaticField;
        public String staticInnerClassField = "test";

        private String privateStaticInnerClassField = "test";

        static {
            outClassStaticField = 2;
        }

        /*
        只能访问外部类的静态成员
         */
        public static void staticInnerClassStaticFun() {
            Integer age = outClassStaticField;

            //通过外部类对象访问外部类私有共有成员。
            OuterClass outerClass = new OuterClass();
            System.out.println(outerClass.outClassField);
            outerClass.OuterClassFun();
        }

        /*
        只能访问外部类的静态成员
         */
        public void staticInnerClassFun() {
            //Non-static field 'age' cannot be referenced from a static context
            // Integer age1 = age;
            Integer m = outClassStaticField;

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
            // age = 3;
            //  n = 3;
        }

        /*
      Inner classes cannot have static declarations
      内部非静态类不能有静态
    */
//        public static void staticFun() {
//            Integer age = n;
//        }

        /*
 内部类不能声明静态成员
 可以访问外部类的静态、非静态成员
  */
        public void innerClassFun() {
            System.out.println(outClassField);
            System.out.println(outClassStaticField);
        }
    }

    public interface InnerInterface {
        void helloWorld();

    }
}

interface OuterInterface {
    void helloWorld();

    default void helloWorld1() {
        OuterInterfaceImp imp = new OuterInterfaceImp();
    }

    class OuterInterfaceImp implements OuterInterface {
        @Override
        public void helloWorld() {
            System.out.println("helloworld!");
        }
    }

}






