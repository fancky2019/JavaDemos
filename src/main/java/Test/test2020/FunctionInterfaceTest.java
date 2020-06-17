package Test.test2020;

import utility.Action;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/*
常用委托：Supplier：无入参，有返回
          Consumer：无返回，有入参
          Function：一个入参，一个返回

E - Element (在集合中使用，因为集合中存放的是元素)
T - Type（Java 类）
R - Return
K - Key（键）
V - Value（值）
N - Number（数值类型）
？ - 表示不确定的java类型

 */
public class FunctionInterfaceTest {
    public void test() {

    }

    private void functionInterfaces() {

        //泛型如果不指定类型参数，其类型就是object类型。
        //C#里必须要指定类型参数
        //无入参，有返回
        Supplier supplier = () ->
        {
            return 1;
        };
        Object re = supplier.get();

        //无返回，有入参
        Integer comParam = 1;
        Consumer consumer = (p) ->
        {

        };
        consumer.accept(comParam);

        //一个入参，一个返回
        Function<Integer, Integer> function = (p) ->
        {
            return p + 1;
        };
        function.apply(2);

        //自定义函数接口
        Action action=()->
        {

        };
        action.callBack();

    }
}
