package Test.test2019;

import java.text.MessageFormat;

/**
 * = =:对于预定义的值类型，如果操作数的值相等，则相等运算符 (==) 返回 true，否则返回 false。
 * 对于引用类型，如果两个操作数引用同一个对象，则 == 返回 true。
 * String 类型，== 同一个对象，注意内存驻留。
 * equals:number类型值一样，String逐个字符比较是否一样，具体看String类的equals方法
 */
public class StringTest {
    public void test() {
        //  format();
          stringIntern();
        String str1 = StringTest.trimEnd("sd..", '.');
        String str2 = StringTest.trimStart(".sd..", '.');
        //和C#trim()一样
        String str3 = " add .".trim();
        //双引号转义：\"
        String str4 = "d'd'sdsd\"ds";
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


}
