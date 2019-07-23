package Test.test2019;

/*
十进制转二进制：除2取余，逆序排列
二进制数使用补码表示，最高位为符号位，正数的符号位为0，负数为1。
原码：除2取余，逆序排列
反码：原码按位取反
补码：反码+1
 */
public class LeftMoveTest {
    public void test() {
        move();
    }

    private void move() {
        Integer num = 3;
        //位运算：将数字转化为二进制后，进行运算
        //左移:往左边移动n位，相当于乘以2的n次方
        Double pow = Math.pow(2, 3);
        Integer re = num << 3;//24=3*Math.pow(2,3);

        //右移:往右边移动n位，若值为正，则在高位插入0；若值为负，则在高位插入1。
        Integer rre = num >> 4;
        Integer r1 = 8 >> 2;
        Integer r2 = -8 >> 2;

        String str = Integer.toBinaryString(-6);

        Integer m = 0;
    }
}
