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
== 比较时: 基础类型和包装类型会进行自动拆箱，包装类型和包装类型之间是引用类型的比较--是否是同一个对象
equals():参数为基础类型时会进行自动装箱。装箱后变成取 intValue 后基础类型值的比较。
         具体参见包装类的equals()方法：如果参数为空，类型判断返回false，否则转成obj-->强转Integer-->拆箱获取intValue。
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

        Integer num22 = new Integer(130);
        Integer num222 = new Integer(130);
        Boolean com = num22 == num222;//false


        boolean re22 = num22 == 130;
        Integer num3 = new Integer(230);
        Integer num33 = new Integer(230);
        Boolean com1 = num3 == num33;//false

        Boolean isEnable = false;

        //拆箱比较值
        //自动拆箱：相当于 isEnable.booleanValue()
        if (isEnable) {
            int m = 0;
        }
//        //相当于 isEnable.booleanValue() 。如果isEnable为null自动拆箱就会报NullPointerException
        if (isEnable.booleanValue()) {

        }

        Boolean isUsed = null;
        Boolean isUsed1 = false;
        Boolean isUsed2 = false;
        Boolean isUsed3 = new Boolean(false);
        Boolean rr1=isUsed1.equals(isUsed);//false
        Boolean rr2=isUsed1.equals(isUsed2);//true
        Boolean rr3=isUsed1==isUsed2;//true
        Boolean rr4=isUsed1==isUsed3;//false
        try {
            Boolean rr5=isUsed==false;//报异常，装箱类型与基础类型比较：拆箱取值比较
        }
        catch (Exception ex)
        {

        }

        Boolean rr6=isUsed1.equals(false);//报异常，装箱类型与基础类型比较：拆箱取值比较
        boolean bo = isEnable;

      Boolean ret=  isEnable.booleanValue();
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
