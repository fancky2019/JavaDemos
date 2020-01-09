package Test.test2018;

/*
类加载过程：
            加载:
            验证：
            准备：
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
            //Launcher&AppClassLoader
//            ClassLoader cl=   ClassLoaderTest.class.getClassLoader();
//            if(cl==null)
//            {
//                cl.loadClass("Test.test2018.ChildClass");
//            }

        } catch (Exception ex) {

        }
    }
}

class SuperClass {
    public static final int SUPER_STATIC_FINAL_VARIABLE = 1;
    public static int parentStaticVariable = 1;

   public SuperClass()
   {
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
    public  ChildClass()
    {
        System.out.println("ChildClass");
    }
    public static final int CHILD_STATIC_FINAL_VARIABLE = 2;
    public static String childStaticStr = "staticChildStr1";

    private String childStr1 = "childStr1";

    public String getChildStr1() {
        return childStr1;
    }
}
