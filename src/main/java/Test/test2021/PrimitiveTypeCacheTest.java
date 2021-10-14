package Test.test2021;

import scala.Int;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;

/*
[-128,127]
 primitive 类型数据
整型：byte, short, int, long
字符型：char
浮点型：float, double。此两种不会缓存
布尔型：boolean

 */
public class PrimitiveTypeCacheTest {

    public void test() {
        fun();
    }

    /*
    valueOf 源码从缓存里取
     */
    private void fun() {
        //方法内一般用primitive类型
        Integer integer1 = 126;//实际调用valueOf源码从缓存里取
        Integer integer2 = Integer.valueOf(126);//valueOf 源码从缓存里取
        Integer integer22 = Integer.valueOf(126);//valueOf 源码从缓存里取

        Integer integer3 = new Integer(126);//new  每次回重新生成一个对象
        Integer integer4 = new Integer(126);//new  每次回重新生成一个对象

        //Integer 类重写了equals
        boolean result1 = integer1.equals(integer2); //true
        boolean result2 = integer2.equals(integer3);//true
        boolean result3 = integer3.equals(integer4);//true

        boolean result11 = integer1 == integer2; //true
        boolean result21 = integer2 == integer3; //true
        boolean result31 = integer3 == integer4;  //


        Short short1 = Short.valueOf((short) 3);//valueOf 源码从缓存里取
        int m = 0;
    }

    private  void fun1()
    {
        //和C#ArrayList一样存储object类型
        ArrayList list1=new ArrayList();
        list1.add(1);
        List<Integer> list2=new LinkedList<Integer>();
        //Arrays.asList
       // StampedLock

        ReentrantLock lock=new ReentrantLock();
    }
}
