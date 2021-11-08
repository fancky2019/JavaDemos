package Test.test2019;

import Model.Student;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/*
 AtomicInteger
 AtomicReference


 AtomicStampedReference:可以避免ABA问题，内部封装了引用和stamp版本控制。
 */
public class AtomicReferenceTest {
    public void test() {
        fun();
    }

    private void fun()
    {
        AtomicReference<Student> atomicReference=new AtomicReference<>();
        Student student1=new Student();
        Student student2=new Student();
        atomicReference.compareAndSet(student1,student2);

    }
}
