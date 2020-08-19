package Test.test2020;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {

    /*
    <<:表示左移移，不分正负数，低位补0；　相当于乘以2的次幂。
    >>:表示右移，如果该数为正，则高位补0，若为负数，则高位补1；相当于除以2的次幂。
    >>>:表示无符号右移，也叫逻辑右移，即若该数为正，则高位补0，而若该数为负数，则右移后高位同样补0

     ^：位运算，相同为0，不同为1。
     */

    /*
     hread.yield() 方法，使当前线程由执行状态，变成为就绪状态，让出cpu时间，在下一个线程执行时候，
     此线程有可能被执行，也有可能没有被执行。


     sizeCtl ：默认为0，用来控制table的初始化和扩容操作，具体应用在后续会体现出来。
                -1 代表table正在初始化
                -N 表示有N-1个线程正在进行扩容操作
                其余情况：
                1、如果table未初始化，表示table需要初始化的大小。
                2、如果table初始化完成，表示table的容量，默认是table大小的0.75倍

     */
    public void test() {
        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
        Integer re = map.put(1, 2);//null
        //key 存在更新value
        re = map.put(1, 3);//2
        Integer val = map.get(1);

//        Object o=new Object();
//        o.wait();


    }




}



