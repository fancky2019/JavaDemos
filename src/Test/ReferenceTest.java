package Test;

import Model.Student;

public class ReferenceTest {
    //字符串驻留，重新申请一个空间保存新的值，不改变引用源的值。
    public void stringReference(String str) {
        str = "abc";
    }

    public void classParam(Student student) {
        student.setAge(10);
        student.setName("fancky");
    }

    public Student changeObj(Student student) {
        student.setAge(10);
        student.setName("fancky");
        return student;
    }
}
