package Test.test2019;

import java.io.*;

/*
try-with-resources  语句释放资源的类必须实现AutoCloseable 接口。
 */
public class TryTest {
    public void test() {

    }

    private void tryFun() {
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
