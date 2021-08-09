package Test.test2021;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreTest {
    public void  test()
    {

    }

    private void fun1 () throws InterruptedException {
        Semaphore semaphore=new Semaphore(1);
        semaphore.tryAcquire(500, TimeUnit.MICROSECONDS);
    }
}
