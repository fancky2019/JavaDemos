package Test.opensource.zookeeper;

import net.bytebuddy.description.method.MethodDescription;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class ZooKeeperTest {
    /*
      Maven添加zookeeper、curator-framework、curator-recipes依赖

      curator-framework：对zookeeper的底层api的一些封装。
      curator-client：提供一些客户端的操作，例如重试策略等。
      curator-recipes：封装了一些高级特性，如：Cache事件监听、选举、分布式锁、分布式计数器、分布式Barrier等。



    http://curator.apache.org/releases.html下载：apache-curator-5.1.0-source-release.zip里面有curator-examples
    curator-examples有curator的常用操作
     */

    public void test() {
        try {
//            createNode();
//        queryNode();
//        updateNode();
//        getStat();

//            transaction(createClient());

//            usingWatcher();
            pathCacheExample();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    .inBackground((CuratorFramework curatorFramework, CuratorEvent curatorEvent) ->
                    {
                        //this is another method of getting notification of an async completion
                        //  操作异常不进入回调,成功才回调
                        CuratorEventType curatorEventType = curatorEvent.getType();
                    })
                    .forPath("/Test/sub/sub4", "sub2".getBytes());//注意节点前要已"/"开头
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
            client.delete().guaranteed().deletingChildrenIfNeeded().forPath("/node1");


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


            //返回Test子节点名称，不返回孙子及后代节点。
            List list = client.getChildren().forPath("/Test"); // 获取子节点的路径
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void getStat() {
        try {
            Stat stat = new Stat();
            CuratorFramework client = createClient();

            client.getData().storingStatIn(stat)
                    .forPath("/Test/node1");

            int version1 = stat.getVersion();//修改一次版本号+1.

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Collection<CuratorTransactionResult> transaction(CuratorFramework client) throws Exception {
        // this example shows how to use ZooKeeper's transactions

        CuratorOp createOp = client.transactionOp().create().forPath("/Test/path", "payload".getBytes());
        CuratorOp setDataOp = client.transactionOp().setData().forPath("/Test/path", "other data".getBytes());
        CuratorOp deleteOp = client.transactionOp().delete().forPath("/Test/sub/sub1");

        //执行事务操作，如果有失败就抛异常
        Collection<CuratorTransactionResult> results = client.transaction().forOperations(createOp, setDataOp, deleteOp);

        /*
        /Test/path - CREATE
        /Test/path - SET_DATA
        /Test/sub/sub1 - DELETE
         */
        for (CuratorTransactionResult result : results) {
            System.out.println(result.getForPath() + " - " + result.getType());
        }

        return results;
    }


    private void updateNode() {

        try {
            CuratorFramework client = createClient();
            Stat stat = client.setData()
//                    .withVersion(222)) // 指定版本修改,找不到匹配版本报异常
                    .forPath("/Test/node1", "data".getBytes());
            //找不到要修改的节点抛异常
            int version1 = stat.getVersion();//修改一次版本号+1.
            stat = client.setData().forPath("/Test/node1", "data1".getBytes());
            int version2 = stat.getVersion();
            int m = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //region  Watcher


    private void usingWatcher() {
        //  绑定事件只有三个操作：getData、exists、getChildren。
        //只能回调一次
        try {
            CuratorFramework client = createClient();
            client.getData().usingWatcher((CuratorWatcher) curatorWatcher ->
            {
                Watcher.Event.EventType t = curatorWatcher.getType();
            }).forPath("/Test/node2");
            Thread.sleep(10000);
            int m = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*


    Curator提供了三种Watcher(Cache)来监听结点的变化
    Path Cache : 监控一个ZNode的子节点. 当一个子节点增加， 更新，删除时， Path Cache会改变它的状态， 会包含最新的子节点， 子节点的数据和状态
    Node Cache : 只是监听某一个特定的节点
    Tree Cache : 可以监控整个树上的所有节点，类似于PathCache和NodeCache的组合

     */


    //region PathCacheExample

    private void pathCacheExample() throws Exception {

//        官方demo监听和操作在同一个线程内，会有问题，收不到修改的回调
        //一个线程操作
        CompletableFuture.runAsync(() ->
        {
            String path = "/example/cache";
            String connectString = "localhost:2181";
            CuratorFramework client = null;

            try {
                client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
                client.start();
                // in this example we will cache data. Notice that this is optional.


//            addListener(cache);
                Thread.sleep(1000);
//            client.create().creatingParentContainersIfNeeded()//，如果父节点不存在直接创建会抛出NoNodeException，此方法递归创建父节点
//                    .withMode(CreateMode.PERSISTENT)
//                    .inBackground((CuratorFramework curatorFramework, CuratorEvent curatorEvent) ->
//                    {
//                        //this is another method of getting notification of an async completion
//                        //  操作异常不进入回调,成功才回调
//                        CuratorEventType curatorEventType = curatorEvent.getType();
//                    })
//                    .forPath("/example/cache/test2", "payload1".getBytes());//注意节点前要已"/"开头


                String pathWatcher = "/example/cache/test3";
                Stat stat = client.checkExists().forPath(pathWatcher);
                if (stat != null) {
                    client.setData().forPath(pathWatcher, "payload12".getBytes());//注意节点前要已"/"开头
                } else {
                    client.create().creatingParentsIfNeeded().forPath(pathWatcher);
                }

//            client.delete().guaranteed().deletingChildrenIfNeeded().forPath(pathWatcher);
//            client.close();//不能关闭，不然收不到回调Listener
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });

        //一个线程监听
        CompletableFuture.runAsync(() ->
        {

            String connectString = null;
            try {
                connectString = "localhost:2181";
                String path = "/example/cache";
                CuratorFramework   client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
                client.start();
                PathChildrenCache cache = new PathChildrenCache(client, path, true);
                cache.start();
                // a PathChildrenCacheListener is optional. Here, it's used just to log changes
                PathChildrenCacheListener listener = new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                        switch (event.getType()) {
                            case CHILD_ADDED: {
                                System.out.println("Node added: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                                break;
                            }

                            case CHILD_UPDATED: {
                                System.out.println("Node changed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                                break;
                            }

                            case CHILD_REMOVED: {
                                System.out.println("Node removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                                break;
                            }
                        }
                    }
                };
                cache.getListenable().addListener(listener);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }


    //endregion
    //endregion


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
