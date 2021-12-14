package Test.test2018;
//Lambda表达式就是函数式接口（FunctionalInterface）实现的快捷方式，它相当于函数式接口实现的实例，
//因为在方法中可以使用Object作为参数，所以把Lambda表达式作为方法的参数也是可以的。
//函数式接口只有一个抽象方法，并且没有重写Object类中的方法(Object类中的public的方法除外)，可以有默认方法和静态方法。
//函数式接口一般用注解@FunctionalInterface标注。
//该注解可有可无

import javax.management.loading.PrivateClassLoader;

@FunctionalInterface
public interface FuncInterface {

    //接口成员字段访问修饰符默认：public static final
    static String name = "";
    final Integer age = 10;
    static final String address = "";

    void funcInterfaceTest(String str);

    //default类成员访问修饰符：本包内,默认访问修饰符

    /**
     * default修饰必须有方法体，实现类可以修改
     * default 方法通过接口对象调用
     */
    default void display() {
        System.out.println("  default public  void  display()");
    }

    /**
     * 可以有静态方法
     * 通过接口调用，类似静态类
     */
    static void display1() {
        System.out.println("  default public  void  display()");
    }

    static String staticFun() {
        return "ddd";
    }

    //Java 9
//    private void fun()
//    {
//
//    }

}
