package Test.test2019;

import Model.Student;
import scala.Int;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 将全局变量设置为  ThreadLocal，可达到多线程访问同步的效果。
 * 空间换时间
 * <p>
 * 避免加锁用ThreadLocal
 * <p>
 * <p>
 * ThreadLocalMap 存储在Thread 类中，ThreadLocalMap内部 Entry[] key -->ThreadLocal  value-->data
 * 当GC 时候若ThreadLocal没有强引用，则会回收Entry 里的key.若没有remove 会造成内存泄漏，直到线程结束时候 Thread 释放空间 ThreadLocalMap。
 */
public class ThreadLocalTest {

    ThreadLocal<Student> threadLocal = new ThreadLocal<Student>();

    ThreadLocal<List<Student>> studentList = new ThreadLocal<List<Student>>();

    ThreadLocal<Integer> threadLocalInt = new ThreadLocal<>();

    public void test() {
        //ThreadLocal 存储在Thread类内通过 ThreadLocal 的get()方法查看
        Thread thread=Thread.currentThread();
//        ThreadLocal.ThreadLocalMap threadLocals

        try {
            //线程一般采用线程池思想，服用，所以threadLocal 内部ThreadLocalMap 采用 Entry[] 存储。就造成内存泄漏。
            threadLocalInt.set(1);
            threadLocalInt.set(2);//替换之前的值
            Integer num = threadLocalInt.get();
            threadLocalInt.remove();
            num = threadLocalInt.get();//null
            utilityFunction("fancky", 27);
//            multiThread();
        } catch (Throwable e) {
            e.printStackTrace();
        }

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
            } catch (java.lang.Exception ex) {
                System.out.println(ex.getMessage());
            }
        });
    }

    private void utilityFunction(String name, int age) {
        Student student = new Student(name, age);
        //设置
        this.threadLocal.set(student);
        try {
            System.gc();
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //取值
        System.out.println(MessageFormat.format("utilityFunction ThreadID:{0} Person:{1}", Thread.currentThread().getId(), this.threadLocal.get().toString()));
        //删除本地线程的值。
        this.threadLocal.remove();
    }

    private void utilityFunction1(String name, int age) {
        Student student = new Student(name, age);
        List<Student> list = this.studentList.get();
        if (list != null) {
            list.add(student);
        } else {
            List<Student> students = new LinkedList<>();
            students.add(student);
            studentList.set(students);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Student> students = this.studentList.get();
        Student lastStudent = students.get(students.size() - 1);
        System.out.println(MessageFormat.format("utilityFunction1 ThreadID:{0} Person:{1}", Thread.currentThread().getId(), lastStudent.toString()));
        //删除本地线程的值。
        this.studentList.remove();
    }
}
