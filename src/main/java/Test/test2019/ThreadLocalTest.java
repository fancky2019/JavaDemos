package Test.test2019;

import Model.Student;
import Test.test2018.Person;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 将全局变量设置为  ThreadLocal，可达到多线程访问同步的效果。
 * 空间换时间
 */
public class ThreadLocalTest {

    ThreadLocal<Student> student = new ThreadLocal<Student>();

    ThreadLocal<List<Student>> studentList = new ThreadLocal<List<Student>>();

    public void test() {

        multiThread();

    }

    private void multiThread() {
        CompletableFuture.runAsync(() ->
        {
            try {
                utilityFunction("fancky", 27);
                utilityFunction1("fancky1", 27);
                utilityFunction1("fancky11", 27);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });
        CompletableFuture.runAsync(() ->
        {
            try {
                utilityFunction("lr1", 26);
                utilityFunction1("fancky1", 26);
                utilityFunction1("fancky11", 26);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });
    }

    private void utilityFunction(String name, int age) throws Exception {
        Student student = new Student(name, age);
        //设置
        this.student.set(student);
        Thread.sleep(3000);
        System.out.println();
        //取值
        System.out.println(MessageFormat.format("utilityFunction ThreadID:{0} Person:{1}", Thread.currentThread().getId(), this.student.get().toString()));
        //删除本地线程的值。
        this.student.remove();
    }

    private void utilityFunction1(String name, int age) throws Exception {
        Student student = new Student(name, age);
        List<Student> list = this.studentList.get();
        if (list != null) {
            list.add(student);
        } else {
            List<Student> students = new LinkedList<>();
            students.add(student);
            studentList.set(students);
        }
        Thread.sleep(3000);
        List<Student> students = this.studentList.get();
        Student lastStudent = students.get(students.size() - 1);
        System.out.println(MessageFormat.format("utilityFunction1 ThreadID:{0} Person:{1}", Thread.currentThread().getId(), lastStudent.toString()));
        //删除本地线程的值。
        this.studentList.remove();
    }
}
