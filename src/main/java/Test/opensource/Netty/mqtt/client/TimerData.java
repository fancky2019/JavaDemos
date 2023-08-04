package Test.opensource.Netty.mqtt.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class TimerData {


    public static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(10);

    public static ConcurrentHashMap<Integer, ScheduledFuture<?>> scheduledFutureMap = new ConcurrentHashMap<>();
}
