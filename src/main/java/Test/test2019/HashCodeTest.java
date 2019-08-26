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
    }

    public void getHashCode() {

        Student student = new Student("fancky", 32);
        //c#获取hashCode通过VS生成，具体参照“笔记--》C#-->VS2017--》VS生成 Equals 和 GetHashCode 方法重写”
        Integer hashCode1 = Objects.hash(student.getName(), student.getAge());
        Integer hashCode2 = Objects.hashCode(student);
        Integer hashCode3 = Objects.hashCode(student);

        Integer hashCode4 = student.hashCode();
        Integer hashCode5 = student.hashCode();


        HashSet<Student> hashSet = new HashSet<Student>();
        hashSet.add(student);
        try {
            Thread.sleep(2000);
        } catch (Exception ex) {

        }
        hashSet.add(student);
        Integer m = 0;
    }
}
