package Test.opensource.zookeeper;

import net.bytebuddy.description.method.MethodDescription;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 *
 */
public class ZooKeeperTest {
    /*
      Maven添加zookeeper、curator-framework、curator-recipes依赖

      curator-framework：对zookeeper的底层api的一些封装。
      curator-client：提供一些客户端的操作，例如重试策略等。
      curator-recipes：封装了一些高级特性，如：Cache事件监听、选举、分布式锁、分布式计数器、分布式Barrier等。



    http://curator.apache.org/releases.html下载：apache-curator-5.1.0-source-release.zip里面有sample
     */

    public void test() {
//        createNode();
//        queryNode();
        updateNode();
    }


    public CuratorFramework createClient() {
        String connectString = "localhost:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = createClient(connectString, retryPolicy, 5000, 3000);
        client.start();
        return client;
    }


    public static CuratorFramework createClient(String connectionString, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs) {
//        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        // using the CuratorFrameworkFactory.builder() gives fine grained control
        // over creation options. See the CuratorFrameworkFactory.Builder javadoc
        // details
        return CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .namespace("base") //默认父节点
                // etc. etc.
                .build();


    }


    /*
    Zookeeper的节点创建模式：
    PERSISTENT：持久化
    PERSISTENT_SEQUENTIAL：持久化并且带序列号
    EPHEMERAL：临时
    EPHEMERAL_SEQUENTIAL：临时并且带序列号
     */
    private void createNode() {

        CuratorFramework client = null;
        try {
            client = createClient();
            //如果节点存在,再添加节点会报错。

            //如果删除一个不存的节点会报错。
//            client.delete().deletingChildrenIfNeeded().forPath("/Test");

            // 如果没有设置节点属性，节点创建模式默认为持久化节点，内容默认为空
//            client.create().forPath("path");

            client.create()
                    .creatingParentContainersIfNeeded()//，如果父节点不存在直接创建会抛出NoNodeException，此方法递归创建父节点
                    .withMode(CreateMode.PERSISTENT)
                    .forPath("/Test/node1", "node1".getBytes());//注意节点前要已"/"开头
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deleteNode() {

        CuratorFramework client = null;
        try {
            client = client = createClient();

            //只能删除叶子节点，非叶子节点报错
            //client.delete().forPath("path");

            //如果删除一个不存的节点会报错。
//            client.delete().deletingChildrenIfNeeded().forPath("/Test");

            //删除一个节点，并且递归删除其所有的子节点
            client.delete().deletingChildrenIfNeeded().forPath("/node1");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void queryNode() {
        CuratorFramework client = null;
        try {
            client = client = createClient();
            //不存在返回null
            Object obj = client.checkExists().forPath("/path");
            //存在返回Stat实例
//            Stat
            Object obj1 = client.checkExists().forPath("/Test/node1");
            //节点差找不到报异常
            byte[] bytes = client.getData().forPath("/Test/node1");
            
            String data = new String(bytes);
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void updateNode() {

        try {
            CuratorFramework client = createClient();
            Stat stat = client.setData()
//                    .withVersion(222)) // 指定版本修改,找不到匹配版本报异常
                    .forPath("/Test/node1", "data".getBytes());

            int version1 = stat.getVersion();//修改一次版本号+1.
            stat = client.setData().forPath("/Test/node1", "data1".getBytes());
            int version2 = stat.getVersion();
            int m = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取精确到秒的时间戳
     *
     * @param localDateTime
     * @return
     */
    public static int getSecondTimestampTwo(LocalDateTime localDateTime) {
        if (null == localDateTime) {
            return 0;
        }
        Long seconds = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
        String timestamp = String.valueOf(seconds / 1000);
        return Integer.valueOf(timestamp);
    }
}
