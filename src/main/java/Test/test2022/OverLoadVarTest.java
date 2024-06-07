package Test.test2022;

/**
优先调用重载
 */
public class OverLoadVarTest {
    public void test() {
        fun(1, 3);//1
        fun(1, 2,3);//2
//        fun(1, 2,3,4,"d");//1
    }

    private void fun(int a, int b) {
        System.out.println("1");
    }

    private void fun(int a, int... c) {
        int[] arr=c;
        if(c.length>0)
        {

        }
        System.out.println("2");
    }

    //Object... 和 int... 同时候存在，会存在方向指向不明问题。
//    private void fun(int a, Object... d) {
//        System.out.println("3");
//    }
}
