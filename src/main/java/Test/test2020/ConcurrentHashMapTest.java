package Test.test2020;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {
    public void test() {
        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
        map.put(1, 2);
        Integer val = map.get(1);
    }
}
