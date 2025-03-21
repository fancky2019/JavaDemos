package Test.test2024;

import scala.runtime.VolatileDoubleRef;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Twitter_Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)
 */
public class DateTimeSequence {

//    /** 开始时间截 (2015-01-01) */
//    private final long twepoch = 1420041600000L;
//
//    /** 机器id所占的位数 */
//    private final long workerIdBits = 5L;
//
//    /** 数据标识id所占的位数 */
//    private final long datacenterIdBits = 5L;
//
//    /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
//    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
//
//    /** 支持的最大数据标识id，结果是31 */
//    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
//
//    /** 序列在id中占的位数 */
//    private final long sequenceBits = 12L;
//
//    /** 机器ID向左移12位 */
//    private final long workerIdShift = sequenceBits;
//
//    /** 数据标识id向左移17位(12+5) */
//    private final long datacenterIdShift = sequenceBits + workerIdBits;
//
//    /** 时间截向左移22位(5+5+12) */
//    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
//
//    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
//    private final long sequenceMask = -1L ^ (-1L << sequenceBits);
//
//    /** 工作机器ID(0~31) */
//    private long workerId;
//
//    /** 数据中心ID(0~31) */
//    private long datacenterId;

    /** 毫秒内序列(0~4095) */
    private long sequence = 0L;
    private long sequenceMax = 1000L;

    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;

//    /**
//     * 构造函数
//     * @param workerId 工作ID (0~31)
//     * @param datacenterId 数据中心ID (0~31)
//     */
//    public DateTimeSequence(long workerId, long datacenterId) {
//        if (workerId > maxWorkerId || workerId < 0) {
//            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
//        }
//        if (datacenterId > maxDatacenterId || datacenterId < 0) {
//            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
//        }
//        this.workerId = workerId;
//        this.datacenterId = datacenterId;
//    }


    public void test() {

        for (int i = 0; i < 1200; i++) {
            System.out.println(nextTimeSequence());
        }
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    public synchronized String nextTimeSequence() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence += 1;
            //毫秒内序列溢出
            if (sequence > sequenceMax) {
                sequence = 1L;
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 1L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;
        LocalDateTime l1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastTimestamp), ZoneOffset.of("+8"));
        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String localDateTimeStr = LocalDateTime.now().format(dateTimeFormatter1);

        String seq = localDateTimeStr + "." + sequence;
        return seq;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
}

