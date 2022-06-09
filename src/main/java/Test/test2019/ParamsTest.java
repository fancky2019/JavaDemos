package Test.test2019;

/**
 * 变长参数
 *
 *
 *      * 变长参数必须是最后一个参数，只能有一个变长参数：Vararg parameter must be the last in the list
 *      * 不传参数，数组是个空数组，长度为0，但是不是null。
 *      * 可以传单个元素，或者数组，或者逗号隔开的n个元素
 *
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
