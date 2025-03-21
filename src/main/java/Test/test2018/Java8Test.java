package Test.test2018;

import Model.Student;
import com.rabbitmq.client.Return;

import java.math.BigDecimal;
import java.text.Collator;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 日期占位符
 * G　　"公元"
 * y　　四位数年份
 * M　　月
 * d　　日
 * h　　时 在上午或下午 (1~12)
 * H　　时 在一天中 (0~23)
 * m　　分
 * s　　秒
 * S　　毫秒
 */
/*
java8:接口中可以有default 方法和静态方法
 */
public class Java8Test {
    public void test() {
        getListObjectProperty();
//        localDateTimeTest();
//        methodReference();

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


//        list.parallelStream()
//        list.stream().parallel().forEach();

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

        //java  stream

       /* stream是顺序流，由主线程按顺序对流执行操作，
        parallelStream是并行流，内部以多线程并行执行的方式对流进行操作，
        需要注意使用并行流的前提是流中的数据处理没有顺序要求（会乱序，即使用了forEachOrdered）
        */
        java.util.List<Integer> listRe = new ArrayList<>();
        //并行处理，不保证元素顺序执行，类似C#的Parallel
        list.stream().parallel().forEach(p ->
        {
            //todo
        });

        //按照元素顺序执行
        list.stream().parallel().forEachOrdered(p ->
        {
            //todo
        });

        Stream.of(list).parallel().forEach(p -> System.out.println(p));

        List<Student> listGroup = new ArrayList<>(
                Arrays.asList(new Student(1, "fancky5", 6, "农民"),
                        new Student(2, "fancky6", 5, "程序员"),
                        new Student(3, "fancky7", 8, "农民"),
                        new Student(4, "fancky5", 7, "农民"),
                        new Student(5, "fancky9", 12, "教师")
                ));
        //Function.identity()返回t -> t形式的Lambda表达式。
        Map<Integer, Student> map = listGroup.stream().collect(Collectors.toMap(Student::getId, Function.identity()));
        Map<Integer, Student> map1 = listGroup.stream().collect(Collectors.toMap(p -> p.getId(), p -> p));

        List<Student> listGroup1 = map.values().stream().collect(Collectors.toList());
        listGroup1 = new ArrayList<>(map.values());

        //元素出现的次数 分组
        //分组 groupingBy多：单个属性分组,默认hashMao
        Map<String, List<Student>> studentsGroupBy = listGroup.stream().collect(Collectors.groupingBy(Student::getName));
        studentsGroupBy = listGroup.stream().collect(Collectors.groupingBy(p->p.getName()));

        for(String key:studentsGroupBy.keySet())
        {
            List<Student> ll=studentsGroupBy.get(key);
            if(ll.size()>1)
            {
                //重复
            }
        }
        List<Student> listStu = studentsGroupBy.values().stream().flatMap(Collection::stream).collect(Collectors.toList());

        List<Student> listStu1 = studentsGroupBy.values().stream().flatMap(p->p.stream()).collect(Collectors.toList());

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


        HashMap<String, List<Student>> studentsGroupBy1 = listGroup.stream().collect(Collectors.groupingBy(Student::getName, HashMap::new, Collectors.toList()));
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





        //添加
        list.add(new Student("fancky9", 10));
        list.addAll(list1);


        //添加
        List<Student> students = new ArrayList<>();
        students.add(new Student("fancky9", 10));

        List<Student> students1 = new ArrayList<>();
        students1.add(new Student("fancky11", 11));
        students1.add(new Student("fancky12", 12));

        students.addAll(students1);


        //最大、最小
        Integer maxAge = Collections.max(ageList);
        Integer minAge = Collections.min(ageList);
        int maxAge11= students1.stream().map(p->p.getAge()).mapToInt(Integer::intValue).max().orElse(0);
        Student maxAgeStudent12=   students1.stream().max(Comparator.comparing(Student::getAge)).orElse(null);
        int maxAge13=  students1.stream().map(p->p.getAge()).max(Comparator.comparing(Integer::intValue)).orElse(0);
        //年龄最大的对象
        Student oldestPerson = Collections.max(students1, Comparator.comparingInt(Student::getAge));

        Integer m = 0;
    }

