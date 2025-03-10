package Test.test2021;

import Model.Student;
import org.checkerframework.checker.units.qual.K;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
泛型如果不指定类型参数，其类型就是object类型。C#里必须要指定类型参数
E - Element (在集合中使用，因为集合中存放的是元素)
T - Type（Java 类）
R - Return
K - Key（键）
V - Value（值）
N - Number（数值类型）
？ - 表示不确定的java类型


(ArrayList)顺序表‌：顺序表的存储方式是顺序存储，在内存中申请一块连续的空间，通过下标(实现RandomAccess 接口)来进行存储。
                 顺序表在物理上是连续的，。‌不能扩容
 (LinkedList)链表‌：链表的存储方式是链式存储，申请的空间未必连续，扩容 n+n/2


 */
public class CollectionTest {
    public void test() {
        getListObjectProperty();
        fun();
//        RandomAccess
    }


    private void getListObjectProperty() {
        // Arrays.asList 返回的集合大小固定，无法添加和删除
        List<Student> list = new ArrayList<>(Arrays.asList(new Student("fancky1", 1),
                new Student("fancky2", 4),
                new Student("fancky3", 2),
                new Student("fancky4", 3)
        ));

        List<Student> list1 = new ArrayList<>(Arrays.asList(new Student("fancky5", 6),
                new Student("fancky6", 5, "程序员"),
                new Student("fancky7", 8, "农民"),
                new Student("fancky8", 7, "农民"),
                new Student("fancky9", 12, "教师")
        ));
        //获取对象集合的某一属性集合

        //select  skip limit 相当于C# skip  take
        list.stream().skip(1).limit(2).collect(Collectors.toList());
        // JAVA 9
        // list.stream().takeWhile

        //lambda表达式
        List<String> nameList = list.stream().map(p -> p.getName()).collect(Collectors.toList());
        //方法引用::
        List<String> nameList1 = list.stream().map(Student::getName).collect(Collectors.toList());
        List<Integer> ageList = list.stream().map(p -> p.getAge()).collect(Collectors.toList());
        //过滤
        List<Student> filterList = list.stream().filter(p -> p.getAge() > 3).collect(Collectors.toList());
        //去重
        List<Integer> eosStudentIds = list.stream().map(p -> p.getAge()).distinct().collect(Collectors.toList());
        //取一个
        Optional<Student> s1 = list.stream().filter(p -> p.getAge() > 20).findFirst();
        Student s11 = s1.orElse(null);
//        //实现Comparable接口，然后重写compareTo方法。
//        ageList.sort((a, b) ->
//        {
//            if (a >= b) {
//                return 1;
//            } else
//
//            {
//                return -1;
//            }
//        });

        java.util.List<Integer> listRe = new ArrayList<>();
        //并行处理，类似C#的Parallel
        list.stream().parallel().forEach(p ->
        {
            //todo
        });

        //按照顺序执行
        list.stream().parallel().forEachOrdered(p ->
        {
            //todo
        });

        //exist
        boolean exist = list.stream().anyMatch(p -> p.getName().equals("fancky"));

        //  BigDecimal 求和
//        BigDecimal bb =list.stream().map(Student::getAge).reduce(BigDecimal.ZERO,BigDecimal::add);

        //求和
        Long sum = list.stream()
                .collect(Collectors.summarizingLong(Student::getAge)).getSum();
        list.stream().collect(Collectors.summarizingLong(p -> p.getAge())).getSum();
        Integer sum3 = list.stream().map(p -> p.getAge()).reduce(Integer::sum).get();
        Integer sum4 = list.stream().map(p -> p.getAge()).reduce((a, b) -> a + b).get();
        Integer sum5 = list.stream().mapToInt(p -> p.getAge()).sum();
        List<Student> listGroup = new ArrayList<>(
                Arrays.asList(new Student("fancky5", 6, "农民"),
                        new Student("fancky6", 5, "程序员"),
                        new Student("fancky7", 8, "农民"),
                        new Student("fancky5", 7, "农民"),
                        new Student("fancky9", 12, "教师")
                ));
        //分组 groupingBy多：单个属性分组,默认hashMao
        Map<String, List<Student>> studentsGroupBy = listGroup.stream().collect(Collectors.groupingBy(Student::getName));

        //平铺转list  平铺数组 .flatMap(Arrays::stream) 平铺listCollection::stream
        List<Student> listStu=  studentsGroupBy.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        HashMap<String, List<Student>> studentsGroupBy1 = listGroup.stream().collect(Collectors.groupingBy(Student::getName,HashMap::new,Collectors.toList()));
        //分组：多个属性分组--将多个属性拼接成一个属性
        Map<String, List<Student>> multiFieldGroupBy = listGroup.stream().collect(Collectors.groupingBy(p -> MessageFormat.format("{0}_{1}", p.getName(), p.getJob())));
        //对分组后形成的字典进行迭代
        multiFieldGroupBy.forEach((key, val) ->
        {
            String[] fields = key.split("_");
            //然后组装分组后的model . 如：name,age, count
        });
        //分组求和
        Map<String, IntSummaryStatistics> groupBySummaryStatistics = listGroup.stream().collect(Collectors.groupingBy(Student::getName, Collectors.summarizingInt(Student::getAge)));

        groupBySummaryStatistics.forEach((groupByFiled, intSummaryStatistics) ->
        {
            System.out.println(MessageFormat.format("name:{0},age sum:{1}", groupByFiled, intSummaryStatistics.getSum()));
        });


        HashMap<Integer, String> hashMap = null;
        List<Integer> intList = new ArrayList<>();
        intList.add(1);
        intList.add(9);
        intList.add(18);
        intList.add(3);
        intList.add(1);

        //Integer进行排序
        //正序
        List sorted1 = intList.stream().sorted().collect(Collectors.toList());
        //倒叙
        sorted1 = intList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        //List对象进行排序
        //排序 正序
        list.stream().sorted(Comparator.comparing(Student::getAge)).collect(Collectors.toList());
        //排序 逆序.reversed()
        list.stream().sorted(Comparator.comparing(Student::getAge).reversed()).collect(Collectors.toList());
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

    private void fun() {


        /*
            // views
            private transient KeySetView<K,V> keySet;
            private transient ValuesView<K,V> values;
            private transient EntrySetView<K,V> entrySet;
         */
        HashMap<String, String> hashMap = new LinkedHashMap<>();
        HashMap<String, String> hashMap1 = new HashMap<>();
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("key1", "value1");
        concurrentHashMap.put("key2", "value2");
        concurrentHashMap.put("key3", "value3");
        concurrentHashMap.get("key1");


        //key 集合
        ConcurrentHashMap.KeySetView<String, String> keySet= concurrentHashMap.keySet();
        //value 集合
        Collection<String> valuesView= concurrentHashMap.values();
        concurrentHashMap.keys();



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

        HashSet<String> set = new HashSet<String>();
        set.add("aaa");
        set.add("bbb");
        set.add("ccc");
        for (String s:set) {
            System.out.println(s);
        }

        Enumeration<String> getHeaderNames = null;
        List<String> list = Collections.list(getHeaderNames);

    }
}
