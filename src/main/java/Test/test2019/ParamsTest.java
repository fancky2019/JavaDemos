package Test.test2019;

/**
 * 变长参数
 */
public class ParamsTest {
    public void test() {
        fun();//intArray不为null,长度为0，
        fun(1, 2);
        int[] arr = {3, 4};
        fun(arr);

    }

    private void fun(int... intArray) {
        if (intArray != null) {
            int m = intArray.length;
        }

    }
}