    private void localDateTimeTest() {
        LocalDateTime localDateTime = LocalDateTime.now();

        //转换string
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String dateStr = localDateTime.format(dateTimeFormatter);

        //时间格式化
        String dateString = "2021-10-01T12:00:00.965Z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);

        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        //string 转换
        LocalDateTime localDateTime4 = LocalDateTime.parse("2018-12-31 13:12:00.000", dateTimeFormatter1);
        //小的时间在前 Duration是秒，纳秒
        Duration duration = Duration.between(localDateTime4, localDateTime);

        Long day = duration.toDays();
        long days = duration.getSeconds() / (24L * 60L * 60L);
        Integer idays = (int) days;

        LocalDate tomorrow1111 = LocalDate.parse("2020-01-14", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime localDateTime1111 = LocalDateTime.of(tomorrow1111, LocalTime.of(0, 0, 0));

        //2019-09-20T09:46:20.203
        LocalDateTime localDateTimeNow = LocalDateTime.now();

        //转换成带时区的时间
        //2019-09-20T09:46:20.203+08:00[Asia/Shanghai]
        ZonedDateTime zonedDateTime = localDateTimeNow.atZone(ZoneId.systemDefault());
        ZonedDateTime atZone = localDateTimeNow.atZone(ZoneId.of("Asia/Shanghai"));

        //ZonedDateTime 转LocalDateTime
        ZonedDateTime zonedDateTime1 = ZonedDateTime.now();
        zonedDateTime1.toLocalDateTime();

        //格式化
        //2019-09-20 09:53:30.997
        String nowStr = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        //日期运算，年月日部分运算。
        LocalDateTime addDateTime = localDateTimeNow.plusDays(1);
        LocalDateTime minusDateTime = localDateTimeNow.minusHours(3);

        // 东八区时
        TimeZone timeZone = TimeZone.getTimeZone("GMT-8");


        //获取秒数
        Long seconds = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        //获取毫秒数
        Long milliSeconds = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();

        //和时间戳相互转换
        //  Convert LocalDateTime to milliseconds since January 1, 1970, 00:00:00 GMT
        Long milliseconds = LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        // epoch milliseconds to LocalDateTime
        LocalDateTime newNow = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneOffset.UTC);

        Integer m = 0;
    }

    private void optionalTest() {
        Student stu = new Student("fancky", 1);
        Student student = null;
        Optional<Student> optionalStudent = Optional.ofNullable(stu);
        //如果值存在，执行函数接口consumer，和C#的?.
        optionalStudent.ifPresent(p -> System.out.println(p.getName()));

        //取值
        Student val = null;
        if (optionalStudent.isPresent()) {
            val = optionalStudent.get();
        }
        //如果值存在就返回值，若不存在就返回指定的Null
        Student st = optionalStudent.orElse(null);
        optionalStudent = Optional.ofNullable(null);
        //  Student st1 = optionalStudent.orElse(null);


        Optional<Student> studentNull = Optional.ofNullable(student);

//      Optional<Student> fla=  optionalStudent.flatMap(p->Optional.of(p));
        //flatmap可以将一个2维的集合转成1维度,map只能将分割结果转成一个List,所以输出为list对象
        Optional<String> fla = optionalStudent.flatMap(p -> Optional.of(p.getName()));
        Integer m = 0;
    }

    // region 方法引用
    private void methodReference() {
        //方法引用就是lambda表达式的简写。
        //方法应用的原型必须和函数接口相匹配。
        //函数接口就当成C#里的委托用。


       /*
        Consumer<T> :一入，返回void
        Supplier<T> ：无入，一出
        Predicate<T> :一入，返回bool
        Function<T, R> :一入一出
       */

        //一个入参，无返回
        Consumer<String> consumer1 = (s) ->
        {
            String s1 = "abc";
            s1 += s;
            System.out.println(s1);
        };
        consumer1.accept("123");
        Consumer<String> consumer = System.out::println;
        consumer.accept("abc");

        //一入一出
        //报错
//        Function<Integer,String> function1=functionMethod;
        //方法应用的原型必须和函数接口相匹配。
        Function<Integer, String> function = this::functionMethod;
        function.apply(10);

        Function<Function<Integer, String>, String> function2 = this::functionMethod2;
        function2.apply(function);


        //无入参，一个返回
        Supplier<Java8Test> supplier = Java8Test::new;
        Java8Test java8Test = supplier.get();

        Supplier<Java8Test> supplier1 = () -> new Java8Test();
        Java8Test java8Test1 = supplier1.get();
    }

    private String functionMethod(Integer m) {
        System.out.println(MessageFormat.format("functionMethod executed ,parameter is {0}", m));
        return m.toString();
    }

    private String functionMethod2(Function<Integer, String> function) {
        System.out.println(" functionMethod2 executed");
        return function.apply(20);
    }

    //endregion

    private void functionalInterface() {
        //匿名方法赋值给函数接口，像是C#直接将匿名方法赋值给委托
        Consumer<String> consumer = p -> System.out.println(p);
        //函数接口调用方法，C#里委托是方法，就直接调用,不用掉方法
        consumer.accept("Consumer");

        //一入一出
        Function<String, Integer> function = p -> Integer.parseInt(p);
        Integer num = function.apply("1");

        Predicate<Boolean> predicate = p -> !p;
        Boolean re = predicate.test(false);

        //两入无出
        BiConsumer<Integer, Object> biConsumer = (a, b) ->
        {

        };
        //跟C#不一样biConsumer（3,4），java 函数接口只是一个函数接口（可能有多个方法）要指定调用方法
        biConsumer.accept(3, 4);


        //两入一出
        BiFunction<Integer, Integer, String> biFunction = (m, n) ->
        {
            return m + n + "";
        };

        biFunction.apply(1, 2);
    }


}
