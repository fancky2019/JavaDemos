package Test.test2018;

import Model.Student;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * lambda 表达式的执行原理是会构建一个内部类，其中表达式中用到的外部变量，都会通过内部类的构造函数，作为参数引入。
 * Lambda 表达式引用的局部变量必须是 final 或者是等同 final 效果的即lambda内不会修改变量值
 *
 *
 * lambda 不能引用外部原始类型数据，可以引用引用类型数据
 * @author cc
 */
public class LambdaTest {
    public void test() {
        finalFun(1, 2);
        //自定义函数接口
        FuncClass funcClass = new FuncClass();
        funcClass.testFuncInterface(p ->
        {
            System.out.println(p);
        });
        funcClass.testFuncInterface(p -> System.out.println(p));
        systemFunctionalInterface();

        //匿名方法赋值给函数接口，像是C#直接将匿名方法赋值给委托
        Consumer<String> consumer = p -> System.out.println(p);
        //函数接口调用方法，C#里委托是方法，就直接调用,不用掉方法
        consumer.accept("Consumer");
        Predicate<Boolean> predicate = p -> !p;
        Boolean re = predicate.test(false);
        Function<String, Integer> function = p -> Integer.parseInt(p);
        Integer num = function.apply("1");
    }

    private void finalFun(final int x, int y) {
//        x=2; final 修改的参数不能修改值。
        y = 10;
        //一个非 final 的局部变量或方法参数，其值在初始化后就从未更改，那么该变量就是 effectively final
        //如果参数值在lambda表达式中未修改，其等效final，可以不用生命final
        int m = 0;
        //lambda表达式中引用外部参数，其不能修改外部参数。要求参数式final或effectively final
        int n = 0;
        final int p = 0;
        //对象不用生命诚final,lambda内可以修改对象属性值,但是不能new指向新的地址
        Student student = new Student("fancky", 27);
        Student student1 = new Student("fancky1", 27);
        System.out.println("init student's age:" + student.getAge());
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() ->
        {
            System.out.println("m=" + m);
            //   n+=1;  //lambda表达式中引用外部参数，其不能修改外部参数。要求参数式final或effectively final
            int age = student.getAge();
            student.setAge(28);
            System.out.println("lambda changes student's age:" + student.getAge());
            //lambda内可以修改对象属性值,但是不能new
//            student1 = new Student("fancky1", 27);
            int ob = 0;
        });
        try {
            completableFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("after lambda student's age:" + student.getAge());
        int mmm = 0;

    }

    /**
     * 系统自带的函数接口
     */
    private void systemFunctionalInterface() {
        //Consumer<T>：一个参数没有返回值的
        consumerFunctionalInterface((String str) ->
        {
            System.out.println(str);
        });
        // Function<T, R>:一个入参一个返回值
        //  Function<String,Integer>
        functionFunctionalInterface(p ->
        {
            return Integer.parseInt(p) + 2;
        });
        // Predicate<T>:一个参数返回boolean
        predicateFunctionalInterface(b ->
        {
            boolean b1 = !b;
            int m = 0;
            return b1;
        });
    }

    private void consumerFunctionalInterface(Consumer<String> consumer) {
        consumer.accept("ConsumerFunctionalInterface");
    }

    private void predicateFunctionalInterface(Predicate<Boolean> predicate) {
        boolean bo = predicate.test(false);
        int m = 0;
    }

    private void functionFunctionalInterface(Function<String, Integer> function) {
        int re = function.apply("1");
        int m = 0;
    }

    private int returnInt(Predicate<Integer> predicate) {
        return 1;
    }
}


class FuncClass {
    public void testFuncInterface(FuncInterface funcInterface) {
        funcInterface.funcInterfaceTest("hello world!");
        //default 方法通过接口对象调用
        funcInterface.display();
        FuncInterface.display1();
    }
}
