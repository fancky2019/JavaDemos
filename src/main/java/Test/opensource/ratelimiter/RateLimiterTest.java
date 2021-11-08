package Test.opensource.ratelimiter;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.curator.shaded.com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/*
<!-- https://mvnrepository.com/artifact/com.google.guava/guava -->
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>27.0.1-jre</version>
</dependency>
 */
public class RateLimiterTest {
    public void test()
    {

    }
    private RateLimiter rateLimiter = RateLimiter.create(1, 2, TimeUnit.SECONDS);
    private void fun()
    {
        if(rateLimiter.tryAcquire(5,2,TimeUnit.SECONDS))
        {

        }
    }
}
