package Test.test2021;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CollectionTest {
    public void test() {
        fun();
    }

    private void fun() {

        HashMap<String, String> hashMap = new LinkedHashMap<>();
        HashMap<String, String> hashMap1 = new HashMap<>();
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("key1", "value1");
        concurrentHashMap.put("key2", "value2");
        concurrentHashMap.put("key3", "value3");
        concurrentHashMap.get("key1");

        concurrentHashMap.forEach((k, v) ->
        {
            String key = k;
            String value = v;
            int m = 0;
        });

        //内部保存用 Map.Entry<String, String> entry
        for (Map.Entry<String, String> entry : concurrentHashMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            int m = 0;
        }

        for (String key : concurrentHashMap.keySet()) {
            int m = 0;
        }
        for (String key : concurrentHashMap.values()) {
            int m = 0;
        }

        Iterator<Map.Entry<String, String>> entries = concurrentHashMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            String key = entry.getKey();
            String value = entry.getValue();
            int m = 0;
        }
        // Collection<V> 转list
        //存的时候偶是object 取得时候根据类型强制转换。不像C#内部存储的就是T[]泛型数组
        ArrayList<String> arrayList = new ArrayList<>(concurrentHashMap.values());
        //存的时候偶是object 取得时候根据类型强制转换
        //直接通过索引获取
        String indexValue = arrayList.get(1);
        arrayList.add("value4");


        //foreach 内部不能进行被迭代集合的删除新增、修改操作。和C#一样，若写请用for循环，注意索引变化。
        arrayList.forEach(p ->
        {
            String value = p;
            int m = 0;
        });
        for (String p : arrayList) {
            int m = 0;
        }


        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("lsit1");
        linkedList.add("lsit2");
        linkedList.add("lsit3");

        //内部通过for循环，循环到下标时候终止，并获取节点
        linkedList.get(0);

        //foreach 内部不能进行被迭代集合的删除新增、修改操作。和C#一样，若写请用for循环，注意索引变化。
        linkedList.forEach(p ->
        {
            String value = p;
            int m = 0;
        });

        for (String p : linkedList) {
            int m = 0;
        }

        Enumeration<String> getHeaderNames = null;
        List<String> list = Collections.list(getHeaderNames);

    }
}
