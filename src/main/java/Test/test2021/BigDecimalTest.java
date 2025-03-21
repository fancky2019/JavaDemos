package Test.test2021;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BigDecimalTest {
    public void test() {
        foramt();
        operation();

    }

    private void foramt() {

       //推荐使用它的BigDecimal(String val)构造方法或者 BigDecimal.valueOf(double val) 静态方法来创建对象。
        BigDecimal a = new BigDecimal("1.0");
        BigDecimal b = BigDecimal.valueOf(1);

        BigDecimal bigDecimal = new BigDecimal(1.2366);
        //保留两位小数
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        BigDecimal bigDecimal2 = new BigDecimal(2.2366);
        //除的时候保留4位小数
        BigDecimal shouldCompletePercent = bigDecimal.divide(bigDecimal2, 4, RoundingMode.HALF_UP);

        //转百分数
        DecimalFormat decimalFormat = new DecimalFormat("0.00%");
        //55.44%
        String percentStr = decimalFormat.format(shouldCompletePercent);
        Integer m = 0;
    }

    /**
     * 原值不变，返回操作后的值
     */
    private void operation() {
        BigDecimal bigDecimal1 = new BigDecimal(10);
        BigDecimal bigDecimal2 = new BigDecimal(2);

        //加
        BigDecimal add = bigDecimal1.add(bigDecimal2);
        //减
        BigDecimal subtract = bigDecimal1.subtract(bigDecimal2);
        //乘
        BigDecimal multiply = bigDecimal1.multiply(bigDecimal2);
        //除
        BigDecimal divide = bigDecimal1.divide(bigDecimal2, 4, RoundingMode.HALF_UP);
        //判断是否==0
        Boolean result = bigDecimal1.compareTo(BigDecimal.ZERO) == 0;
        Integer m = 0;
    }
}
