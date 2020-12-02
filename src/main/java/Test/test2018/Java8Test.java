package Test.test2018;

import Model.Student;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
                new Student("fancky6", 5),
                new Student("fancky7", 8),
                new Student("fancky8", 7)
        ));
        //获取对象集合的某一属性集合

        //select
        //lambda表达式
        List<String> nameList = list.stream().map(p -> p.getName()).collect(Collectors.toList());
        //方法引用::
        List<String> nameList1 = list.stream().map(Student::getName).collect(Collectors.toList());
        List<Integer> ageList = list.stream().map(p -> p.getAge()).collect(Collectors.toList());
        //过滤
        List<Student> filterList = list.stream().filter(p -> p.getAge() > 3).collect(Collectors.toList());
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


        //并行处理，类似C#的Parallel
        list.stream().parallel().forEach(p ->
        {
            //todo
        });
        //exist
        boolean exist = list.stream().anyMatch(p -> p.getName() == "fancky");

        //groupingBy
        Map<String, List<Student>> studentsGroupBy = list.stream().collect(Collectors.groupingBy(Student::getName));

        //分组求和
        Map<String, IntSummaryStatistics> groupBySummaryStatistics = list.stream().collect(Collectors.groupingBy(Student::getName, Collectors.summarizingInt(Student::getAge)));

        groupBySummaryStatistics.forEach((groupBy, intSummaryStatistics) ->
        {
            System.out.println(MessageFormat.format("name:{0},age sum:{1}", groupBy, intSummaryStatistics.getSum()));
        });


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

    private void localDateTimeTest() {
        LocalDateTime localDateTime = LocalDateTime.now();

        //转换string
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String dateStr = localDateTime.format(dateTimeFormatter);

//

        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        //string 转换
        LocalDateTime localDateTime4 = LocalDateTime.parse("2018-12-31 13:12:00.000", dateTimeFormatter1);
        //小的时间在前 Duration是秒，纳秒
        Duration duration = Duration.between(localDateTime4, localDateTime);

        Long day = duration.toDays();
        Long days = duration.getSeconds() / (24l * 60l * 60l);
        Integer idays = days.intValue();

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
        //如果值不存在就用Null
        Student st = optionalStudent.orElse(null);
        optionalStudent = Optional.ofNullable(null);
        //  Student st1 = optionalStudent.orElse(null);


//      Optional<Student> fla=  optionalStudent.flatMap(p->Optional.of(p));
        Optional<String> fla = optionalStudent.flatMap(p -> Optional.of(p.getName()));
        Integer m = 0;
    }

    // region 方法引用
    private void methodReference() {
        //方法引用就是lambda表达式的简写。
        //方法应用的原型必须和函数接口相匹配。
        //函数接口就当成C#里的委托用。
        Consumer<String> consumer1 = (s) ->
        {
            String s1 = "abc";
            s1 += s;
            System.out.println(s1);
        };
        consumer1.accept("123");
        Consumer<String> consumer = System.out::println;
        consumer.accept("abc");
        //报错
//        Function<Integer,String> function1=functionMethod;
        //方法应用的原型必须和函数接口相匹配。
        Function<Integer, String> function = this::functionMethod;
        function.apply(10);

        Function<Function<Integer, String>, String> function2 = this::functionMethod2;
        function2.apply(function);


        //
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


}
