package Test.test2019;

import Model.Student;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/*
封装了了一个转换标记的变量用于表示是否转换过，代替每次变动的版本号
 */
public class AtomicMarkableReferenceTest {
    public void test() {
        fun();
    }

    private void fun() {
        Student student1 = new Student();
        student1.setName("name1");
        Student student2 = new Student();
        student2.setName("name2");
        //引用student1
        AtomicMarkableReference<Student> atomicMarkableReference = new AtomicMarkableReference<>(student1, false);
        //引用student2
        atomicMarkableReference.compareAndSet(student1, student2, false, true);
        //引用student2 无法交换，和student1不相等
        atomicMarkableReference.compareAndSet(student1, student2, true, true);
        //
        atomicMarkableReference.compareAndSet(student2, student1, true, false);

        int m = 0;
    }
}
