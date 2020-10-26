package Test.test2019;

import Model.Student;
import Test.test2018.SetTest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/*
hashcode 生成见实体类Student

 HashCode 用于散列表存储的如HashMap、HashSet等set集合。
         HashCode 用于散列表存储的如Dictionary、HashSet等set集合。
        快速判读是否存储了改对象：一、根据hashcode 判读该散列地址是否使用，未使用（没有存储）直接添加。
        二、如果散列地址使用了，判断对象是否相等（Equals），如果相等不添加，如果不相等则散列到新的地址，判断是否添加。
如果不重写hashCode，则返回的是对象的内存地址。

JAVA 要求同一对象必须要有相同的hashCode，如果hashCode不同也不报错。
 */
public class HashCodeTest {
    public void test() {

        getHashCode();
//        hashSetSameHashCode();
//        hashMapSameHashCode();
    }

    private void getHashCode() {

        Student student = new Student("fancky", 32);
        //c#获取hashCode通过VS生成，具体参照“笔记--》C#-->VS2017--》VS生成 Equals 和 GetHashCode 方法重写”
        Integer hashCode1 = Objects.hash(student.getName(), student.getAge());
        Integer hashCode2 = Objects.hashCode(student);
        Integer hashCode3 = Objects.hashCode(student);

        Integer hashCode4 = student.hashCode();
        Integer hashCode5 = student.hashCode();

        Student student1 = new Student("fancky", 32);
        Integer hashCode11 = student1.hashCode();

        HashSet<Student> hashSet = new HashSet<Student>();
        hashSet.add(student);
        try {
            Thread.sleep(2000);
        } catch (Exception ex) {

        }
        hashSet.add(student);
        Integer m = 0;
    }

    private  void hashSetSameHashCode()
    {
        HashSet<Student> hashSet = new HashSet<Student>();
        Student student1 = new Student("fancky", 32);
        hashSet.add(student1);
        Student student2 = new Student("fancky", 321);
        hashSet.add(student2);
        Student student3 = new Student("fancky", 332);
        hashSet.add(student3);
        Student student4= new Student("fancky", 3321);
        hashSet.add(student4);


    /*
    不重写equals，比较的是内存地址，不同对象地址不同。
    HashSet会重复添加，尽管两个对象的字段值一样。
    如果重写了，只能添加一次。
     */
        HashSet<Student> hashSet1 = new HashSet<Student>();
        hashSet1.add(student1);
        Student student111 = new Student("fancky", 32);
        hashSet1.add(student111);

        HashMap<Integer ,Student> hashMap = new HashMap<>();
        Student student11 = new Student("fancky", 32);
        hashMap.put(1,student11);
        Student student22 = new Student("fancky", 321);
        hashMap.put(1,student22);
        Student student33 = new Student("fancky", 332);
        hashMap.put(1,student33);
        Student student44= new Student("fancky", 3321);
        hashMap.put(1,student44);
       Student student= hashMap.get(1);

    }

    private  void  hashMapSameHashCode()
    {
        /*
        如果HashCode一样，后添加的值占据链表第一个位置，新添加的next指向先添加的
         */
        HashMap<Student ,Student> hashMap = new HashMap<>();
        Student student11 = new Student("fancky", 32);
        hashMap.put(student11,student11);
        Student student22 = new Student("fancky", 321);
        hashMap.put(student22,student22);
        /*
        A：首先取链表第一个元素判断：
        1）：判断hashcode一样，不一样返回value,如果一样执行2
        2）：判断key一样，如果不一样，执行B。
        B：循环hashcode的链表,直到找到和key一样的value。
         */
        Student student1=  hashMap.get(student11);
        Student student2=  hashMap.get(student22);


        /*
        相同的key，后添加的覆盖前面的
         */
        HashMap<Integer ,Student> hashMap1 = new HashMap<>();
        Student student111 = new Student("fancky", 32);
        hashMap1.put(1,student111);
        Student student222 = new Student("fancky", 321);
        hashMap1.put(1,student222);
        Student stu=  hashMap.get(student111);
        Student stu1=  hashMap.get(student111);
    }
}
