package Test.test2018;

import Model.Student;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class SetTest {

    /*
add/offer/put的区别：
1、 add: 和collection的add一样，当队列满时，调用add()方法抛出异常IllegalStateException异常
2、 调用off()方法当队列满时返回的 false。
3、调用put方法时候，当当前队列等于设置的最大长度时，将阻塞，直到能够有空间插入元素

remove/poll/take的区别
1、remove，remove()是从队列中删除第一个元素。remove() 的行为与 Collection 接口的相似
2、poll，但是新的 poll() 方法时有值时返回值，在空集合调用时不是抛出异常，只是返回 null,
3、take: 获取并移除此队列的头部，在元素变得可用之前一直等待 。queue的长度 == 0 的时候，一直阻塞

peek，element区别：
检测操作，element() 和 peek() 用于在队列的头部查询元素。与 remove() 方法类似，在队列为空时， element() 抛出一个IllegalStateException异常，
而 peek() 返回 null，Queue.java源码

 */

    public void test() {
        operation();
        sets();
        //  getListObjectProperty();
    }

    private void operation() {
        List<Integer> listInt = Arrays.asList(1, 2, 3, 8, 6, 5);
        //反转
        Collections.reverse(listInt);
        Integer sum = listInt.stream().reduce(0, Integer::sum);
        Integer sum1 = listInt.stream().reduce(Integer::sum).get();
        Integer min = listInt.stream().reduce(Integer.MAX_VALUE, Integer::min);
        Integer max = listInt.stream().reduce(Integer.MIN_VALUE, Integer::max);
        //查找
        long l = listInt.stream().filter(p -> p == 2).count();
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

    private void sets() {

        //region hashMap <Key,Value> --->c# Dictionary
        //hashMap <Key,Value>
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("key1", 1);
        //如果添加了重复的Key,后面添加的KeyValue会更新之前添加的
        map.put("key1", 2);//Value=2
        map.put("key3", 3);
        Map<String, Integer> map1 = new HashMap<String, Integer>();
        map1.put("key1", 2);
        map1.put("key2", 2);
        //取值
        Integer val = map1.get("key2");
        //删除
        map1.remove("key2");
        //Key存在一样的，值会被更新
        map.putAll(map1);
        //遍历方式一：遍历Key
        for (String key : map.keySet()) {
            //通过key 获取值,获取方式类似List根据索引获取item
            Integer value = map.get(key);
        }
        //遍历方式二：遍历Value
        for (Integer value : map.values()) {

        }

        //遍历方式三：遍历entrySet
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            String key = entry.getKey();//获取Key
            Integer value = entry.getValue();//获取Value
        }
        //foreach: 实体遍历
        for (Map.Entry<String, Integer> m : map.entrySet()) {
            System.out.println("key:" + m.getKey() + " value:" + m.getValue());
        }


        //endregion

        //region List--->C# List
        //list
        //类型不能使基础类型,可以使用包装器。
//        List<int> list11111=new LinkedList<>();

        List<Integer> list = new ArrayList<Integer>();
        list.add(9);
        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(2);
        list1.add(3);
        list1.add(5);
        //取,注意索引从0开始，和数组一样
        Integer first = list1.get(0);
        Integer second = list1.get(1);

        //删除
        list.remove(2);
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
        //取值只能遍历
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
        /**
         * 尽量别用迭代器，用forEach
         */
        linkedHashSet.forEach(p ->
        {

        });
        //endregion

    }

    private void getListObjectProperty() {
        // Arrays.asList 返回的集合大小固定，无法添加和删除
        List<Student> list = new ArrayList<>(Arrays.asList(new Student("fancky1", 1),
                new Student("fancky2", 4),
                new Student("fancky3", 2),
                new Student("fancky4", 3)
        ));

        List<Student> list1 = new ArrayList<>(Arrays.asList(new Student("fancky5", 6),
                new Student("fancky6", 5),
                new Student("fancky7", 8),
                new Student("fancky8", 7)
        ));
        //获取对象集合的某一属性集合
        //lambda表达式
        List<String> nameList = list.stream().map(p -> p.getName()).collect(Collectors.toList());
        //方法引用::
        List<String> nameList1 = list.stream().map(Student::getName).collect(Collectors.toList());
        List<Integer> ageList = list.stream().map(p -> p.getAge()).collect(Collectors.toList());
        //过滤
        List<Student> filterList = list.stream().filter(p -> p.getAge() > 3).collect(Collectors.toList());
        //第一个
        Student first = filterList.get(0);
        //最后一个
        Student last = filterList.get(filterList.size());
        //对象集合排序：实现Comparable接口，然后重写compareTo方法。
        filterList.sort((a, b) -> a.getAge().compareTo(b.getAge()));
        //java8 Comparator
        filterList.sort(Comparator.comparing(Student::getAge));
        //建议filterList.sort
        Collections.sort(filterList, Comparator.comparing(p -> p.getAge()));

        //获取简单的统计
        IntSummaryStatistics intSummaryStatistics = filterList.stream().mapToInt(p -> p.getAge()).summaryStatistics();
        //获取最大
        Integer max = intSummaryStatistics.getMax();
        //获取最小
        Integer min = intSummaryStatistics.getMin();
        // intSummaryStatistics.getCount();
        //正序
        Collections.sort(ageList);
        //逆序
        Collections.reverse(ageList);

        //最大、最小
        Integer maxAge = Collections.max(ageList);
        Integer minAge = Collections.min(ageList);


        //添加
        list.add(new Student("fancky9", 10));
        list.addAll(list1);


        //添加
        List<Student> students = new ArrayList<>();
        students.add(new Student("fancky9", 10));

        List<Student> students1 = new ArrayList<>();
        students.add(new Student("fancky11", 11));
        students.add(new Student("fancky12", 12));

        students.addAll(students1);
        Integer m = 0;
    }

}
