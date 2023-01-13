package Test.test2022;


import java.util.*;
import java.util.concurrent.*;

public class ThreadSafetyCollectionTest {
    public void test() throws InterruptedException {
        fun1();
    }

    private void fun1() throws InterruptedException {
        //弱数据一致性的
        //读不加锁，写加锁，会发生索引跳跃 因为for循环只读向前，期间删除了数据就发生索引跳跃逻辑bug
        //有可能发生索引越界
        CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        //ReentrantLock 加锁
        copyOnWriteArrayList.add(1);
        //old array 的length
        int size = copyOnWriteArrayList.size();
        //volatile Object[] array;
        Integer num = copyOnWriteArrayList.get(0);
        copyOnWriteArrayList.remove(0);

        ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();


        CopyOnWriteArraySet<Integer> copyOnWriteArraySet = new CopyOnWriteArraySet<>();
        //ReentrantLock lock
        copyOnWriteArraySet.add(1);
        //transient volatile Object[] array;
        copyOnWriteArraySet.forEach(p ->
        {

        });


        List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<Integer>());
        //读写synchronized (object) synchronized
        synchronizedList.add(1);
        //synchronized (object)
        int m = synchronizedList.get(0);
        Set<Integer> synchronizedSet = Collections.synchronizedSet(new HashSet<Integer>());
        //synchronized (object)
        synchronizedSet.add(1);
        Map<String, Integer> synchronizedMap = Collections.synchronizedMap(new HashMap<>());
        //synchronized (object)
        synchronizedMap.put("1", 1);
        //synchronized (object)
        synchronizedMap.get("1");


        ArrayBlockingQueue<String> arrayBlockingQueue=new ArrayBlockingQueue<>(Integer.MAX_VALUE);
       //ReentrantLock
        arrayBlockingQueue.put("1");
        arrayBlockingQueue.take();

        LinkedBlockingDeque<String> linkedBlockingDeque=new LinkedBlockingDeque<>();
        //ReentrantLock
        linkedBlockingDeque.put("1");
        int mm = 0;

    }
}
