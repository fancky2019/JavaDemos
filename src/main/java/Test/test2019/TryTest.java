package Test.test2019;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/*
try-with-resources  语句释放资源的类必须实现AutoCloseable 接口。

Closeable extends AutoCloseable
 */
public class TryTest implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void close() throws Exception {
        System.out.println("call  TryTest close()");
    }

    public void test() {
        tryWithResources();
    }

    /*
    AutoCloseable 的close()在catch  finally之前执行。如果不处理异常，就将异常抛出
    执行顺序：close()-->catch-->finally
     */
//    private void tryWithResources() throws Exception  {
    private void tryWithResources() {
        //多个语句用分号分开，实现AutoCloseable接口的类必须在try后的括号内赋值
        try (TryTest tryTest = new TryTest()) {
            int m = Integer.parseInt("n");

        } catch (Exception ex) {
            // 如果不抛出异常，catch 块一定要加，就相当于 try catch  try  后添加要关闭的对象
            //在调用close() 后执行 catch
            System.out.println("tryWithResources catch");
            LOGGER.error("", ex);
        }
//        finally {
//            //可不用finally,finally 在调用 close() 方法后执行
//            System.out.println("tryWithResources finally");
//        }

//        多个语句用分号分开
        try (InputStream inputStream = new FileInputStream("");
             OutputStream outputStream = new FileOutputStream("")) {


        } catch (Exception ex) {

        }
    }


    private void tryCatch() {
        try {

            //像C#嵌套using 一样
            try (InputStream inputStream = new FileInputStream("")) {
                try (OutputStream outputStream = new FileOutputStream("")) {

                }
            }

            //多个语句写在一个try括号内，用分号隔开
            try (InputStream inputStream = new FileInputStream("");
                 OutputStream outputStream = new FileOutputStream("")) {

            }

        } catch (IOException io) {

        } catch (Exception ex) {

        } finally {

        }
    }

}
