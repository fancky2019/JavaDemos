package Test.test2018;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/*
泛型擦除：T-->object、上界(如果指定上界)、下界（如果指定下界）
通配符?:用于类型变量。
泛型：类型参数化

泛型如果不指定类型参数，其类型就是object类型。C#里必须要指定类型参数
E - Element (在集合中使用，因为集合中存放的是元素)
T - Type（Java 类）
R - Return
K - Key（键）
V - Value（值）
N - Number（数值类型）
？ - 表示不确定的java类型

 */
public class GenericTest {

    public void test() {

        //泛型如果不指定类型参数，其类型就是object类型。
//        List list=new ArrayList();
//        list.add(1);

        GenTest<? extends Object> a = new GenTest<>();
        GenTest<? super GenS> aa = new GenTest<>();
        GenaTest<Integer> genaTest = new GenaTest<>();
        funVoid(Integer.class);
        try {
            funT(Integer.class);
        } catch (Exception ex) {

        }
        List<Integer> list = new ArrayList<>();
        //java 泛型类可不带类型变量
        GenaTest.Display();
        GenaTest.Display1();
        GenaTest genaTest1 = new GenaTest();
        genaTest1.Display2();
        GenaTest<String> genaTest2 = new GenaTest<>();
        // 泛型类型不能使primitive type
        // GenaTest<int> genaTest21 = new GenaTest<>();

        List<GenS> list1 = new ArrayList<>();
        list1.add(new GenS());
        funVoid2(list1);

        List<Integer> list2 = new ArrayList<>();
        list2.add(2);
        funVoid2(list2);
        //   funVoid2<>(List<? super T> list)

        GenTest<Integer> gt = new GenTest<>();
        //s:object
        gt.forEachRemaining1((s) ->
        {

        });
        //s:String
        gt.forEachRemaining1((String s) ->
        {

        });
        //s:Integer
        gt.forEachRemaining1((Integer s) ->
        {

        });
//        gt.forEachRemaining1(new Consumer<Object>() {
//        });
//        gt.forEachRemaining1(new Consumer<String>() {
//        });

        //报错，必须是实现Animal接口的
        //  GenaTest1<Integer>gt1=new GenaTest1<Integer>() ;
        GenaTest1<GenS1> gt1 = new GenaTest1<GenS1>();
    }


    public <T> void funVoid(Class<T> clazz) {

    }

    //注意：以下泛型方法extends和super的写法
    //extends
    public <T extends GenS> void funVoid1(T t) {

    }

    public <T> void funVoid3(List<? extends T> t) {

    }

    //泛型擦除：T-->object、上界、下界
    //funVoid3 和 funVoid2 泛型擦除后除名字不一样，都一样
    //super
    public <T> void funVoid2(List<? super T> list) {

    }


    public <T> T funT(Class<T> clazz) throws Exception {
        T t = clazz.newInstance();
        return t;
    }

    class GenTest<T> {
        public void test() {

        }

        //不是一个泛型方法
        public void forEachRemaining(Consumer<? super T> consumer) {
        }

        //不是一个泛型方法
        public void forEachRemaining1(Consumer<?> consumer) {
        }

//        public <? super T> void funVoid2(List<T> list) {
//
//        }
    }


}


class GenaTest<T> {

    static void Display() {

    }

    static <T> void Display1() {

    }

    <T> void Display2() {

    }
}

//实现接口
class GenaTest1<T extends Animal> {

}

class Gena {

    static void Display() {

    }

    static <T> void Display1(Class cl) throws Exception {
        T t = (T) cl.newInstance();
    }

    <T> void Display2() {

    }

    <T> T getT(Class classT) {
        T t = null;
        try {
            t = (T) classT.newInstance();
        } catch (Exception ex) {

        }
        return t;
    }
}


class StaticGenericTest<T> {

    //T不能在静态上下文中使用
    //   private static    StaticGenericTest<T> singleInstance=null;
    private StaticGenericTest<T> singleInstance = null;

    static {
        //singleInstance = new StaticGenericTest<>();
    }

    StaticGenericTest() {
        singleInstance = new StaticGenericTest<>();
    }


    //T不能在静态上下文中使用
//    private static void  Fun(T t)
//    {
//
//    }

//    T不能在静态上下文中使用
////    private static void Fun() {
////        T t = singleInstance.getClass().newInstance();
////    }

    private void Fun(Class<T> cl) throws Exception {
        T t = (T) cl.newInstance();
    }
}

class GenS {

}

class GenS1 extends GenS implements Animal {
    @Override
    public Integer getAge() {
        return null;
    }
}