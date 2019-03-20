package Test.test2018;

public class ExecutionSequenceTest extends Parent {
    /*
     C>B>A
     普通块（A）：定义在方法内(不管是普通方法还是静态方法)。方法被调用时执行。
     构造块（B）：直接定义在类中，但是没有static。优先于构造方法执行，晚于静态块执行。
     静态块（C）：定义在类中，static修饰。最先执行，只执行一次。====》C#静态构造函数
     */

     /*
      字段声明没有赋值，包装类对象就为Null
      基本类型为0和String=null
      */

    /*
    访问静态方法：执行静态字段--》静态块
     */
    /*
     Main()函数是程序的入口点
     父类静态字段--》父类静态块--》子类静态字段--》子类静态块--》父类构造块、字段（按出现顺序）--》父类构造函数--》
     子类构造块、字段（按出现顺序）--》子类构造函数
     */ {
        System.out.println("构造块：类内构造方法前！");
    }

    private Integer a = 5;
    private static Integer b = 4;
    int tt;

    public ExecutionSequenceTest() {
        System.out.println("构造块：类内构造方法！");
    }

    {
        System.out.println("构造块：类内构造方法后！");
    }

    static {
        System.out.println("静态块！");
    }

    static {
        System.out.println("静态块！");
    }

    public void test() {
        {
            int m = 0;
        }
    }


    public static void testStatic() {

    }

}

class Parent {
    {
        System.out.println("Parent构造块：类内构造方法前！");
    }

    private Integer p = 5;
    private Integer pp;
    private Integer ppp;
    private static Integer b = 4;

    private String str = "abc";
    private String str1;
    private String str11;

    public Parent() {
        System.out.println("Parent构造块：类内构造方法！");
        pp = 22;
        str1 = "dssds";

    }

    {
        System.out.println("Parent构造块：类内构造方法后！");
    }

    private Integer c = 5;

    static {
        System.out.println("Parent静态块！");
    }
}
