package Test.test2018;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class LambdaTest {
    public void test() {
        //自定义函数接口
        FuncClass funcClass = new FuncClass();
        funcClass.testFuncInterface(p ->
        {
            System.out.println(p);
        });
        funcClass.testFuncInterface(p -> System.out.println(p));
        sysytemFunctionalInterface();

        //匿名方法赋值给函数接口，像是C#直接将匿名方法赋值给委托
        Consumer<String> consumer = p -> System.out.println(p);
        //函数接口调用方法，C#里委托是方法，就直接调用,不用掉方法
        consumer.accept("Consumer");
        Predicate<Boolean> predicate = p -> !p;
        Boolean re = predicate.test(false);
        Function<String, Integer> function = p -> Integer.parseInt(p);
        Integer num = function.apply("1");
    }

    /**
     * 系统自带的函数接口
     */
    private void sysytemFunctionalInterface() {
        //Consumer<T>：一个参数没有返回值的
        ConsumerFunctionalInterface((String str) ->
        {
            System.out.println(str);
        });
        // Function<T, R>:一个入参一个返回值
        //  Function<String,Integer>
        FunctionFunctionalInterface(p ->
        {
            return Integer.parseInt(p) + 2;
        });
        // Predicate<T>:一个参数返回boolean
        PredicateFunctionalInterface(b ->
        {
            Boolean b1 = !b;
            Integer m = 0;
            return b1;
        });
    }

    private void ConsumerFunctionalInterface(Consumer<String> consumer) {
        consumer.accept("ConsumerFunctionalInterface");
    }

    private void PredicateFunctionalInterface(Predicate<Boolean> predicate) {
        Boolean bo = predicate.test(false);
        Integer m = 0;
    }

    private void FunctionFunctionalInterface(Function<String, Integer> function) {
        Integer re = function.apply("1");
        Integer m = 0;
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
