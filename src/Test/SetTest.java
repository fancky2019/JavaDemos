package Test;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class SetTest {

    public void test() {
        operation();
    }

    private void operation() {
        List<Integer> listInt = Arrays.asList(1, 2, 3, 8, 6, 5);

        Integer sum = listInt.stream().reduce(0, Integer::sum);
        Integer sum1 = listInt.stream().reduce(Integer::sum).get();
        Integer min = listInt.stream().reduce(Integer.MAX_VALUE, Integer::min);
        Integer max = listInt.stream().reduce(Integer.MIN_VALUE, Integer::max);
        //查找
        listInt.stream().filter(p -> p == 2).count();
        List<Integer> re = listInt.stream().filter(p -> p >= 2).collect(Collectors.toList());
        listInt.sort((m, n) ->
        {
            if (m > n) {
                return 1;
            } else if (m == n) {
                return 0;
            } else {
                //要返回-1，返回0不排序
                return -1;
            }
        });
        Integer m = 0;
    }

    public void declare() {

        //region hashMap <Key,Value> --->c# Dictionary
        //hashMap <Key,Value>
        Map<String, Integer> map = new HashMap<String, Integer>();

        map.put("key1", 1);
        map.put("key3", 3);
        Map<String, Integer> map1 = new HashMap<String, Integer>();
        map1.put("key1", 2);
        map1.put("key2", 2);
        //Key存在一样的，值会被更新
        map.putAll(map1);
        //遍历Key
        for (String m : map.keySet()) {

        }
        //遍历Value
        for (Integer i : map.values()) {
        }
        //endregion

        //region List--->C# List
        //list
        List<Integer> list = new ArrayList<Integer>();
        list.add(9);
        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(2);
        list1.add(3);
        list1.add(5);
        //all 相当于C#里的Range
        list.addAll(list1);
        for (Integer i : list) {
            Integer m = i;
        }
        list1.forEach(p ->
        {
            System.out.println(p);
        });
        Iterator<Integer> it = list1.iterator();
        while (it.hasNext()) {
            Integer a = it.next();
        }
        //endregion

        //region Vector --->C#ArrayList
        Vector vector = new Vector();
        vector.add(1);
        vector.add("sd");
        for (Object o : vector) {
            String s = o.toString();
        }
        //endregion

        //region HashSet
        HashSet<Integer> hashSet = new HashSet<Integer>();
        hashSet.add(2);
        hashSet.add(1);
        hashSet.add(3);
        // hashSet.remove(1);

        Iterator<Integer> hsIterator = hashSet.iterator();
        while (hsIterator.hasNext()) {
            Integer str = hsIterator.next();
            Integer n = 0;
        }
        //endregion

        //region TreeSet
        TreeSet<Integer> treeSet = new TreeSet<Integer>();
        treeSet.add(3);
        treeSet.add(2);
        treeSet.add(5);
        treeSet.remove(1);
        //endregion

        //region LinkedHashSet
        LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet<Integer>();
        linkedHashSet.add(2);
        linkedHashSet.add(1);
        linkedHashSet.add(3);
        linkedHashSet.remove(1);
        Iterator<Integer> ll = linkedHashSet.iterator();
        while (ll.hasNext()) {
            Integer str = ll.next();
        }
        //endregion

    }

}
