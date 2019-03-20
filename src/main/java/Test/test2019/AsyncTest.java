package Test.test2019;

import common.Action;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AsyncTest {
    public void test() {
        asyncFun(() -> asyncCallBack());
    }

    private void asyncFun(Action action) {
        try {


//            Integer m = CompletableFuture.supplyAsync(() ->
//            {
//                asyncCallBack();
//                return 1;
//            }).get();
            CompletableFuture.runAsync(()->
            {
                asyncCallBack();
            });

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void asyncCallBack() {
        CompletableFuture.runAsync(() ->
        {
            System.out.println("异步方法完成！");
        });
    }

}
