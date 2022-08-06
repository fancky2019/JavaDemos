package Test.test2018;

//import com.sun.media.sound.EmergencySoundbank;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
泛型类型变量不能是基本数据类型，C#可以是基本数据类型
?：通配符，变量只读。不能修改
extends也称为上界通配符，就是指定上边界。即泛型中的类必须为当前类的子类或当前类。
super也称为下届通配符，就是指定下边界。即泛型中的类必须为当前类或者其父类。

泛型类型擦除:Java在编译期间，所有的泛型信息都会被擦掉.
类型擦除后保留的原始类型
泛型擦除之后类型： T-->object(没有指定上下界)、上界(如果指定上界 extends)、下界（如果指定下界 super）

 */
public class GenericsErasureTest {
    public void test() {
        fun1();
    }


    private void fun1() {
        List<String> linkedList = new LinkedList<>();
        ArrayList<String> arrayList = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        arrayList.add("123");


        try {
            ArrayList<Integer> list1 = new ArrayList<Integer>();
            list1.add(1);  //这样调用 add 方法只能存储整形，因为泛型类型的实例为 Integer
            list1.getClass().getMethod("add", Object.class).invoke(list, "asd");
            for (int i = 0; i < list.size(); i++) {
                //ArrayList 取值的时候会自动类型转换
                Object obj = list.get(i);
                if (obj instanceof Integer) {
                    int num = (int) obj;
                    int n = 0;
                } else if (obj instanceof String) {
                    String str = (String) obj;
                    int n = 0;
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        boolean result = list.getClass() == arrayList.getClass();//true:编译时候类型擦除
        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();
        ConcurrentLinkedQueue<Integer> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();
//        List<Integer>  list1=
    }

    private void fun2() {

    }
}


