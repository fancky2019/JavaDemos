package Test.test2021;

import java.math.BigInteger;

/**
 * @Auther fancky
 * @Date 2020-9-24 17:24
 * @Description
 */
public class BigInterTest {
    public void test() {
        fun();
    }

    private void fun() {
        BigInteger bigInteger = new BigInteger("0");
        BigInteger bigInteger1 = new BigInteger("1");
        BigInteger bigInteger2 = new BigInteger("2");
        //add内返回一个新的对象，不是改变bigInteger
        bigInteger = bigInteger.add(bigInteger1).add(bigInteger2);
        String re = bigInteger.toString();
        int m = 0;
    }
}
