package Model;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
public class DelayQueueModel<T> implements Delayed {

    private T t;
    private  long expireTime;

    /**
     *
     * @param t
     * @param time expire time (unit - ms)
     */
    public DelayQueueModel(T t,long time)
    {
       this.t=t;
        //获取秒数
        //the number of seconds from the epoch of 1970-01-01T00:00:00Z
        Long second = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        //获取毫秒数 class ZoneOffset     extends ZoneId
        this.expireTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()+time;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return expireTime-LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    @Override
    public int compareTo(Delayed o) {
        DelayQueueModel<T> delayQueueModel = (DelayQueueModel<T>) o;
        long difference = this.expireTime - delayQueueModel.expireTime ;
        if (difference <= 0) {// 改成>=会造成问题
            return -1;
        }else {
            return 1;
        }

    }
}
