package Test.test2021;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CompletableFutureTest {
    public void test() {
        fun();
    }

    private void fun() {
        //CompletableFuture 内部默认最多启动处理器个数 -1 个线程执行任务。
        //ThreadPoolExecutor
//        ExecutorService service = Executors.newCachedThreadPool();

        try {


            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() ->
            {
                //可以把whenComplete放在里面
                return 3;
            });

            int re = completableFuture.get();

            CompletableFuture.runAsync(() ->
            {

            }).whenComplete((a, b) ->
            {
                Object aa = a;
                Object bb = b;
            });

            try {


                BiConsumer<String, Throwable> biConsumer = (a, b) ->
                {
                    int result = Integer.parseInt(a);
                };
                // Exception ex = new Exception("sddssd");
                //跟C#不一样biConsumer（3,4），java 函数接口只是一个函数接口（可能有多个方法）要指定调用方法
                //   biConsumer.accept("sd", ex);


                CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(() ->
                {
                    return "t";
                });

                completableFuture1.whenComplete(biConsumer);

                completableFuture1.exceptionally(ex ->
                {
                    String str=ex.getMessage();
                    return "";
                });
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }


            try {

                Consumer<String> consumer = a ->
                {
                    int result = Integer.parseInt(a);
                };
                //跟C#不一样biConsumer（3,4），java 函数接口只是一个函数接口（可能有多个方法）要指定调用方法
                consumer.accept("m");

            } catch (Exception ex) {
                System.out.println(ex.toString());
            }


            CompletableFuture.supplyAsync(() ->
            {
                return 3;
            }).whenComplete((a, b) ->    //a=3,b=null
            {
                Object aa = a;
                Object bb = b;
            });





            //thenAccept 返回 CompletableFuture<Void>
            CompletableFuture<Void> completableFuture1 = CompletableFuture.supplyAsync(() ->
            {
                return "t";
            }).thenAccept(System.out::println);

        } catch (Exception ex) {

        }
    }
}
