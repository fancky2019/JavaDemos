package Test.test2019;

import Model.Student;
import Test.test2018.Person;

/**
 * = =:对于预定义的值类型，如果操作数的值相等，则相等运算符 (==) 返回 true，否则返回 false。
 * 对于引用类型，如果两个操作数引用同一个对象，则 == 返回 true。
 * String 类型，== 同一个对象，注意内存驻留。
 * equals:number类型值一样，String逐个字符比较是否一样，具体看String类的equals方法
 *
 * == 比较地址，如果没重写equal 则对象地址比较，否则调用equal
 */
public class EqualsOperatorTest {
    public void test() {
        EqualOperator();
        Equal();
    }

    private void EqualOperator() {
        int a1 = 10;
        int a2 = 9;
        int a11 = 10;
        Boolean r1 = a1 == a11;// true
        Boolean r12 = a1 == a2;//false
        String s1 = "abc";
        String s2 = "d";
        String s11 = "abc";
        Boolean r2 = s1 == s11; // true
        Boolean r21 = s1 == s2;//false
        Student student1 = new Student("fancky", 28);
        Student student2 = new Student("fancky", 29);
        Student student11 = new Student("fancky", 28);
        Student student3 = student1;
        Boolean r3 = student1 == student11;// false
        Boolean r32 = student1 == student2;//false
        Boolean r33 = student1 == student3;//true

        Integer m = 0;
    }

    private void Equal() {
        Integer a1 = 10;
        Integer a2 = 9;
        Integer a11 = 10;
        //拆箱比较intValue
        Boolean ri = a1.equals(a2);
        Boolean ri1 = a1.equals(a11);

        String s1 = "abc";
        String s2 = "d";
        String s11 = "abc";
        Boolean r2 = s1.equals(s11); // true
        Boolean r21 = s1.equals(s2);//false
        Student student1 = new Student("fancky", 28);
        Student student2 = new Student("fancky", 29);
        Student student11 = new Student("fancky", 28);
        Student student3 = student1;
        Boolean r3 = student1.equals(student11);// true,Person重写了Equals，如果不重写false
        Boolean r32 = student1.equals(student2);//false
        Boolean r33 = student1.equals(student3);//true
        Integer m = 0;
    }
}
