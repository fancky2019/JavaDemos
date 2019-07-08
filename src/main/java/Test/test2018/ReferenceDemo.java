package Test.test2018;

import Model.Student;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
  字符串:不改变引用字符串的值。
简单对象：引用传参可改变妻地址。
数组对象:可改变元素属性值、元素指向的地址，不可改变数组大小、重新实例化。
集合对象:可改变元素属性、元素指向的地址、添加新的元素（改变元素个数），不可重新实例化。
 */
public class ReferenceDemo {
    public void test() {
        String str1="123";
        String str2="456";
        stringReference(str1);
        Student student1 = new Student();
        student1.setName("li");
        Student student11 = new Student();
        student11.setName("li");

        simpleObject1(student1);
        simpleObject1(student11);

        Student student2 = new Student();
        student2.setName("li");
        Student[] array2 = new Student[1];
        array2[0] = student2;

        Student student22 = new Student();
        student22.setName("li");
        Student[] array22 = new Student[1];
        array22[0] = student22;


        Student student222 = new Student();
        student222.setName("li");
        Student[] array222 = new Student[]{student222};


        Student student2222 = new Student();
        student2222.setName("li");
        Student[] array2222 = {student2222};

        arrayReference1(array2);

        arrayReference3(array222);
        arrayReference4(array2222);


        Student student3 = new Student();
        student3.setName("li");
        List<Student> list1=new ArrayList<>();
        list1.add(student3);

//        List<Student> list2=new ArrayList<>();
//        list2.addAll(list1);

        Student student33 = new Student();
        student33.setName("li");
        List<Student> list2=new ArrayList<>();
        list2.add(student33);

        Student student333 = new Student();
        student333.setName("li");
        List<Student> list3=new ArrayList<>();
        list3.add(student333);

        Student student3333 = new Student();
        student3333.setName("li");
        List<Student> list4=new ArrayList<>();
        list4.add(student3333);

        listReference1(list1);
        listReference2(list2);
        listReference3(list3);
        listReference4(list4);
        Integer m = 0;
    }

    private void stringReference(String str)
    {
        str="abc";
    }

    /**
     * 可改变属性值
     * @param student
     */
    private void simpleObject1(Student student) {
        student.setName("fancky");
    }

    /**
     * 可指向新的内存地址
     * @param student
     */
    private void simpleObject11(Student student) {
        student = new Student();
        student.setName("fancky");
    }

    /**'
     * 可改变元素的属性值
     * @param array
     */
    private void arrayReference1(Student[] array) {
        Student student = array[0];
        student.setName("fancky");
    }


    /**
     * 元素可指向新的内存地址
     * @param array
     */
    private void arrayReference3(Student[] array) {
        array[0] = new Student();
        array[0].setName("fancky");
    }

    /**
     * 不能重新实例化
     * @param array
     */
    private void arrayReference4(Student[] array) {
        array = new Student[2];
        Student student2 = new Student();
        student2.setName("fancky1");
        array[0]=student2;
        Student student22 = new Student();
        student2.setName("fancky2");
        array[1]=student2;
    }

    /**
     * 可改变元素的属性值
     * @param list
     */
    private  void  listReference1(List<Student> list)
    {
       Student student=list.get(0);
        student.setName("fancky");
    }

    /**
     *元素可指向新的地址
     * @param list
     */
    private  void  listReference2(List<Student> list)
    {
        Student student=new Student() ;
        student.setName("fancky");
        list.set(0,student) ;
    }

    /**
     *可添加新的元素（扩容）
     * @param list
     */
    private  void  listReference3(List<Student> list)
    {
        Student student=new Student() ;
        student.setName("fancky");
        list.add(student) ;
    }

    /**
     *不可重新实例化
     * @param list
     */
    private  void  listReference4(List<Student> list)
    {
        list=new ArrayList<>() ;
        Student student=new Student() ;
        student.setName("fancky");
        list.add(student) ;
    }
}
