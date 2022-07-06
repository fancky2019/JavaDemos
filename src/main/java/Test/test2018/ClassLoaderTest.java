package Test.test2018;

/*
类加载过程：
            加载:new、反射等类被使用时候，类被加载。
            验证：
            准备：静态变量、非静态Final赋初始值，静态Final赋值。
            解析：
            初始化：


     Main()函数是程序的入口点,如果main在加载类中，静态块会在main之前执行。
     父类静态字段、父类静态块（按出现顺序）--》子类静态字段、子类静态块-（按出现顺序）-》父类构造块、字段（按出现顺序）--》父类构造函数--》
     子类构造块、字段（按出现顺序）--》子类构造函数

             说明：静态构造执行条件---当前类的静态成员被调用、new时候执行。当前类----静态成员声明的类中。
             调用子类静态成员：父类静态块、成员（按出现顺序）-->子类静态块、成员（按出现顺序）
             调用父类静态成员：父类静态块、成员（按出现顺序）
 */
public class ClassLoaderTest {
    public void test() {


        //        ChildClass.class.getClassLoader().
        //BootstrapClassLoader：加载路径%JAVA_HOME%/lib下的jar
        // ExtensionClassLoader:加载%JRE_HOME%/lib/ext下的jar
        // AppClassLoader：加载应用程序ClassPath下的类


        /*   * AppClassLoader 加载classpath指定的路径中的类
               * ExtClassLoader 加载jre/lib/ext目录下或者java.ext.dirs系统属性定义的目录下的类
               * BootStrap  加载JRE/lib/rt.jar中的类
         */

        //双亲委派机制
        //每个类都有一个ClassLoader
        //右下道上（子类到父类）检查类是否加载，由上到下（父类到子类）加载类
        //如果ClassLoader==null  则加载该类


        /*
        输出：
        1
        说明：final变量在类加载阶段--准备阶段就加载到方法区，不会执行初始化。
             准备阶段：类变量赋值，存储在方法区，final变量初始化。
         */
        // System.out.println(ChildClass.SUPER_STATIC_FINAL_VARIABLE);
        // System.out.println(ChildClass.CHILD_STATIC_FINAL_VARIABLE);

        /*
        调用父类静态成员
        输出：
        Static_Super
        1
        说明：静态构造执行条件---当前类的静态成员被调用、new时候执行。当前类----静态成员声明的类中。
             调用子类静态成员：父类静态块、成员（按出现顺序）-->子类静态块、成员（按出现顺序）
             调用父类静态成员：父类静态块、成员（按出现顺序）
         */
        System.out.println(ChildClass.parentStaticVariable);

        /*
        调用子类静态成员
        输出：
        Static_Super
        Static_Child
        1
         */
        //  System.out.println(ChildClass.childStaticStr);
        /*
        输出：
        Static_Super
        str1
        说明：准备阶段str1=null。初始化阶段赋值str1=str2（静态块在前）str1=str1
         */
        //  System.out.println(ChildClass.str1);

        /*
         输出：
        Static_Super
        Static_Child
        Block_Super
        SuperClass
        Block_Child
        ChildClass
         */
        //   new ChildClass().getChildStr1();

        try {
            //classLoader 在class 类中定义
            //Launcher&AppClassLoader
            //当前classLoader 判断类是否加载，如果没有加载先判断父classLoader是否为空，不空就调用父类加载，
            //否则就找父类加载器直到bootstrap classLoader
            ClassLoader cl=   ClassLoaderTest.class.getClassLoader();
            if(cl==null)
            {
//                cl.getParent()
                cl.loadClass("Test.test2018.ChildClass");
            }

        } catch (Exception ex) {

        }
    }
}

class SuperClass {
    public static final int SUPER_STATIC_FINAL_VARIABLE = 1;
    public static int parentStaticVariable = 1;

    public SuperClass() {
        System.out.println("SuperClass");
    }

    static {
        System.out.println("Static_Super ");
        str1 = "str12";
    }

    {
        System.out.println("Block_Super ");
    }

    public static String str1 = "str1";
}

class ChildClass extends SuperClass {

    {
        System.out.println("Block_Child ");
    }

    static {
        System.out.println("Static_Child");
    }

    public ChildClass() {
        System.out.println("ChildClass");
    }

    public static final int CHILD_STATIC_FINAL_VARIABLE = 2;
    public static String childStaticStr = "staticChildStr1";

    private String childStr1 = "childStr1";

    public String getChildStr1() {
        return childStr1;
    }
}
