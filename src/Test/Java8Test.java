package Test;

import Model.Student;

import java.util.*;
import java.util.stream.Collectors;

public class Java8Test {
    public void test() {
        getListObjectProperty();
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

//    Optional<Integer>
}
