package Test.test2019;

import Model.Student;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * jdk8中字符串常量池存放在堆中
 * 字面量(interned strings)转移到了java heap；类的静态变量(class statics)转移到了java heap。
 * <p>
 * jdk1.7的HotSpot中，已经把原本放在方法区中的静态变量、字符串常量池等移到堆内存中。字符串常量和静态类型变量存储在普通的堆区中。
 * 在jdk1.8中，永久代已经不存在，存储的类信息、编译后的代码数据等已经移动到了元空间(MetaSpace 堆外内存--本地内存)中，元空间并没有处于堆内存上，
 * 而是直接占用的本地内存(NativeMemory)。
 * <p>
 * 元空间：类、字段、方法的修饰信息，常量也存储在元空间。
 * <p>
 * 方法区：永久代和元空间是方法区的一种实现。
 * <p>
 * <p>
 * 变量存储：
 * 类常量和静态变量：元空间(方法区的一种实现)
 * 除静态变量之外的类变量：类变量只有在对象创建之后才分配内存，所以基本类型的话在堆中的对象中，引用类型的话在堆中、堆中的对象保存引用
 * 局部变量：局部变量也是只有对象才有的，但是它在方法中，所以其实它是在栈上的：如果是基本类型，就保存在栈上；如果是引用类型，就保存在堆上，然后在栈中保存一个引用
 * <p>
 * <p>
 * <p>
 * = =:对于预定义的值类型，如果操作数的值相等，则相等运算符 (==) 返回 true，否则返回 false。
 * 对于引用类型，如果两个操作数引用同一个对象，则 == 返回 true。
 * String 类型，== 同一个对象，注意内存驻留。
 * equals:number类型值一样，String逐个字符比较是否一样，具体看String类的equals方法
 */
public class StringTest {
    public void test() {
//        static  int a; static不能用在方法内部
        concat();


        String str = "sdsd(%)";
        String newStr = remove(str);
        //  format();
        stringIntern();
        String str1 = StringTest.trimEnd("sd..", '.');
        String str2 = StringTest.trimStart(".sd..", '.');
        //和C#trim()一样
        String str3 = " add .".trim();
        //双引号转义：\"
        String str4 = "d'd'sdsd\"ds";

        String name = "fancky";
        Student student = new Student();
        student.setName(name);
        parametersFunction("name", student);
        System.out.println(name);
    }

    private String remove(String str) {
        List<String> list = new ArrayList<String>();
        list.add("(");
        list.add(")");
        list.add("%");

        for (String character : list) {
            str=   str.replace(character, "");
        }
        return str;
    }

    private void concat() {

        int m = 1;
        //可以直接转成str，类似javascript
        String str = m + "";

        char ch = 'b';
        int chi = ch - 1;//b 转ascii 码 98-1=97(a)

        //减不能成立
//        String sub="A"-1;
        //不能转数字
        // Integer num="1"+2;
        int n = 0;
    }

    private void format() {
        Integer m = 5;
        String str = "12";
        String format1 = MessageFormat.format("m={0},str={1}", m, str);
        String format2 = String.format("m=%d,str=%s", m, str);
        Integer mr = 0;
    }

    /*
    字面量字符串和字符串常量会被驻留。
    All literal strings and string-valued constant expressions are interned.
    Intern（）：从驻留池中取当前值的字符串。如果池中不存在，当前对象会加入
                池中并返回当前对象的引用。



        * When the intern method is invoked, if the pool already contains a
         * string equal to this {@code String} object as determined by
         * the {@link #equals(Object)} method, then the string from the pool is
         * returned. Otherwise, this {@code String} object is added to the
         * pool and a reference to this {@code String} object is returned.
     */
    private void stringIntern() {
        String s1 = "abc";
        String s2 = new String("abc");//new  一个新的实例
        Boolean r1 = s1 == s2;//false
        Boolean e1 = s1.equals(s2);//true

        String s3 = "abc";//驻留池里取
        Boolean r2 = s1 == s3;//true

        String s4 = new String("abc").intern();//从驻留池里取
        Boolean r3 = s1 == s3;//true

        String s5 = new String(s1).intern();
        Boolean r4 = s1 == s5;//true

        String s8 = new StringBuilder("abc").toString();
        Boolean r7 = s1 == s8;//false

        String s6 = new StringBuilder("abc").toString().intern();
        Boolean r5 = s1 == s5;//true

        String s7 = new StringBuilder(s1).toString().intern();
        Boolean r6 = s1 == s6;//true


        Integer m = 0;
    }

    public static String trimEnd(String str, Character character) {
        return str.replaceAll("[" + character + "]+$", "");
    }

    public static String trimStart(String str, Character character) {
        return str.replaceAll("^[" + character + "]+", "");
    }

    public void plus() {
        //可以字符串和字符直接相加
        String str1 = "c" + 'd';

        //不能加字符数组
        String str = "c" + new Character[]{'a', 'b'};

        Character[] characters = new Character[3];
        characters[0] = 'a';
        characters[1] = 'b';
        characters[2] = 'c';
        String str2 = "c" + characters;
        Integer m = 0;
    }

    /*
         String 作为参数(形参)，不改变原参数值（实参），
         String以外 引用类型作为参数（形参），会改变原参数值（实参）。

         基本类型按值传递;传值
        string 以为 引用类型按引用传递：传地址
        string 可以理解按值传递
        */
    private void parametersFunction(String name, Student student) {
        //字符串直接赋值和用new出的对象赋值的区别仅仅在于存储方式不同。
        name = "rui";//name形参副本指向新的地址，name实参指向的地址没变
        name = new String("r");

        student.setName("rui");
    }

    private void join() {
        String[] names = {"Bob", "Alice", "Grace"};
        String joinStr = String.join(",", names);
        StringJoiner stringJoiner = new StringJoiner(",", "(", ")");

        for (String name : names) {
            stringJoiner.add(name);
        }

        String str = stringJoiner.toString();
        int m = 0;
    }


}
