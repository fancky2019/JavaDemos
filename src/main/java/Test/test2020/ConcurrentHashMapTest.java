package Test.test2020;

import org.checkerframework.checker.units.qual.K;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
遍历参见：Test.test2021.CollectionTest
 */
public class ConcurrentHashMapTest {

    /*
    <<:表示左移移，不分正负数，低位补0；　相当于乘以2的次幂。
    >>:表示右移，如果该数为正，则高位补0，若为负数，则高位补1；相当于除以2的次幂。
    >>>:表示无符号右移，也叫逻辑右移，即若该数为正，则高位补0，而若该数为负数，则右移后高位同样补0

     ^：位运算，相同为0，不同为1。
     */

    /**
     thread.yield() 方法，使当前线程由执行状态，变成为就绪状态，让出cpu时间，在下一个线程执行时候，
     此线程有可能被执行，也有可能没有被执行。


     sizeCtl ：默认为0，用来控制table的初始化和扩容操作，具体应用在后续会体现出来。
                -1 代表table正在初始化
                -N 表示有N-1个线程正在进行扩容操作
                其余情况：
                1、如果table未初始化，表示table需要初始化的大小。
                2、如果table初始化完成，表示table的容量，默认是table大小的0.75倍

     */
    public void test() {
        // Hashtable采用的是数组 + 链表，当链表过长会影响查询效率，而ConcurrentHashMap采用数组 + 链表 + 红黑树，当链表长度超过8，则将链表转成红黑树，提高查询效率。

        /*
        static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        volatile V val;
        volatile Node<K,V> next;
         */

        //region HashMap
        /*
        static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;
         */
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        //hashmap 初始容量，16 阈值：16*0.75，每次添加完之后判断超过阈值扩容
        for (int i = 1; i <= 16 * 0.75; i++) {
            hashMap.put(i, 1);
        }
        //13个超过阈值，hashmap扩容
        hashMap.put(13, 1);

        hashMap.get(1);

        hashMap.keySet();
        hashMap.entrySet();

        for (Map.Entry<Integer, Integer> entry : hashMap.entrySet()) {
            entry.getValue();
            entry.getKey();
        }
        //endregion

        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
        Integer re = map.put(1, 2);//null
        //key 存在更新value


        /*
        1、table未初始化，先初始化
        2、如果table[(n - 1) & hash]==null,cas设置table[(n - 1) & hash]=val
        3、如果加入同时在扩容帮助转换（MOVED）
        4、锁住table[(n - 1) & hash]，
          如果是链表 if (fh >= 0) 。   如果key相等，更新。否则添加到链表
          注:该table表中该结点的hash值大于0，表明是链表 当转换为树之后，hash值为-2。fh = f.hash
          如果是红黑树，直接添加到红黑树
        5、 table[hashcode]的链表，如果链表长度超过8，这个位置的链表，转红黑树
            扩容2倍。 int n = tab.length； new Node<?,?>[n << 1];
         */
        re = map.put(1, 3);//2,返回之前key，对应的值

        //没有更新，插入相同的key就更新之前的key的value


        /*
        volatile V val;
        1、根据key的hashcode 找到table[]中node,
        2、根据node的key如果等于当前的key，就返回node val,反之3.
        3、如果node的key不等于当前的key，说明hash碰撞了，要遍历链表。从链表中找key等于当前key的node的val。
         */
        Integer val = map.get(1);

//        Object o=new Object();
//        o.wait();


    }

    private void funKeys() {
        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
        Integer re = map.put(1, 2);//null
        re = map.put(2, 3);

        map.forEach((m, n) ->
        {
            Object o = m;
            int p = 0;
        });
    }


}



