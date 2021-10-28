package Test.test2021;

import mssql.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;

/*
HashSet:无序性、不重复
LinkedHashSet:有序

HashMap:
LinkedHashMap:
ConcurrentHashMap:
ConcurrentLinkedHashMap:
 */
public class HashMapSetTest {
    public void  test()
    {
        fun();
    }

    private void fun()
    {
       // ConcurrentLinkedQueue
      //  LinkedBlockingQueue

        //无序:不是按照添加进去的顺序存储。
        HashSet<Integer> hashSet=new HashSet<>();
        hashSet.add(2);
        hashSet.add(1);
        hashSet.add(3);
        hashSet.add(1);//重复没有添加进去


        //无序:不是按照添加进去的顺序存储。
        //底层使用红黑树：有序
        TreeSet<Integer> treeSet=new TreeSet<>();
        treeSet.add(2);
        treeSet.add(1);
        treeSet.add(3);
        treeSet.add(1);//重复没有添加进去


        //无序:不是按照添加进去的顺序存储。
        //无序，不重复. 后面添加相同的key会覆盖之前的值。
        HashMap<Integer,String> hashMap=new HashMap<>();
        hashMap.put(2,"2");
        hashMap.put(1,"1");
        hashMap.put(3,"3");
        hashMap.put(1,"1");
        hashMap.put(3,"1");


        //无序:不是按照添加进去的顺序存储。
        ConcurrentSkipListSet<Integer> concurrentSkipListSet=new ConcurrentSkipListSet<>();
        concurrentSkipListSet.add(2);
        concurrentSkipListSet.add(1);
        concurrentSkipListSet.add(3);
        concurrentSkipListSet.add(1);//重复没有添加进去

        //无序:不是按照添加进去的顺序存储。
        //无序，不重复. 后面添加相同的key会覆盖之前的值。
        ConcurrentHashMap<Integer,String> concurrentHashMap=new ConcurrentHashMap<>();
        concurrentHashMap.put(2,"2");
        concurrentHashMap.put(1,"1");
        concurrentHashMap.put(3,"3");
        concurrentHashMap.put(1,"1");
        concurrentHashMap.put(3,"1");


        int m=0;
    }
}
