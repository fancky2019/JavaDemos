package Test.opensource.ratelimiter;

import org.apache.curator.shaded.com.google.common.util.concurrent.RateLimiter;
import org.apache.log4j.helpers.DateTimeDateFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public void test() {
        fun();
    }

    //warmupPeriod:在预热期内达到每秒生产permitsPerSecond。相当于第一个permitsPerSecond实际用warmupPeriod时间，之后每秒生产permitsPerSecond
    private RateLimiter rateLimiter1 = RateLimiter.create(5, 2, TimeUnit.SECONDS);
    //每秒恒定生产permitsPerSecond。没有预热的概念
    private RateLimiter rateLimiter = RateLimiter.create(5);

    private void fun() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        while (true) {

//            if (rateLimiter.tryAcquire(1, 1, TimeUnit.SECONDS)) {
//                System.out.println(LocalDateTime.now().format(dateTimeFormatter));
//            }


            //阻塞获取
//            rateLimiter.acquire(1);
//            System.out.println(LocalDateTime.now().format(dateTimeFormatter));


            //设置1秒超时获取令牌
            if (rateLimiter.tryAcquire(1, 1, TimeUnit.SECONDS)) {
                System.out.println(LocalDateTime.now().format(dateTimeFormatter));
            } else {
                System.out.println("获取令牌失败");
            }

//            //不指定超时，令牌生产会有一定时间间隔。将获取不到令牌。必须指定超时时间
//            if (rateLimiter.tryAcquire()) {
//                System.out.println(LocalDateTime.now().format(dateTimeFormatter));
//            }
//            else {
//                System.out.println("获取令牌失败");
//            }



        }
    }
}
