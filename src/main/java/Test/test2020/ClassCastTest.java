package Test.test2020;

import Model.Student;
import Model.StudentParent;

public class ClassCastTest {
    public void test() {
        cast();
    }

    /*
    和C#一样：子类转换成父类后可以再转换成子类，父类不能直接转换成子类。
     */
    private void cast() {

        //region 子类转换成父类后可以再转换成子类
        Student student = new Student();
        student.setName("fancky");
        student.setAge(27);
        StudentParent studentParent = null;
        if (student instanceof StudentParent) {
            studentParent = (StudentParent) student;
        }

        Student student1 = null;
        if (studentParent instanceof Student) {
            student1 = (Student) studentParent;
        }
        //endregion

        //region 父类不能直接转换成子类
        StudentParent studentParent11 = new StudentParent();
        Student student11 = null;
        if (studentParent11 instanceof Student) {
            student11 = (Student) studentParent11;
        } else {
            System.out.println("studentParent11 is not instanceof ");
        }
        //endregion

        int m = 0;
    }
}
