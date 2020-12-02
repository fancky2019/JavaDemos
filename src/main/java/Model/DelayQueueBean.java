package Model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 基于创建队列的时间点，延迟指定时间进行迭代。
 * DelayQueue<T>内部会根据延迟间隔从小到大排序
 */
public class DelayQueueBean implements Delayed {

    /**
     * 出队时间间隔
     */
    private Long interval;

    /**
     * 延迟后时间，即出队时间
     */
    private Long delayTime;
    private String orderNumber;

    private LocalDateTime createTime;


    /**
     * @param interval    延迟的毫秒数
     * @param orderNumber 订单号
     */
    public DelayQueueBean(Long interval, String orderNumber) {
        this.interval = interval;
        this.orderNumber = orderNumber;
        delayTime = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() + interval;
        createTime = LocalDateTime.now();
    }

    public Long getDelayTime() {
        return delayTime;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * Returns the remaining delay associated with this object, in the
     * given time unit.
     *
     * @param unit the time unit
     * @return the remaining delay; zero or negative values indicate
     * that the delay has already elapsed
     */
    @Override
    public long getDelay(TimeUnit unit) {
        //不能用interval，因为入队了很久才调用出队，此时如果用interval，getDelay(TimeUnit unit)还是大于0。

        //DelayQueue<E> 内部出队逻辑：getDelay(TimeUnit unit)<0 ,小于0才出队-->所以应该是延迟后的时间小于当前时间
        return unit.convert(delayTime - LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(), unit);
    }

    @Override
    public int compareTo(Delayed o) {
//        Objects.requireNonNull(o);
        if (o == null) {
            return 1;
        }
        DelayQueueBean bean = (DelayQueueBean) o;
        return this.delayTime.compareTo(bean.delayTime);
    }
}
