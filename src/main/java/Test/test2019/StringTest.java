package Test.test2019;

import java.text.MessageFormat;
/**
 *
 *     = =:对于预定义的值类型，如果操作数的值相等，则相等运算符 (==) 返回 true，否则返回 false。
 *         对于引用类型，如果两个操作数引用同一个对象，则 == 返回 true。
 *         String 类型，== 同一个对象，注意内存驻留。
 *  equals:number类型值一样，String逐个字符比较是否一样，具体看String类的equals方法
 */
public class StringTest {
    public void test() {
      //  format();
        stringIntern();
    }

    private void format() {
        Integer m = 5;
        String str = "12";
        String format1 = MessageFormat.format("m={0},str={1}", m, str);
        String format2 = String.format("m=%d,str=%s", m, str);
        Integer mr=0;
    }

    private  void  stringIntern()
    {
        String s1="abc";
        String s2=new String("abc");//new  一个新的实例
        Boolean r1=s1==s2;//false
        Boolean e1=s1.equals(s2);//true

        String s3="abc";//驻留池里取
        Boolean r2=s1==s3;//true

        String s4=new String("abc").intern();//从驻留池里取
        Boolean r3=s1==s3;//true

        String s5=new String(s1).intern();
        Boolean r4=s1==s5;

        String s8=new StringBuilder("abc").toString();
        Boolean r7=s1==s8;//false

        String s6=new StringBuilder("abc").toString().intern();
        Boolean r5=s1==s5;

        String s7=new StringBuilder(s1).toString().intern();
        Boolean r6=s1==s6;


        Integer m=0;
    }

}
