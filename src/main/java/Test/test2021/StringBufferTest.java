package Test.test2021;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/*
1、StringBuffer 与 StringBuilder 中的方法和功能完全是等价的，
2、只是StringBuffer 中的方法大都采用了 synchronized 关键字进行修饰，因此是线程安全的，
而 StringBuilder 没有这个修饰，可以被认为是线程不安全的。
3、在单线程程序下，StringBuilder效率更快，因为它不需要加锁，不具备多线程安全
而StringBuffer则每次都需要判断锁，效率相对更低
 */
public class StringBufferTest {
    public void test() {
        fun();
    }

    private void fun() {
        StringBuffer stringBuffer = new StringBuffer();
        //每次append 会新扩容添加的字符串长度的容量
        stringBuffer.append("abc");
        stringBuffer.append("cde");
        String str = stringBuffer.toString();
        int m = 0;

    }
}
