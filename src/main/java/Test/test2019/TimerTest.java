package Test.test2019;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TimerTest {
    public void test() {

        schedule();
    }

    private void schedule() {
        //Timer
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("TimerTask");
                //doWork()
            }
        }, 0, 3 * 1000);


        //newScheduledThreadPool
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() ->
        {
            System.out.println("scheduleAtFixedRate");
            //doWork()
        }, 0, 2, TimeUnit.SECONDS);


    }


}
