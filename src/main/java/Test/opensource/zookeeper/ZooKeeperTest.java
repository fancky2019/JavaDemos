package Test.opensource.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/*
 *
 *ZK ： 文件系统、通知机制
 *
 *
 *
 *
 * 有序节点：假如当前有一个父节点为/lock，我们可以在这个父节点下面创建子节点；
 * zookeeper提供了一个可选的有序特性，例如我们可以创建子节点“/lock/node-”并且指明有序，那么zookeeper在生成子节点时会根据当前的子节点数量自动添加整数序号
 * 也就是说，如果是第一个创建的子节点，那么生成的子节点为/lock/node-0000000000，下一个节点则为/lock/node-0000000001，依次类推。
 * 临时节点：客户端可以建立一个临时节点，在会话结束或者会话超时后，zookeeper会自动删除该节点。
 * 事件监听：在读取数据时，我们可以同时对节点设置事件监听，当节点数据或结构变化时，zookeeper会通知客户端。当前zookeeper有如下四种事件：
 * 节点创建
 * 节点删除
 * 节点数据修改
 * 子节点变更
 *
 *1.命名服务   2.配置管理   3.集群管理   4.分布式锁  5.队列管理
 * 分布式锁：创建临时有序节点，监听当前线程创建的节点的序号是否未最小，则获取锁，否则监听前一个序号的事件，删除则获取锁。
 *         分布式锁独占、控制时序。编号最小获取锁。
 * 命名服务：一个服务名称路径下有多个子节点ip；ServerNamePath:server1IP、server2IP、server3IP。通过服务名称获取节点下可用的ip.
 * 配置管理： 监听配置节点变化，获取最新配置节点信息
 * 集群管理：集群管理无在乎两点：是否有机器退出和加入、选举master。监听一个节点下所有的子节点（各个服务ip）的上线下线，然后选举编号最小master
 * */
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
            queryNode();
//        updateNode();
//        getStat();

//            transaction(createClient());

//            usingWatcher();
//            pathCacheExample();

//            distributeLock();

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
                // .namespace("base") //默认父节点
                // etc. etc.
                .build();


    }


    /*
    Zookeeper的节点创建模式：
    PERSISTENT：持久化
    PERSISTENT_SEQUENTIAL：持久化并且带序列号
    EPHEMERAL：临时
    EPHEMERAL_SEQUENTIAL：临时并且带序列号


1、PERSISTENT-持久化目录节点 :户端与zookeeper断开连接后，该节点依旧存在
2、PERSISTENT_SEQUENTIAL-持久化顺序编号目录节点 :客户端与zookeeper断开连接后，该节点依旧存在，只是Zookeeper给该节点名称进行顺序编号
3、EPHEMERAL-临时目录节点 :户端与zookeeper断开连接后，该节点被删除
4、EPHEMERAL_SEQUENTIAL-临时顺序编号目录节点 :客户端与zookeeper断开连接后，该节点被删除，只是Zookeeper给该节点名称进行顺序编号

     */
    private void createNode() {

        CuratorFramework client = null;
        try {
            client = createClient();
            //如果节点存在,再添加节点会添加不进去。

            //如果删除一个不存的节点会报错。
//            client.delete().deletingChildrenIfNeeded().forPath("/Test");

            // 如果没有设置节点属性，节点创建模式默认为持久化节点，内容默认为空
//            client.create().forPath("path");

            //CreateMode.PERSISTENT :sub4
            //CreateMode.PERSISTENT_SEQUENTIAL ::sub40000000000 //路径后加10位有序数字
            //CreateMode.EPHEMERAL :
            String path = client.create()
                    .creatingParentContainersIfNeeded()//，如果父节点不存在直接创建会抛出NoNodeException，此方法递归创建父节点
                    .withMode(CreateMode.PERSISTENT)
                    .inBackground((CuratorFramework curatorFramework, CuratorEvent curatorEvent) ->
                    {
                        //this is another method of getting notification of an async completion
                        //  操作异常不进入回调,成功才回调
                        CuratorEventType curatorEventType = curatorEvent.getType();
                    })
                    .forPath("/Test/sub/node1", "node-data1".getBytes());//注意节点前要已"/"开头
            int m = 0;
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

    private void queryNode() {
        CuratorFramework client = null;
        try {
            client = createClient();
            //不存在返回null
            Stat obj = client.checkExists().forPath("/path");
            //存在返回Stat实例,不存在返回null
            Stat obj1 = client.checkExists().forPath("/Test/node1");
            //节点不存在报异常
            byte[] bytes = client.getData().forPath("/Test/node1");
            String data = new String(bytes);

            Stat stat = new Stat();
            //节点不存在报异常.
            byte[] bytes1 = client.getData().storingStatIn(stat).forPath("/Test/node1");
            //返回Test子节点名称，不返回孙子及后代节点。
            List<String> list = client.getChildren().forPath("/Test"); // 获取子节点的路径
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //查看节点的stat(统计)信息
    private void getStat() {
        try {
            Stat stat = new Stat();
            CuratorFramework client = createClient();
            client.getData().storingStatIn(stat).forPath("/Test/node1");
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
                    client.setData().forPath(pathWatcher, "nodeDataChange".getBytes());//注意节点前要已"/"开头
                } else {
                    client.create().creatingParentsIfNeeded().forPath(pathWatcher, "nodeData".getBytes());
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
                CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
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


    //region 分布式锁
//Curator是一个zookeeper的开源客户端，也提供了分布式锁的实现。
    private void distributeLock() {
        String path = "/lock";
        String connectString = "localhost:2181";
        CuratorFramework client = null;

        client = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
        client.start();
        InterProcessMutex interProcessMutex = new InterProcessMutex(client, path);
        try {
            //获得锁：可设置超时时间
            //会在path下创建一个持久有序节点
            interProcessMutex.acquire();

            int m = 0;
//            interProcessMutex.release();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //释放锁
                interProcessMutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion


}
