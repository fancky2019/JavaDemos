package Test.test2019;

import common.Action;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
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

            //有返回值
            //     CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() ->
            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() ->
            {
                try {
                    Thread.sleep(10 * 1000);

                    return 1;
                } catch (Exception ex) {
                    return 0;
                }

            }).thenApply(s ->
            {
               //Do other work()
                return s+2;
            });

            CompletableFuture.runAsync(()->
            {
                asyncCallBack();
            });

            //completableFuture.get(10, TimeUnit.SECONDS);
            completableFuture.get();
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
