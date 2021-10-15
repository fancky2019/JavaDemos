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


自动拆箱、装箱： 装箱// Integer.valueOf(126); //拆箱：intValue();

装箱类型和基本数据类型比较：自动拆箱与基本类型比较
== 比较时: 基础类型和包装类型会进行自动拆箱，包装类型和包装类型之间是正常类型的比较。
equals 方法，参数为基础类型时会进行自动装箱。装箱后变成取 intValue 后基础类型值的比较。
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

        //调用 valueOf 自动装箱
        Integer integer1 = 126;//实际调用valueOf源码从缓存里取  自动装箱
        Integer integer2 = Integer.valueOf(126);//valueOf 源码从缓存里取
        Integer integer22 = Integer.valueOf(126);//valueOf 源码从缓存里取

        Integer integer3 = new Integer(126);//new  每次回重新生成一个对象
        Integer integer4 = new Integer(126);//new  每次回重新生成一个对象

        //Integer 类重写了equals
        boolean result1 = integer1.equals(integer2); //true
        boolean result2 = integer2.equals(integer3);//true
        boolean result3 = integer3.equals(integer4);//true

        boolean result11 = integer1 == integer2; //true
        boolean result21 = integer2 == integer3; //false
        boolean result31 = integer3 == integer4;  //false


        Short short1 = Short.valueOf((short) 3);//valueOf 源码从缓存里取


        //拆箱比值：intValue();
        Integer num1 = 100;
        boolean re1 = num1 == 100;  //自动拆箱:调用intValue()比较值

        Integer num2 = 130;
        boolean re2 = num2 == 130;

        Integer num22=new Integer(130);
        Integer num222=new Integer(130);
        Boolean com=num22==num222;//false


        boolean re22 = num22 == 130;
        Integer num3=new Integer(230);
        Integer num33=new Integer(230);
        Boolean com1=num3==num33;//false

        Boolean isEnable=null;

        //自动拆箱：相当于 isEnable.booleanValue()
        if(isEnable)
        {

        }
       //相当于 isEnable.booleanValue() 。如果isEnable为null自动拆箱就会报NullPointerException
        if(isEnable.booleanValue())
        {

        }


        boolean bo=isEnable;



//        Boolean.valueOf(false);
//        Float.valueOf(1f);
//        Character.valueOf('c');
        int m = 0;
    }

    private void fun1() {
        //和C#ArrayList一样存储object类型
        ArrayList list1 = new ArrayList();
        list1.add(1);
        List<Integer> list2 = new LinkedList<Integer>();
        //Arrays.asList
        // StampedLock

        ReentrantLock lock = new ReentrantLock();
    }
}
