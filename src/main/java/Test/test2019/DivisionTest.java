package Test.test2019;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 浮点数不能用二进制精确表示
 */
public class DivisionTest {
    public void test() {
        division();
        multiplicative();
        additive();
        subtraction();
        bigDecimal();
    }

    private void division() {
        Integer r = 3 / 2;//1
        Integer mod = 19 % 4;//3
        Double d1 = 3d / 2d;//1.5

        Double re = 0.3 / 0.1;//2.9999999999999996
        Float f = 0.3f / 0.2f;//3.0
        if ((0.3 / 0.1) == 3) {//false
            Integer m = 0;
        } else {
            Integer m = 0;
        }

    }

    void multiplicative() {
        Double multi = 0.3 * 0.2;//0.6
        Double multi1 = 0.3d * 0.2d;//0.6
        Float fl1 = 0.3f * 0.2f;//0.060000002
        Float fl2 = 0.3F * 0.2F;//0.060000002


        Double d1 = 0.3;
        Double d2 = 0.2;
        Double rd = d1 * d2;//0.06
        Double d11 = 0.3d;
        Double d22 = 0.2d;
        Double rd1 = d11 * d22;//0.06

        Float f1 = 0.3f;
        Float f2 = 0.2f;
        Float rf = f1 * f2;//0.060000002
        Integer m = 0;
    }

    void additive() {
        Float f1 = 0.3f + 0.2f;//0.5
        Double d1 = 0.3 + 0.2;//0.5
        Double dd2 = 0.05 + 0.01;//0.060000000000000005
        double d2 = 0.05 + 0.01;//0.060000000000000005
        Integer m = 0;
    }

    void subtraction() {
        Float f1 = 0.3f - 0.2f;//0.10000001
        Double d1 = 0.3 - 0.2;//0.09999999999999998
        Double dd2 = 0.05 - 0.01;//0.04
        double d2 = 0.05 - 0.01;//0.04
        Integer m = 0;

    }

    void bigDecimal() {
        //注意构造BigDecimal要用字符串传参
        BigDecimal bd1 = new BigDecimal("0.03");
        BigDecimal bd2 = new BigDecimal("0.02");

//        Double d1=0.03;
//        Double d2=0.02;
//        BigDecimal bd1=new BigDecimal(d1.toString());
//        BigDecimal bd2=new BigDecimal(d2.toString());

        //不要用这种，用字符串构造
//        BigDecimal bd1 = new BigDecimal(0.03);
//        BigDecimal bd2 = new BigDecimal(0.02);

        BigDecimal rbd1 = bd1.multiply(bd2);//0.0006
        BigDecimal rbd2 = bd1.divide(bd2);//1.5
        BigDecimal rbd3 = bd1.add(bd2);//0.05
        BigDecimal rbd4 = bd1.subtract(bd2);//0.01
        Integer m = 0;
    }
}
