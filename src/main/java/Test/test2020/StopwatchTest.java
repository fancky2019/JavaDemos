package Test.test2020;

import com.google.common.base.Stopwatch;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public class StopwatchTest {
    public void test()
    {
        fun();
    }
    private  void  fun()
    {
        Stopwatch stopwatch=Stopwatch.createStarted();
        try {
            Thread.sleep(100);
        }
        catch (Exception ex)
        {

        }
        stopwatch.stop();
        //99 milliSeconds
        System.out.println(MessageFormat.format("{0} milliSeconds",stopwatch.elapsed(TimeUnit.MILLISECONDS)));

        stopwatch.start();
        try {
            Thread.sleep(100);
        }
        catch (Exception ex)
        {

        }
        stopwatch.stop();
        //199 milliSeconds:没有重置接着从第一次start()的时候计时
        System.out.println(MessageFormat.format("{0} milliSeconds",stopwatch.elapsed(TimeUnit.MILLISECONDS)));

        //reset():从当前时间计时
        stopwatch.reset();
        stopwatch.start();
        try {
            Thread.sleep(100);
        }
        catch (Exception ex)
        {

        }
        stopwatch.stop();
        //99 milliSeconds
        System.out.println(MessageFormat.format("{0} milliSeconds",stopwatch.elapsed(TimeUnit.MILLISECONDS)));

    }
}
