package Test.test2018;

import Model.Student;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
字符串:不改变引用字符串的值。
简单对象：方法内可重新实例化，返回不可指向新的内存地址。
数组对象:可改变元素属性值、元素指向的地址，不可改变数组大小、重新实例化。
集合对象:可改变元素属性、元素指向的地址、添加新的元素（改变元素个数），不可重新实例化。
 */
public class ReferenceTest {

    public void test() {
        fun1();
//        strongReference();
//        softReference();
//        weakReference();
//        phantomReference();
//        setNUll();
    }

    //region对象、数组、list调用
    private void fun1() {
        String str1 = "123";
        String str2 = "456";
        stringReference(str1);
        Student student1 = new Student();
        student1.setName("li");
        Student student11 = new Student();
        student11.setName("li");

        simpleObject1(student1);
        simpleObject1(student11);
        simpleObject11(student1);
        Student student2 = new Student();
        student2.setName("li");
        Student[] array2 = new Student[1];
        array2[0] = student2;

        Student student22 = new Student();
        student22.setName("li");
        Student[] array22 = new Student[1];
        array22[0] = student22;


        Student student222 = new Student();
        student222.setName("li");
        Student[] array222 = new Student[]{student222};


        Student student2222 = new Student();
        student2222.setName("li");
        Student[] array2222 = {student2222};

        arrayReference1(array2);

        arrayReference3(array222);
        arrayReference4(array2222);


        Student student3 = new Student();
        student3.setName("li");
        List<Student> list1 = new ArrayList<>();
        list1.add(student3);

//        List<Student> list2=new ArrayList<>();
//        list2.addAll(list1);

        Student student33 = new Student();
        student33.setName("li");
        List<Student> list2 = new ArrayList<>();
        list2.add(student33);

        Student student333 = new Student();
        student333.setName("li");
        List<Student> list3 = new ArrayList<>();
        list3.add(student333);

        Student student3333 = new Student();
        student3333.setName("li");
        List<Student> list4 = new ArrayList<>();
        list4.add(student3333);

        listReference1(list1);
        listReference2(list2);
        listReference3(list3);
        listReference4(list4);
        Integer m = 0;
    }


    private void stringReference(String str) {
        str = "abc";
    }

    /**
     * 可改变属性值
     *
     * @param student
     */
    private void simpleObject1(Student student) {
        student.setName("可改变属性值");
    }

    /**
     * 不可指向新的内存地址
     *
     * @param student
     */
    private void simpleObject11(Student student) {
        student = new Student();
        student.setName("方法内可重新实例化，返回不可指向新的内存地址");
        student.setAge(27);
        int m=0;
    }

    /**
     * '
     * 可改变元素的属性值
     *
     * @param array
     */
    private void arrayReference1(Student[] array) {
        Student student = array[0];
        student.setName("fancky");
    }


    /**
     * 元素可指向新的内存地址
     *
     * @param array
     */
    private void arrayReference3(Student[] array) {
        array[0] = new Student();
        array[0].setName("fancky");
    }

    /**
     * 数组不能重新实例化
     *
     * @param array
     */
    private void arrayReference4(Student[] array) {
        array = new Student[2];
        Student student2 = new Student();
        student2.setName("fancky1");
        array[0] = student2;
        Student student22 = new Student();
        student2.setName("fancky2");
        array[1] = student2;
    }

    /**
     * 可改变元素的属性值
     *
     * @param list
     */
    private void listReference1(List<Student> list) {
        Student student = list.get(0);
        student.setName("fancky");
    }

    /**
     * 元素可指向新的地址
     *
     * @param list
     */
    private void listReference2(List<Student> list) {
        Student student = new Student();
        student.setName("fancky");
        list.set(0, student);
    }

    /**
     * 可添加新的元素（扩容）
     *
     * @param list
     */
    private void listReference3(List<Student> list) {
        Student student = new Student();
        student.setName("fancky");
        list.add(student);
    }

    /**
     * list不可重新实例化
     *
     * @param list
     */
    private void listReference4(List<Student> list) {
        list = new ArrayList<>();
        Student student = new Student();
        student.setName("fancky");
        list.add(student);
    }
    //endregion

    //region 四中引用类型
    /*
    垃圾回收发生在堆上。
    强引用:JVM在内存不足的情况下，抛出outOfMemoryError错误，不会回收此类对象
    软引用:JVM只会在内存不足的情况下回收该对象
    弱引用:如果某个对象与弱引用关联，那么当JVM在进行垃圾回收时，无论内存是否充足，都会回收此类对象。
    虚引用:若某个对象与虚引用关联，那么在任何时候都可能被JVM回收掉。虚引用不能单独使用，必须配合引用队列一起使用。
     */

    //region强引用:JVM在内存不足的情况下，抛出outOfMemoryError错误，不会回收此类对象
    private void strongReference() {
        Object object = new Object();
        String str = "hello";//字符串驻留
        Student student1 = new Student();
        student1.setName("li");
        student1=null;//将对象设置为null，没有引用，gc回收的时候就会清理
        String strongReference = new String("abc");
    }
    //endregion

    //region软引用:JVM只会在内存不足的情况下回收该对象
    private void softReference() {
        SoftReference<String> softReference = new SoftReference<String>(new String("abc"));
        String beforeGc = softReference.get();//abc
        //通知JVM进行内存回收
        System.gc();//GC被标记，具体有没有执行未知
        String afterGc = softReference.get();//abc
        System.out.println("SoftReference afterGc:"+ afterGc);//null  就打印出null
        int m = 0;
    }
    //endregion

    //region弱引用:如果某个对象与弱引用关联，那么当JVM在进行垃圾回收时，无论内存是否充足，都会回收此类对象。
    private void weakReference() {
        Student student= new Student();
        student.setName("li");
        WeakReference<Student> weakReference = new WeakReference<Student>(student);
        Student beforeGc = weakReference.get();//abc
        //通知JVM进行内存回收;调用GC不会立即执行，只是标记要GC。跟C#一样
        System.gc();//GC被标记，具体有没有执行未知
        System.runFinalization();
        try {
            Thread.sleep(100);
        }
        catch (Exception ex)
        {

        }
        Student afterGc = weakReference.get();//GC被标记，具体有没有执行未知
        System.out.println("WeakReference afterGc:"+ afterGc);//null  就打印出null
        int m = 0;
    }
    //endregion

    //region虚引用:若某个对象与虚引用关联，那么在任何时候都可能被JVM回收掉。虚引用不能单独使用，必须配合引用队列一起使用。
    private void phantomReference() {
        ReferenceQueue<String> queue = new ReferenceQueue<>();
        PhantomReference<String> phantomReference = new PhantomReference<String>("ghi", queue);
        String str = phantomReference.get();//null
        System.out.println("PhantomReference afterGc:"+ str);//null  就打印出null
        int m = 0;
    }
    //endregion

    //endregion

    private void setNUll()
    {
        Student student=new Student();
        student.setName("name");
        student.setAge(27);

        Student student1=student;
        Student student2=student;
        Student student3=student;
        student3.setAge(28);//改变引用的值
        student2=null;//student2不指向任何引用，等待gc。
        student1.setAge(23);
        int m=0;
    }
}
