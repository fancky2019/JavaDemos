package Test.test2019;

import java.nio.charset.Charset;

public class CharTest {
    public void test() {
        fun();
    }

    private void fun() {
        try {


            //获取系统默认编码
            String name = System.getProperty("file.encoding");//UTF-8

            //获取系统默认的字符编码
            Charset defaultCharset = Charset.defaultCharset();//UTF-8

            Character ch1 = '1';
            Character ch2 = 'a';
            Character ch3 = '李';

            Charset charset1 = Charset.forName("UTF-8");
            //即 UTF-16BE 和 UTF-16LE
            Charset charset21 = Charset.forName("UTF-16BE");
            Charset charset22 = Charset.forName("UTF-16LE");
            Charset charset23 = Charset.forName("UTF-16");
            //UTF-16
            Charset charset3 = Charset.forName("Unicode");


            Integer length1 = ch1.toString().getBytes(charset1).length;//1
            Integer length2 = ch2.toString().getBytes(charset1).length;//1
            Integer length3 = ch3.toString().getBytes(charset1).length;//3

            Integer length11 = ch1.toString().getBytes(charset21).length;//2
            Integer length22 = ch2.toString().getBytes(charset21).length;//2
            Integer length33 = ch3.toString().getBytes(charset21).length;//2

            Integer length111 = ch1.toString().getBytes(charset22).length;//2
            Integer length222 = ch2.toString().getBytes(charset22).length;//2
            Integer length333 = ch3.toString().getBytes(charset22).length;//2

            Integer length1111 = ch1.toString().getBytes(charset23).length;//4
            Integer length2222 = ch2.toString().getBytes(charset23).length;//4
            Integer length3333 = ch3.toString().getBytes(charset23).length;//4

            Integer len31 = ch1.toString().getBytes(charset3).length;//4
            Integer len32 = ch2.toString().getBytes(charset3).length;//4
            Integer len33 = ch3.toString().getBytes(charset3).length;//4

            Integer m = 0;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }
}
