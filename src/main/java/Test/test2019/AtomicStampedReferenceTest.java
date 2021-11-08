package Test.test2019;

import Model.Student;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;


/*
  private static class Pair<T> {
        final T reference;
        final int stamp;
        private Pair(T reference, int stamp) {
            this.reference = reference;
            this.stamp = stamp;
        }
        static <T> Pair<T> of(T reference, int stamp) {
            return new Pair<T>(reference, stamp);
        }
    }


 AtomicStampedReference:可以避免ABA问题，内部封装了引用和stamp版本控制。
 */
public class AtomicStampedReferenceTest {
    public void test() {
        fun();
    }

    private void fun() {
        Student student1 = new Student();
        student1.setName("name1");
        Student student2 = new Student();
        student2.setName("name2");
        //引用student1
        AtomicStampedReference<Student> atomicStampedReference = new AtomicStampedReference<>(student1, 1);

        int stamp = atomicStampedReference.getStamp();
        //引用student2
        atomicStampedReference.compareAndSet(student1, student2, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
        stamp = atomicStampedReference.getStamp();
        //引用student2 无法交换，和student1不相等
        atomicStampedReference.compareAndSet(student1, student2, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
        stamp = atomicStampedReference.getStamp();
        int m = 0;

    }

}
