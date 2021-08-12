package Test.test2021;


import java.util.concurrent.CompletableFuture;

/*
不能跨线程捕获异常
 */
public class ThreadExceptionTest {
    public void test() {
        fun1();
    }

    private void fun1() {

        try {
            CompletableFuture.runAsync(() ->
            {
                //必须在线程内部进行异常处理，无法抛出到外边的另外一个线程。和C#一样
                Integer m = Integer.parseInt("m");
//                try {
//                    Integer m = Integer.parseInt("m");
//                } catch (Exception e) {
//                   System.out.println("Thead 内部:"+e.getMessage());
//                }
            });
        } catch (Exception e) {
            //外层捕获，没有进入catch
            System.out.println("Thead 外部:"+e.getMessage());
            int m = 0;
        }
        System.out.println("Completed");
    }

}
