package Test.test2025;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;


import java.util.concurrent.TimeUnit;

/**
 * 引入 netty-all 引来
 *
 * HashedWheelTimer 默认单次执行，但可通过手动重提交实现周期性任务。
 *适用于对周期精度要求不高、任务轻量且数量大的场景（如心跳检测）。
 *
 *
 *HashedWheelTimer 是 基于时间轮算法 的高效定时调度器，适用于 超时任务、订单超时取消、连接检测 等。
 * 采用 环形数组+延迟队列，相比 ScheduledExecutorService，更加 节省 CPU 资源。
 * 适用于 高吞吐、低延迟 的定时任务，但 不适用于毫秒级高精度任务。
 *
 *
 * 5. HashedWheelTimer 的缺点
 * ❌ 不是高精度定时器，最小调度间隔受 tickDuration 影响。
 * ❌ 任务堆积可能导致执行不及时，任务过多可能会导致执行延迟。
 * ❌ 适合中长周期定时任务，不适合超高精度（如 1ms）的定时任务。
 */
public class HashedWheelTimerTest {
    public  void  test()
    {
        fun1();
    }
    private  void  fun1()
    {
        // 创建 HashedWheelTimer，时间片 100ms，槽数 512.精度就是100ms,100ms之后才能到下一个槽
        HashedWheelTimer timer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS, 512);

        // 创建任务，延迟 5 秒执行
//        TimerTask task = timeout -> System.out.println("任务执行时间：" + System.currentTimeMillis());
        TimerTask task = new TimerTask() {
            @Override
            public void run(Timeout timeout) {
                try {
                    // 执行你的周期性逻辑
                    System.out.println("周期性任务执行");
                } finally {
                    // 关键步骤：任务完成后，重新提交自身到时间轮
                    timer.newTimeout(this, 1, TimeUnit.SECONDS); // 间隔1秒
                }
            }
        };
        // 提交任务，延迟 5 秒执行
        timer.newTimeout(task, 5, TimeUnit.SECONDS);

        // 保持主线程运行
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 关闭时间轮
        timer.stop();
    }

}
