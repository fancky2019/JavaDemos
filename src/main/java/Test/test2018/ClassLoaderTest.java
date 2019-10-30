package Test.test2018;

/*
类加载过程：
            加载:
            验证：
            准备：
            解析：
            初始化：
 */
public class ClassLoaderTest {
    public void test() {
        /*
        输出：
        1
        说明：final变量在类加载阶段--准备阶段就加载到方法区，不会执行初始化。
             准备阶段：类变量赋值，存储在方法区，final变量初始化。
         */
        // System.out.println(ChildClass.STATIC_FINAL_VARIABLE);

        /*
        输出：
        SuperClass
        1
        说明：静态构造执行条件---当前类的静态成员被调用、new时候执行。当前类----静态成员声明的类中。
         */
        // System.out.println(ChildClass.staticVariable);

        /*
        输出：
        SuperClass
        str1
        说明：准备阶段str1=null。初始化阶段赋值str1=str2（静态块在前）str1=str1
         */
        //System.out.println(ChildClass.str1);

        /*
         输出：
        SuperClass
        ChildClass
         */
        new ChildClass().getChildStr1();
    }
}

class SuperClass {
    public static final int STATIC_FINAL_VARIABLE = 1;
    public static int staticVariable = 1;

    static {
        System.out.println("SuperClass");
        str1 = "str12";
    }


    public static String str1 = "str1";
}

class ChildClass extends SuperClass {
    static {
        System.out.println("ChildClass");
    }

    public static String staticChildStr1 = "staticChildStr1";

    private String childStr1 = "childStr1";

    public String getChildStr1() {
        return childStr1;
    }
}
