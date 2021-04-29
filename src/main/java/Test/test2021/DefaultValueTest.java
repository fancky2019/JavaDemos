package Test.test2021;

public class DefaultValueTest {
    public void test() {
        fun();
    }

    private void fun() {
        try {
            //装箱类型默认值为null
            Boolean bl1 = null;//null
            Boolean bi2;//变量未初始化赋值，不能使用
            if (bl1) {
                int nn = 1;
            }
            bi2 = true;
            if (bi2) {
                int nn = 1;
            }
            boolean bl3;
            int m = 0;
        } catch (Exception ex) {
            int n = 0;
        }

    }
}
