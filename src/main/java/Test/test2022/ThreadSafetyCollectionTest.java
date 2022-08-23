package Test.test2022;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.checkerframework.checker.units.qual.K;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class ThreadSafetyCollectionTest {
    public void test() {
        fun1();
    }

    private void fun1() {
        CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        //ReentrantLock 加锁
        copyOnWriteArrayList.add(1);
        //volatile Object[] array;
        Integer num = copyOnWriteArrayList.get(0);

        ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();


        CopyOnWriteArraySet<Integer> copyOnWriteArraySet = new CopyOnWriteArraySet<>();
        //ReentrantLock lock
        copyOnWriteArraySet.add(1);
        //transient volatile Object[] array;
        copyOnWriteArraySet.forEach(p ->
        {

        });


        List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<Integer>());
        //synchronized (object) synchronized
        synchronizedList.add(1);
        //synchronized (object)
        int m = synchronizedList.get(0);
        Set<Integer> synchronizedSet = Collections.synchronizedSet(new HashSet<Integer>());
        //synchronized (object)
        synchronizedSet.add(1);
        Map<String, Integer> synchronizedMap = Collections.synchronizedMap(new HashMap<>());
        //synchronized (object)
        synchronizedMap.put("1",1);
        //synchronized (object)
        synchronizedMap.get("1");

        int mm = 0;

    }
}
