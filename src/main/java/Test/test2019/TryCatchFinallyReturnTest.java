package Test.test2019;

import java.io.Console;

public class TryCatchFinallyReturnTest {
    public void test() {
        /*
        一、finally没return:try、catch有return 执行return之后再执行finally块，try、catch内return的值不受finally语句影响
                            因为try、catch在return之后才执行finally块。此情形和C#一样。
        二、finally有return：如果finally里有return将会覆盖try、catch里的return值。C#return块内不允许有return。
        */
        System.out.printf("Get()=%d\n", get());//异常：2，正常：1。
        System.out.printf("a=%d\n", a);//2

    }

    Integer a = 1;

    Integer get() {
        Integer m = -1;
        try {
            //  Integer i = Integer.parseInt("a");
            return m = 1;
        } catch (Exception ex) {
            m = 0;
            a = 1;
            return m;
        } finally {
            a = 2;
            m = 2;
            //finally里的return会覆盖try、catch里的return值
            //  return m;
        }
    }

}
