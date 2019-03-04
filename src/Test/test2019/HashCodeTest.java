package Test.test2019;

import Model.Student;

import java.util.Objects;

public class HashCodeTest {
    public  void  test()
    {
        getHashCode();
    }

    public void  getHashCode()
    {
        Student student=new Student("fancky",32) ;
        //c#获取hashCode通过VS生成，具体参照“笔记--》C#-->VS2017--》VS生成 Equals 和 GetHashCode 方法重写”
       Integer hashCode1= Objects.hash(student.getName(),student.getAge());
       Integer hashCode2=Objects.hashCode(student);
       Integer m=0;
    }
}
