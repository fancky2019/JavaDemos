package Test.test2023;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimestampTest {
    public void test() {
        fun();
    }

    private void fun() {
        LocalDateTime localDateTime = LocalDateTime.now();
        //获取秒数 1704763889 10位
        //the number of seconds from the epoch of 1970-01-01T00:00:00Z
        long seconds = localDateTime.toEpochSecond(ZoneOffset.of("+08:00"));
        //获取毫秒数时间戳 class ZoneOffset     extends ZoneId. 1704763889940 13位
        long expireTime = localDateTime.toInstant(ZoneOffset.of("+08:00")).toEpochMilli();

       //单号：3位变动设计+ 时间戳10位+用户ID尾6位
       //买家 卖家 冗余一份卖家的数据
        int m = 0;
    }
}
