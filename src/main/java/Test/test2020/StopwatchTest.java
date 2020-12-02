package Test.test2020;

import com.google.common.base.Stopwatch;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

//JDK9
//   一：Thread.Sleep(1000);
//  Thread.Sleep()方法：是强制放弃CPU的时间片，然后重新和其他线程一起参与CPU的竞争。
//  二：Thread.SpinWait(1000);//JDK9
//    Thread.SpinWait()方法：只是让CPU去执行一段没有用的代码。当时间结束之后能马上继续执行，而不是重新参与CPU的竞争。
//                用Sleep()方法是会让线程放弃CPU的使用权。
//                用SpinWait()方法是不会放弃CPU的使用权。


public class StopwatchTest {
    public void test() {
        fun();
    }

    private void fun() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            Thread.sleep(100);
        } catch (Exception ex) {

        }
        stopwatch.stop();
        //99 milliSeconds
        System.out.println(MessageFormat.format("{0} milliSeconds", stopwatch.elapsed(TimeUnit.MILLISECONDS)));
        stopwatch.reset();//不重置时间会累加。
        stopwatch.start();
        try {
            Thread.sleep(100);
            //   一：Thread.Sleep(1000);
            //  Thread.Sleep()方法：是强制放弃CPU的时间片，然后重新和其他线程一起参与CPU的竞争。
            //    二：Thread.SpinWait(1000);
            //    Thread.SpinWait()方法：只是让CPU去执行一段没有用的代码。当时间结束之后能马上继续执行，而不是重新参与CPU的竞争。
//                用Sleep()方法是会让线程放弃CPU的使用权。
//                用SpinWait()方法是不会放弃CPU的使用权。
        } catch (Exception ex) {

        }
        stopwatch.stop();
        //199 milliSeconds:没有重置接着从第一次start()的时候计时
        System.out.println(MessageFormat.format("{0} milliSeconds", stopwatch.elapsed(TimeUnit.MILLISECONDS)));

        //reset():从当前时间计时
        stopwatch.reset();
        stopwatch.start();
        try {
            Thread.sleep(100);
        } catch (Exception ex) {

        }
        stopwatch.stop();
        //99 milliSeconds
        System.out.println(MessageFormat.format("{0} milliSeconds", stopwatch.elapsed(TimeUnit.MILLISECONDS)));

    }
}
