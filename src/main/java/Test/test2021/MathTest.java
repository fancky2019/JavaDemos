package Test.test2021;

import com.sun.javafx.logging.PulseLogger;
import scala.Byte;

public class MathTest {
    public void test() {
        fun();
    }

    private void fun() {
        //（1）ceil：向上取整； 向上的下一个整数
        double ceil1 = Math.ceil(11.3); //= 12;
        double ceil2 = Math.ceil(-11.3);// = 11;
        double ceil22 = Math.ceil(-1.5);// = -1;
        //（2）floor：向下取整；向下的下一个整数
        double floor1 = Math.floor(11.3);//= 11;
        double floor2 = Math.floor(-11.3);//= -12;
        double floor22 = Math.floor(-1.5);//= -2;
        //（3）round：四舍五入；相当于加0.5然后向下取整。
        double round1 = Math.round(11.3);// = 11;
        double round2 = Math.round(11.8);//= 12;
        double round3 = Math.round(-11.3);//= -11;
        double round4 = Math.round(-11.8);//= -12;
        double round44 = Math.round(-11.5);//= -11;
        double round5 = Math.round(-1.5);//= -1;
        double round55 = Math.round(1.5);//= 2;

        //例如10000000，最高位是1，是负数，①对各位取反得01111111，转换为十进制就是127，加上负号得-127，再减去1得-128；
        byte by2=(byte)( (byte) 127+(byte)1);//-128
        int m = 0;
    }

    private void funThrows() throws Exception {
        Thread.sleep(1);
        throw new Exception("sdsd");

    }
}
