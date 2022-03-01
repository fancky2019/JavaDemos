package Test.test2019;

import Model.Product;
import Model.Student;
import Test.test2018.Person;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamOptionalTest {
    public void test() {
        //streamFun();
        operationFun();
    }

    private void streamFun() {
        List<Integer> listInteger = new ArrayList<Integer>();
        listInteger.add(4);
        listInteger.add(2);
        listInteger.add(3);
        listInteger.add(5);
        //原集合已经排序
        //   listInteger.sort(Integer::compareTo);
        // listInteger.sort(Comparator.comparing(p->p));
        listInteger.sort(Comparator.comparing(p -> p));
        Integer n = 0;


        List<Student> list = new ArrayList<>(Arrays.asList(new Student("fancky1", 1),
                new Student("fancky2", 4),
                new Student("fancky3", 2),
                new Student("fancky4", 3),
                new Student("fancy", 31),
                new Student("fancy", 32),
                new Student("fancy", 33),
                new Student("test", 21),
                new Student("test", 22)
        ));

        List<Student> list1 = new ArrayList<>(Arrays.asList(new Student("fancky5", 6),
                new Student("fancky6", 5),
                new Student("fancky7", 8),
                new Student("fancky8", 7)
        ));
        List<Student> list2 = new ArrayList<>(Arrays.asList(
                new Student(null, 5)

        ));
//        String str="";
//        str.toCharArray();
//        Character[] cha=null;
//        cha.toString();


        //查找
        //必须先过滤才能得到，findAny然后再在流中处理
        Optional<Student> optionalStudent = list.stream().filter(p -> p.getName().contains("f")).findAny();
        //如果不存在就赋值null
        Student student = optionalStudent.orElse(null);

        Student student1 = null;
        //取之前要判断，不然抛异常
        if (optionalStudent.isPresent()) {
            student1 = optionalStudent.get();
        }

        //空指针判断,过滤时候最好加上空指针判断
        Boolean exist11 = list2.parallelStream().filter(p -> p.getName() != null).anyMatch(p -> p.getName().contains("fancky"));


        //判断存在
        Boolean exist = list.parallelStream().anyMatch(p -> p.getName().contains("fancky"));


        //最大
        list.stream().filter(p -> p.getName() != null).map(p -> p.getName().length()).max(Integer::compareTo);
        list.stream().filter(p -> p.getName() != null).map(p -> p.getName().length()).max(Math::max);
        Student maxNameLength = list.stream().filter(p -> p.getName() != null).max((p, q) -> Integer.max(p.getName().length(), q.getName().length())).orElse(null);
        Student maxAge = list.stream().max((p, q) -> Integer.max(p.getAge(), q.getAge())).orElse(null);

        //最小
        Student minAge = list.stream().max((p, q) -> Integer.min(p.getAge(), q.getAge())).orElse(null);


        //条数
        Long count = list.stream().filter(p -> p.getAge() > 2).count();
        Integer size = list.size();

        //去重  according to Object.equals(Object)) of this stream.
        list.stream().distinct();


        //排序
        //原集合（list）顺序不变,生成的集合有顺序
        //正序
        List<Student> sortedAsc1 = list.stream().sorted(Comparator.comparing(Student::getAge)).collect(Collectors.toList());
        List<Student> sortedAsc2 = list.stream().sorted((a, b) -> a.getAge().compareTo(b.getAge())).collect(Collectors.toList());
        //逆序
        List<Student> sortedDesc1 = list.stream().sorted(Comparator.comparing(Student::getAge).reversed()).collect(Collectors.toList());

        //不用stream,原集合（list）排序
        //正序
        list.sort(Comparator.comparing(Student::getAge));
        //逆序
        list.sort(Comparator.comparing(Student::getAge).reversed());


        //求和
        Integer sum1 = list.stream().map(p -> p.getAge()).reduce(0, (x, y) -> x + y);

        //平均
        Double avg = list.stream().collect(Collectors.averagingDouble(p -> p.getAge()));

        //分组
        //Key分组的字段,分组的数据
        Map<String, List<Student>> group1 = list.stream().collect(Collectors.groupingBy(p -> p.getName()));
        //单个字段分组统计个数
        Map<String, Long> group2 = list.stream().collect(Collectors.groupingBy(Student::getName, Collectors.counting()));

        //分组求和
        Map<String, Integer> group3 = list.stream().collect(Collectors.groupingBy(Student::getName, Collectors.summingInt(Student::getAge)));
        //分组平均
        Map<String, Double> group4 = list.stream().collect(Collectors.groupingBy(Student::getName, Collectors.averagingDouble(Student::getAge)));

        //两个字段分组
        Map<String, Map<Integer, List<Student>>> collect
                = list.stream().collect(
                Collectors.groupingBy(
                        Student::getName, Collectors.groupingBy(Student::getAge)
                )
        );

        //两个分组求和
        Map<String, Map<Integer, Integer>> collect1
                = list.stream().collect(
                Collectors.groupingBy(
                        Student::getName, Collectors.groupingBy(Student::getAge, Collectors.summingInt(Student::getAge))
                )
        );

        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setProductname("li");
        product.setProductstyle("st");
        product.setStockid(1);
        productList.add(product);

        product = new Product();
        product.setProductname("li");
        product.setProductstyle("st");
        product.setStockid(1);
        productList.add(product);
        product = new Product();

        product.setProductname("li");
        product.setProductstyle("st1");
        product.setStockid(1);
        productList.add(product);

        product = new Product();
        product.setProductname("lir");
        product.setProductstyle("st");
        product.setStockid(1);
        productList.add(product);

        product = new Product();
        product.setProductname("lir");
        product.setProductstyle("st");
        product.setStockid(12);
        productList.add(product);


        //三个分组
        Map<String, Map<String, Map<Integer, Long>>> groupByMap
                = productList.stream().collect(
                Collectors.groupingBy(
                        Product::getProductname,
                        Collectors.groupingBy(Product::getProductstyle,
                                Collectors.groupingBy(
                                        Product::getStockid,
                                        Collectors.counting()
                                )
                        )
                )
        );

        //遍历获取分组信息
        /**
         * groupBy:lir-st-1  value:1
         * groupBy:lir-st-12  value:1
         * groupBy:li-st1-1  value:1
         * groupBy:li-st-1  value:2
         */
        groupByMap.keySet().forEach(key1 ->
        {
            Map<String, Map<Integer, Long>> groupByMap2 = groupByMap.get(key1);
            groupByMap2.keySet().forEach(key2 ->
            {
                Map<Integer, Long> groupByMap3 = groupByMap2.get(key2);
                groupByMap3.keySet().forEach(key3 ->
                {
                    String str = String.format("groupBy:%s-%s-%s  value:%d", key1, key2, key3, groupByMap3.get(key3));
                    System.out.println(str);
                });
            });
        });

        Integer m = 0;
        m.equals(3);
    }

    private void operationFun() {
        Integer m = null;

        //赋值
        Optional<Integer> optionalInteger = Optional.ofNullable(m);
        Optional<Integer> op1 = Optional.ofNullable(5);
        //empty 相当于赋值Null
        Optional<Integer> emp = Optional.empty();

        //取值
        Integer n = optionalInteger.orElse(null);
        Integer n1 = op1.orElse(null);
        if (op1.isPresent()) {
            Integer num = op1.get();
        }

        //如果有值则执行
        op1.ifPresent(p ->
        {
            Integer q = p;//5
        });
        //会自动将返回值包装成Optional
        Optional op2 = op1.map(p -> 2 * p);//10
        //返回必须设置Optional类型
        Optional op3 = op1.flatMap(p -> Optional.of(2 * p));//10
        Optional op4 = optionalInteger.map(p -> 2 * p);//empty


        Integer p = 0;
    }
}
