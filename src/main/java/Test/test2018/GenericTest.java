package Test.test2018;

import org.checkerframework.checker.units.qual.K;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
泛型类型变量不能是基本数据类型，C#可以是基本数据类型


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



extends也称为上界通配符，就是指定上边界。即泛型中的类必须为当前类的子类或当前类。
super也称为下届通配符，就是指定下边界。即泛型中的类必须为当前类或者其父类。

注：泛型方法： 参数类型的位置：C#放在方法后<T>,java放在访问修饰符后，返回类型前。  public <T> T funT(Class<T> clazz)、
  public <T> void funT(Class<T> clazz)、
  public static <T, U extends Comparable<? super U>> Comparator<T> comparing( Function<? super T, ? extends U> keyExtractor)
   泛型类：和C#一样在类名后<T>


 //指定泛型类型
 List<?> listyyy11 = GenaTest.<GenS>funVoid21112(new GenS());
 */
public class GenericTest {

    public void test() {

        // 如果定义了泛型，实例化时没有指明类的泛型，则认为此泛型类型为Object
        List list11 = new LinkedList<>();
        list11.add(123);
        list11.add("123");

        //由于没有指定类型参数，泛型类型为Object
        int e1 = (Integer) list11.get(0);
        String str1 = list11.get(1).toString();
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

        GenaTest.<String>Display1("sd");
        //类型参数可以省略
        GenaTest.Display1("sd");
        //指定泛型类型
        List<?> listyyy11 = GenaTest.<GenS>funVoid21112(new GenS());

        List<?> listyyy = GenaTest.<GenS>funVoid21112(new GenS());
        listyyy = GenaTest.funVoid21112(new GenS());

        List<GenS> listGens = GenericTest.this.funVoid21112(new GenS());

        GenaTest genaTest1 = new GenaTest();
//        genaTest1.Display2();
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

    //注意1,2集合泛型和非集合泛型的约束写法
    //1：飞机和泛型
    //注意：以下泛型方法extends和super的写法
    //extends
    public <T extends GenS> void funVoid1(T t) {

    }

    //错误
//    public <T> void funVoid11(<T extends GenS> t) {
//    }

    //2：集合泛型
    public <T> void funVoid3(List<? extends T> t) {

    }

    public <T extends GenS> void funVoid31(List<T> t) {

    }

    public <T> void funVoid311(List<? extends GenS> t) {

    }

    //集成某个类的子类
    private <T> void setLine(List<? extends GenS> t) {

    }

    /**
     * ?:约束写在参数内
     * T约束写在方法前
     */

    //泛型擦除：T-->object、上界、下界
    //funVoid3 和 funVoid2 泛型擦除后除名字不一样，都一样
    //super
    public <T> void funVoid2(List<? super T> list) {

    }

    public <T> void funVoid21(List<? extends T> list) {

    }

    public <T extends GenS> void funVoid211(List<T> list) {

    }

    public <T extends GenS> List<T> funVoid21112(T t) {
        return null;
    }

    public <T> void funVoid2111(List<? super GenS> list) {

    }

    public <T> void funVoid2112(List<T> list) {

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

    //静态方法必须要设置 参数类型
    static <T> void Display1(T t) {

    }

    //报错 静态方法必须要设置 参数类型 cannot be referenced from a static context
//    static  void Display112(T t) {
//
//    }

    void Display1121(T t) {

    }

    //和类的类型占位符不一样，就要设置泛型类型声明。
    static <E> void Display11(E e) {

    }


    public static <T extends GenS> List<T> funVoid21112(T t) {
        return null;
    }
}

//实现接口
class GenaTest1<T extends Animal> {

}

class Gena {

    static void Display() {

    }

    static <T> void Display1(Class<T> cl) throws Exception {
        T t = (T) cl.newInstance();
    }

    <T> void Display2() {

    }

    public static <T> T getT(Class<T> classT) {
        T t = null;
        try {
            t = (T) classT.newInstance();
        } catch (Exception ex) {

        }
        return t;
    }

    public static <T> void getT(T str) {

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

    private <T> void fun(Class<K> cl) throws Exception {
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