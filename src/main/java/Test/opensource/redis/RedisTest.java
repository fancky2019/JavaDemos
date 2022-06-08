package Test.opensource.redis;

import com.google.common.base.Stopwatch;
import io.netty.util.concurrent.CompleteFuture;
import jodd.util.CollectionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.params.XReadGroupParams;
import redis.clients.jedis.resps.StreamEntry;
import redis.clients.jedis.resps.StreamGroupInfo;
import redis.clients.jedis.resps.StreamPendingSummary;
import utility.Configs;
import redis.clients.jedis.*;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/*
 * 数据类型的首字母找对应的数据类型的操作
 * 操作命令中文文档：http://www.redis.cn/commands/lpushx.html
 * https://redis.io/commands/
 *
 * 密码配置： SECURITY配置节点 ，requirepass fancky123456
 *
 * redis集群最少三台主三从。
 *
 * RedLock；集群配置最少三台机器，最好为奇数。(N/2 + 1)中成功获取锁，则获取锁成功。
 * redisson在加锁的时候，key=lockName, value=uuid + threadID, 采用set结构存储，
 * 并包含了上锁的次数 （支持可重入）；
 * 解锁的时候通过hexists判断key和value是否存在，存在则解锁；这里不会出现误解锁
 *
 * 持久化：rdb,aof。默认RDB,如果不丢就用aof方式。
 *
 *
 * 主从同步：主从同步刚连接的时候进行全量同步；全量同步结束后开始增量同步。如果有需要，slave在任何时候都可以发起全量同步，
 * 其主要策略就是无论如何首先会尝试进行增量同步，如果步成功，则会要求slave进行全量同步，之后再进行增量同步。
 * 只要slave启动，就会和master建立连接发送SYNC请求和主机全量同步。
 *
 *
 * redis 高可用：redis 主从、redis sentinel、 redis cluster 依赖ruby 。
 * mysql 高可用：一主两从、一主多从或者多主多从的集群。
 * redis cluster :解决sentinel扩容问题。hash槽算法集群分片存储。每个节点都有自己的至少一个从节点，
 * 若有一个节点的主从都宕机，集群就不可用。每个节点保存其他节点的主从配置信息，主节点不可用就切换从节点同事更新配置。
 *
 *
 * rabbitMQ集群：https://www.cnblogs.com/lonely-wolf/p/14397704.html
 *
 * 元数据:指的是包括队列名字属性、交换机的类型名字属性、绑定信息、vhost等基础信息，不包括队列中的消息数据。
 * 集群主要有两种模式：普通集群模式和镜像队列模式。
 * 普通集群：各节点只存储相同的元数据，消息存在于不同节点。消费消息只能从一个节点读取，消息则从存储的节点转发到读取的节点机器。
 * 如果一个节点宕机则消息无法消费，只能等待重启，且消息磁盘持久化。
 * 镜像队列模式：各个节点保存相同的元数据和消息。类似redis主从模式。由于各节点同步会消耗带宽。
 * 搭建： HAProxy + Keepalived 高可用集群
 *
 *
 * redis key 过期订阅：
 *
 * LUA脚本保证redis执行复杂脚本的原子性
 *
 * memcached不支持持久化，没有安全机制。memcached是多线程工作，而redis是单线程工作。
 * 各个memcached服务器之间互不通信，各自独立存取数据，不共享任何信息。服务器并不具有分布式功能
 *
 * MongoDB 文档性的数据库，支持字段索引、游标操作，其优势在于查询功能比较强大，擅长查询 JSON 数据，能存储海量数据，但是不支持事务。
 *
 * 文件夹：指定前缀的key放在一个文件夹下。如：key_sb:UserInfo:1 路径前缀之间用冒号分开，当key超过两个RedisDesktop会显示在一个文件夹下。
 */
public class RedisTest {
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * 多线程访问redis 实例从ShardedJedisPool取,不能用单例
     */
    private Jedis jedis;//非切片客户端连接
    private JedisPool jedisPool;//非切片连接池
//    private ShardedJedis shardedJedis;//切片客户端连接
//    private ShardedJedisPool shardedJedisPool;//切片连接池

    public RedisTest() {
//分片
//        initialShardedPool();
//        shardedJedis = shardedJedisPool.getResource();


        //不分片
        initialPool();
//        获取Jedis实例,通过try resource 方式，要关闭池连接
        jedis = jedisPool.getResource();

        //设置数据库索引
        jedis.select(8);


    }

    public static RedisTest getInstance() {
        return RedisTestStaticInnner.redisTest;
    }

    public Jedis getRedisClient() {
        return jedisPool.getResource();
    }

    class RedisTestStaticInnner {
        public static RedisTest redisTest = new RedisTest();
    }


    private JedisPoolConfig configJedisPoolConfig() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(1000l);
        config.setTestOnBorrow(false);
        return config;
    }

    /**
     * 初始化非切片池
     */
    private void initialPool() {
        //可设置DBIndex,默认0
        //   jedisPool = new JedisPool(configJedisPoolConfig(), configs.getRedisIP(), Integer.parseInt(configs.getRedisPort()));
        jedisPool = new JedisPool(configJedisPoolConfig(), Configs.Instance.getRedisIP(), Integer.parseInt(Configs.Instance.getRedisPort()),
                Configs.Instance.getTimeout(), Configs.Instance.getRedisPassword(), Configs.Instance.getDbIndex());

    }


    /**
     * 初始化切片池
     */
    private void initialShardedPool() {
        // 池基本配置
//        JedisPoolConfig config = configJedisPoolConfig();

//        // slave链接
//        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
//        shards.add(new JedisShardInfo("127.0.0.1", 6379, "master"));
//
//        // 构造池
//        shardedJedisPool = new ShardedJedisPool(config, shards);
    }


    public void test() {
        Integer db = jedis.getDB();
//        stringTest();
//        hash();
//        list();
//        set();
//        sortedSet();
//        increment();
//        transactionTest();
//        keyExpire();
//        redisQueue();
        streamQueue();
//        pubSub();


//        expireCallBack();
    }

    /**
     * 不能用单例，多线程会出现问题
     *
     * @return
     */
    private synchronized Jedis getJedis() {
        return jedis;
    }

    //region utility
    private void utility() {
        jedis.flushDB();//清空当前数据库
        jedis.select(0);//切换数据库
    }
    //endregion

    //region increment

    /**
     * 自增、自减
     */
    private void increment() {
        jedis.incr("incrKey");//如果没有key 创建一个key,值=1
        jedis.incrBy("incrKey", 2);//value + 2
        jedis.decr("incrKey");//value - 1

        jedis.del("incrKey");
        Integer m = 0;
    }
    //endregion

    //region string

    /**
     * redis操作字符串
     */
    public void stringTest() {

        // 数据结构
        //StringRedisKey1  StringValue1
        //StringRedisKey1  StringValue2
        //StringRedisKey1  StringValue3
        //    *                *
        //    *                *
        //    *                *

        //写
        jedis.set("stringKey", "fancky");
        //设置多个键值对
        jedis.mset("stringKey2", "stringValue", "stringKey3", "22", "stringKey4", "stringValue4");
//        jedis.mget()
        //拼接字符串
        jedis.append("stringKey", ".com");
        jedis.incr("stringKey3");//加1操作,如果Key不存在就新建一个Key


        //读
        String val = jedis.get("stringKey");
        //删除数据
        jedis.del("stringKey");

        jedis.close();


    }
    //endregion

    //region hash

    /**
     * redis操作map集合
     */
    public void hash() {

        // 数据结构
        //HashSetRedisKey1  HashSetKey1 HashSetValue1
        //                  HashSetKey2 HashSetValu2
        //                  HashSetKey3 HashSetValu3
        //                       *            *
        //                       *            *
        //                       *            *
        //HashSetRedisKey2  HashSetKey1 HashSetValue1
        //                  HashSetKey2 HashSetValu2
        //                  HashSetKey3 HashSetValu3
        //                      *             *
        //                      *             *
        //                      *             *


        //写
        HashMap<String, String> hash = new HashMap();

        hash.put("fieldName", "yc");
        hash.put("fieldAge", "22");
        hash.put("fieldQQ", "22");
        //添加
        jedis.hmset("hashKey1", hash);

        //读
        //取出users中的Name,执行结果:[minxr]-->注意结果是一个泛型的List
        //第一个参数是存入redis中map对象的key,后面跟的是放入map中对象的key,后面的key可以是多个，是可变的
        //查询
        List<String> rsmap = jedis.hmget("hashKey1", "fieldName", "fieldAge", "fieldQQ");
        System.out.println(rsmap);


        //删
        //删除Hash表的key:hashKey1的field是age的值
        jedis.hdel("hashKey1", "age");
        jedis.del("hashKey1");
        System.out.println(jedis.hmget("hashKey1", "age"));//因为删除了，所以返回的是Null
        System.out.println(jedis.hlen("hashKey1"));//返回key为user的键中存放的值的个数2
        System.out.println(jedis.exists("hashKey1"));//是否存在key为user的记录，返回true
        System.out.println(jedis.hkeys("hashKey1"));//返回map对象中的所有key
        System.out.println(jedis.hvals("hashKey1"));//返回map对象中的所有value

        Iterator<String> iter = jedis.hkeys("hashKey1").iterator();
        while (iter.hasNext()) {
            String key = iter.next();
        }
        jedis.close();
    }
    //endregion

    //region list

    /**
     * redis操作List集合
     */
    public void list() {

        //数据结构
        //ListRedisKey1   ListValue1
        //                ListValue2
        //                ListValue3
        //                    *
        //                    *
        //                    *
        //ListRedisKey2   ListValue1
        //                ListValue2
        //                ListValue3
        //                    *
        //                    *
        //                    *


        //写
        //先向key java framework 中存放三条数据。返回列表的长度
        jedis.lpush("listKey", "spring");
        jedis.lpush("listKey", "struts");
        jedis.lpush("listKey", "hibernate");
        jedis.rpush("listKey", "spring");
        jedis.rpush("listKey", "struts");
        jedis.rpush("listKey", "hibernate");

        //读
        //再取出所有数据jedis.lrange是按范围取出
        //第一个是key,第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
        //取出所有，但是不删除库
        List<String> listAll = jedis.lrange("listKey", 0, -1);
        String val = jedis.lindex("listKey", 0);
        //删
        //取出并从库中移除
        jedis.lpop("listKey");
        //The number of keys that were removed.
        jedis.del("listKey");

        System.out.println(jedis.lrange("listKey", 0, -1));

        jedis.close();
    }
    //endregion

    //region set

    /**
     * redis操作set集合
     */
    public void set() {

        //数据结构
        //SetRedisKey1    SetValue1
        //                SetValue2
        //                SetValue3
        //                    *
        //                    *
        //                    *
        //SetRedisKey2    SetValue1
        //                SetValue2
        //                SetValue3
        //                    *
        //                    *
        //                    *


        //写
        jedis.sadd("setKey1", "setValue1");
        jedis.sadd("setKey2", "setValue2");
        jedis.sadd("setKey3", "setValue3");
        jedis.sadd("setKey5", "setValue5");
        jedis.sadd("setKey4", "setValue4");
        jedis.sadd("setKey4", "setValue41");
        jedis.sadd("setKey4", "setValue42");


        //读
        Set<String> allVal = jedis.smembers("setKey3");//获取所有加入的value
        Boolean exist = jedis.sismember("setKey4", "setValue4");//判断who是否是user集合的元素
        String val = jedis.srandmember("setKey4");// set 集合中的“随机”元素
        Long count = jedis.scard("setKey4");//返回集合的元素个数


        //集合运算
        //交集
        Set<String> intersect = jedis.sinter("setKey1", "setKey2");
        jedis.sinterstore("setIntersectSoreKey", "setKey1", "setKey2");
        //并集
        Set<String> union = jedis.sunion("setKey1", "setKey2");
        jedis.sunionstore("setUnionSoreKey", "setKey1", "setKey2");
        //差集
        Set<String> difference = jedis.sdiff("setKey1", "setKey2");
        jedis.sdiffstore("setDifferenceSoreKey", "setKey1", "setKey2");


        //删
        //删除值
        jedis.srem("setKey1", "setValue1");
        //删除
        jedis.del("setKey2");
        jedis.close();
    }
    //endregion

    //region sortedSet
    public void sortedSet() {

        //数据结构
        //                                           Score
        //SortedSetRedisKey1    SortedSetValue1        1
        //                      SortedSetValue2        2
        //                      SortedSetValue3        3
        //                           *                 *
        //                           *
        //                           *
        //SortedSetRedisKey2    SortedSetValue1        1
        //                      SortedSetValue2        2
        //                      SortedSetValue3        3
        //                           *                 *
        //                           *                 *
        //                           *                 *

        //写,key 存在更新分  向有序集合添加一个或多个成员，或者更新已存在成员的分数
        jedis.zadd("sortedSetKey1", 1d, "sortedSetValue1");
        jedis.zadd("sortedSetKey1", 2d, "sortedSetValue12");
        jedis.zadd("sortedSetKey1", 3d, "sortedSetValue13");
        jedis.zadd("sortedSetKey1", 4d, "sortedSetValue14");
        jedis.zadd("sortedSetKey4", 4d, "sortedSetValue4");
        jedis.zadd("sortedSetKey2", 2d, "sortedSetValue2");
        jedis.zadd("sortedSetKey3", 3d, "sortedSetValue3");


        //读
        //值递减(从大到小)来排列，请使用 ZREVRANGE 命令。
        //取值该Key的所有 从小到大
        List<String> setStr = jedis.zrange("sortedSetKey1", 0, -1);
//      zrange  返回有序集中，指定区间内的成员。
//        其中成员的位置按分数值递增(从小到大)来排序。
//        具有相同分数值的成员按字典序(lexicographical order )来排列。
        //取2个 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
        //你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，
        List<String> setStr2 = jedis.zrange("sortedSetKey1", 0, 1);
        Iterator<String> zSetStringIterator = setStr.iterator();
        while (zSetStringIterator.hasNext()) {
            String str = zSetStringIterator.next();
            Integer n = 0;
        }
        double score = jedis.zscore("sortedSetKey1", "sortedSetValue1");
        //排名最小的成员及其分
        String first = jedis.zrange("sortedSetKey1", 0, 0).stream().findFirst().orElse("");
        double score11 = jedis.zscore("sortedSetKey1", first);


        //获取正序排名,排名从0开始，即第一是0
        long rank = jedis.zrank("sortedSetKey1", "sortedSetValue13");
        //逆序，从大到小。0开始。最大为0
        long rank1 = jedis.zrevrank("sortedSetKey1", "sortedSetValue13");


        //集合运算
        //交集
        jedis.zinterstore("setIntersectSoreKey", "setKey1", "setKey2");
        //并集
        jedis.zunionstore("setUnionSoreKey", "setKey1", "setKey2");

        //删除Key中的一个值
        jedis.zrem("sortedSetKey1", "sortedSetValue12");
        //删除
        jedis.del("sortedSetKey2");
    }
    //endregion

    //region 事务

    /**
     * 事务
     * Redis 单线程。
     * 如果只有一个连接时候不会有并发问题，但是有两个连接的时候就会有并发问题要加事务。
     * 一主多从，主写从读。
     */
    private void transactionTest() {
        //启动事务前watch  key,如果key在exec执行是否改变了，将不执行，否则执行。exec后将取消watch key。
        //乐观锁:原理有点像CAS(比较交换compare and swap).CAS 存在ABA问题，解决 乐观锁（version、时间戳).

        //事务不具原子性，其中一条失败，不会回滚。
        try {


            //watch的可以一定要存在，否则无法控制事务。
            String watchKey = "lockKey";
            if (!jedis.exists(watchKey)) {
                jedis.incr(watchKey);
            }
            jedis.watch(watchKey);
            //multi 标记事务块的开始
            Transaction transaction = jedis.multi();

            transaction.set("transactionStringKey", "transactionStringValue");
            transaction.incr("jedis");
            transaction.incr("transactionStringKey");

            transaction.incr("jedis1");


            //EXEC命令进行提交事务:如果watch的值改变，将不执行事务。
            //exec  之后不能  discard 有点类似数据库commit之后不能rollback
            transaction.exec();


            // 放弃事务
//        transaction.discard();
        } catch (Exception ex) {
            ex.printStackTrace();
            int m = 0;
        }
        jedis.close();
    }
    //endregion

    //region keyExpire 1.25s---2.25s为一个自然秒。不是1.25秒到了2秒就算1秒
    private void keyExpire() {


        // redis key 中有冒号（:）rdb 会以树结构显示
        //OK
        String resultSet1 = jedis.setex("cd:expireKeyTest11", 30, "expireKeyTestValue1");
        String resultSet = jedis.setex("cd:expireKeyTest1", 30, "expireKeyTestValue1");

        //剩余ttl
        //当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key 的剩余生存时间。
        long ttl = jedis.ttl("expireKeyTest1");

        // Pttl 命令以毫秒为单位返回 key 的剩余过期时间。  当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以毫秒为单位，返回 key 的剩余生存时间。
        long pttl = jedis.pttl("expireKeyTest1");
        ;
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //若 key 存在返回 1 ，否则返回 0 。 过期key  不存在
        boolean keyExists = jedis.exists("expireKeyTest1");

        //key 过期值为null,否则返回key 的值
        String expireValue = jedis.get("expireKeyTest1");


        Stopwatch stopwatch = Stopwatch.createStarted();
        //获取毫秒数
//        Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();

        Long seconds = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).getEpochSecond();
        long currentMilliseconds = System.currentTimeMillis();

        long millis = currentMilliseconds - seconds * 1000;
        System.out.println(MessageFormat.format("before setting key current milliseconds:{0}--{1}", millis, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm:ss.SSS"))));
        jedis.setex("expireKey", 1, "expireKeyValue");
        while (true) {
//           String  expireKeyValue=jedis.get("expireKey");
            if (!jedis.exists("expireKey")) {
                stopwatch.stop();
                System.out.println(MessageFormat.format(" key expire milliseconds:{0}", stopwatch.elapsed(TimeUnit.MILLISECONDS)));


                Long seconds1 = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).getEpochSecond();
                long currentMilliseconds1 = System.currentTimeMillis();
                long millis1 = currentMilliseconds1 - seconds1 * 1000;
                System.out.println(MessageFormat.format(" key expire current milliseconds:{0}--{1}", millis1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm:ss.SSS"))));
                break;
            } else {

                //JDK9
                //   一：Thread.Sleep(1000);
                //  Thread.Sleep()方法：是强制放弃CPU的时间片，然后重新和其他线程一起参与CPU的竞争。
                //    二：Thread.SpinWait(1000);       //JDK9
                //    Thread.SpinWait()方法：只是让CPU去执行一段没有用的代码。当时间结束之后能马上继续执行，而不是重新参与CPU的竞争。
//                用Sleep()方法是会让线程放弃CPU的使用权。
//                用SpinWait()方法是不会放弃CPU的使用权。

                try {
                    Thread.sleep(1);
                } catch (Exception ex) {

                }

            }
        }
    }
    //endregion

    //region 队列

    /**
     * 测试RabbitMQ确认模式生产10000耗时 14s左右，性能远低于redis的队列：生产10W，5.5s左右
     */
    public void redisQueue() {
        jedis.select(13);//切换数据库
        jedis.flushDB();//清空当前数据库
        Stopwatch stopwatch = Stopwatch.createStarted();
        // region Producer
        CompletableFuture.runAsync(() ->
        {
            //在没带参数构造函数生成的Random对象的种子缺省是当前系统时间的毫秒数。
            Random random = new Random();
            String queueKey = "queueKey";

            for (int i = 0; i < 100000; i++) {
                try {

//                      String msg = MessageFormat.format("message - {0} - {1}", i, System.currentTimeMillis());
                    String msg = MessageFormat.format("message - {0}", i);


                    //返回队列长度
                    //LPUSH key value [value ...]
                    Long queueSize = jedis.lpush(queueKey, msg);
                    //    System.out.println(MessageFormat.format("redisQueueProducer - {0}", msg));


                    // [0,200)
//                    int sleepMills = random.nextInt(200);
//                    Thread.sleep(sleepMills);
                } catch (Exception ex) {

                }
            }
            stopwatch.stop();
            long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            System.out.println(MessageFormat.format("redisQueueProducer - {0}", millis));
        });
        //endregion

//        try {
//
//            Thread.sleep(5000);
//        } catch (Exception ex) {
//
//        }

        //多线程访问redis 实例要么用从ShardedJedisPool取，要么对Jedis加锁取。
        //建议从ShardedJedisPool取
        //   region Consumer
        CompletableFuture.runAsync(() ->
        {

            try {
                Jedis jedisConsumer = jedisPool.getResource();
                jedisConsumer.select(13);//切换数据库
//                jedis.flushDB();//清空当前数据库,看是否阻塞
                String queueKey = "queueKey";
                while (true) {
                    //没有可读的消息将一直阻塞
                    //List(Key和Message):每次返回一个 Key和它的Message
//                    Object o=jedisConsumer.brpop(0, queueKey);
                    //list[0]:key   list[1]:message
                    List<String> keyAndMessage = jedisConsumer.brpop(0, queueKey);
                    if (keyAndMessage.size() == 2) {
                        System.out.println(MessageFormat.format("redisQueueConsumer - {0} - {1}", keyAndMessage.get(1), System.currentTimeMillis()));
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });
        //endregion
    }
    //endregion

    //region 订阅

    /**
     * 订阅不会保存在DB：如果只启动pub,没有启动sub。数据库不会有pub的msg。
     */
    private void pubSub() {
        try {
            String pubSubChannel = "ocg";
            CompletableFuture.runAsync(() ->
            {

                String message = "";
                Jedis jedis = jedisPool.getResource();

                jedis.select(13);
                jedis.flushDB();
                for (int i = 0; i < 1000; i++) {
                    message = MessageFormat.format("message - {0}", i);
                    Long publish = jedis.publish(pubSubChannel, message);//返回订阅者数量
                }


            });


            // region Consumer
            CompletableFuture.runAsync(() ->
            {

                Jedis jedis = jedisPool.getResource();
                jedis.select(13);

                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        System.out.println("channel=" + channel + ",message=" + message);
                    }

                }, pubSubChannel);//第一个参数是处理接收消息，第二个参数是订阅的消息频道
            });
            //endregion
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }


    //endregion

    //region stream 队列

    /*

    xinfo help 查看stream 成员命令








     添加消息           XADD streamKey1 * field3 value3   *表示自动生成消息id（时间戳-序列号） XADD key ID field string
     查询所有消息        XRANGE streamKey - +   XRANGE key start end  start 和 end有-和+两个非指定值，他们分别表示无穷小和无穷大，所以当使用这个两个值时，会查询出全部的消息。
     队列长度           XLEN streamKey1
     删除stream        DEL streamKey1
     删除指定的消息      xdel streamKey1 1654583401388-0



     查看队列信息      xinfo stream  streamKey1

     特殊的ID$       阻塞的那一刻开始通过XADD添加到流的条目

     XREAD [COUNT count] [BLOCK milliseconds] STREAMS key [key …] ID [ID …]
     XREAD 非塞读
     xread count 1 streams streamKey1 0
     XREAD 阻塞读 读取大于id的所有消息（多个），阻塞10s,有点想BRPOP但是BRPOP只返回一个消息
     XREAD BLOCK 10000 STREAMS streamKey1 1654586033285-1
     阻塞读指定返回条数
     XREAD count 1 BLOCK 10000 STREAMS streamKey1 0



     创建消费组
      key ：指定 Stream 队列名称 streamKey1。
     groupname ：自定义消费组的名称，不可重复。
     $ ：表示从尾部开始消费，只接受新消息，而当前 Stream 的消息则被忽略。
     1）XGROUP CREATE streamKey1 groupName1 0-0          创建消费组，并传递消息起始id 0-0 可以直接用0代替0-0(不完全ID)
     2）XGROUP CREATE streamKey1 groupName1 $           从尾部开始消费信息，只接受新消息，当前 Stream 的消息则被忽略。



     查看消费组         XINFO GROUPS streamKey1
     删除消费组         xgroup destroy streamKey1 groupName1
     读取消息           xread count 1 streams streamKey1 0-0



     xreadgroup 消费消息
     XREADGROUP group groupName consumerName [COUNT count] [BLOCK milliseconds] STREAMS key [key …] ID [ID …]
     #创建消费者读取消息
     #在groupName1消费者组内通过consumer1消费streamKey1内的消息，消费1条未分配的消息 (> 表示未分配过消费者的消息)
     ##消费组中消费者读取消息，> 表示每当消费一个信息，消费组游标last_delivered_id 变量就会前移一位。
     #没有消息将阻塞10s
     XREADGROUP group groupName1 consumer1 count 1 BLOCK 10000 streams streamKey1 >

     查看消费者信息
     xinfo consumers streamKey1 groupName1

     Pending 消费了待ack列表
     XPENDING streamKey1 groupName1
    返回四个属性：
    消息的ID。
    获取并仍然要确认消息的消费者名称，我们称之为消息的当前所有者。
    自上次将此消息传递给该消费者以来，经过的毫秒数。
    该消息被传递的次数。




     Ack 消息
     XACK streamKey1 groupName1 1654583433508-0
     */
    private void streamQueue() {
        final String redisQueueKey = "redisQueueKey";
        final String groupName = "groupName1";
        final String consumerName = "consumerName1";
        //producer
        CompletableFuture.runAsync(() ->
        {
            try {
                Jedis jedis = jedisPool.getResource();
                jedis.select(13);
//                jedis.flushDB();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", "fancky2");
                hashMap.put("age", "27");
//                //0-0  相当于new StreamEntryID("0-0");
//                StreamEntryID streamEntryID = new StreamEntryID();
                // 消息的id 时间戳-序列号  1654594218003-0  ; StreamEntryID.NEW_ENTRY toString= "*"
                StreamEntryID msgId = jedis.xadd(redisQueueKey, StreamEntryID.NEW_ENTRY, hashMap);
                int m = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        //consumer
        CompletableFuture.runAsync(() ->
        {
            try (Jedis jedis = jedisPool.getResource()) {
                try {

                    jedis.select(13);
//                jedis.flushDB();
                    Thread.sleep(2000);
                    //Available since redis 6.2
                    // jedis.xgroupCreateConsumer(redisQueueKey, groupName, consumerName);
                    //0-0  相当于new StreamEntryID("0-0");
                    StreamEntryID streamEntryID = new StreamEntryID();

                    //创建消费者组,如果消费者组已存在报异常“已存在”
                    if (!groupExist(redisQueueKey, groupName)) {
                        String groupCreate = jedis.xgroupCreate(redisQueueKey, groupName, streamEntryID, false);
                    }

                    //消息消费时的参数 block 0 一直阻塞,单位毫秒10000
                    XReadGroupParams xReadGroupParams = new XReadGroupParams().block(0).count(1);
                    Map<String, StreamEntryID> streamEntryIDs = new HashMap<>();
                    // 消费组游标last_delivered_id 变量就会前移一位 StreamEntryID.UNRECEIVED_ENTRY    toString() { return ">";}
                    streamEntryIDs.put(redisQueueKey, StreamEntryID.UNRECEIVED_ENTRY);
                    //消费  result：redisKey, List<StreamEntry>>
                    while (true) {
                        //每次获取一个,没有信息返回null
                        List<Map.Entry<String, List<StreamEntry>>> result = jedis.xreadGroup(groupName, consumerName, xReadGroupParams, streamEntryIDs);

                        if (result != null && result.size() > 0) {
                            Map.Entry<String, List<StreamEntry>> entry = result.get(0);
                            List<StreamEntry> streamEntries = entry.getValue();
                            //每次block(0).count(1);每次获取一个
                            StreamEntry streamEntry = streamEntries.get(0);
                            Map<String, String> fields = streamEntry.getFields();
                            //                    fields.forEach((k,v)->
                            //                    {
                            //                        String field=k;
                            //                        String value=v;
                            //                    });

                            String name = fields.get("name");
                            int age = Integer.parseInt(fields.get("age"));


                            //业务处理


                            //如果出现异常，消息一直存在pending 里，读取的时候游标前移，不会重复消费 消费组游标last_delivered_id 变量就会前移一位
                            //可以另外开启一个sweep线程重新处理pending信息。pending 里有消费到现在的毫秒时间

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //pending 列表
                            StreamPendingSummary streamPendingSummary = jedis.xpending(redisQueueKey, groupName);

                            //ack ,可以一次ack多个id (多个消息)
                            jedis.xack(redisQueueKey, groupName, streamEntry.getID());
                            int n = 0;
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("", e);
                }
            }

        });
    }

    private boolean groupExist(String redisQueueKey, String groupName) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(13);
            //创建消费者组,如果消费者组已存在报异常“已存在”
            List<StreamGroupInfo> xinfoGroups = jedis.xinfoGroups(redisQueueKey);
            if (xinfoGroups != null && xinfoGroups.size() > 0) {
                //exist
                boolean exist = xinfoGroups.stream().anyMatch(p -> p.getName().equals(groupName));
                return exist;
            }
            return false;
        }
    }


    //endregion

    //region 过期回调

    /**
     * 修改Redis 配置
     * EVENT NOTIFICATION 配置节点
     * 默认：notify-keyspace-events "" 修改成 notify-keyspace-events Ex
     * 重启Redis
     */
    private void expireCallBack() {
        try {
            CompletableFuture.runAsync(() ->
            {
                Jedis jedis = jedisPool.getResource();
                jedis.select(13);
                jedis.flushDB();
                jedis.psubscribe(new JedisPubSub() {
//            public class KeyExpiredListener extends JedisPubSub {

                    @Override
                    public void onPSubscribe(String pattern, int subscribedChannels) {
                        System.out.println("onPSubscribe " + pattern + " " + subscribedChannels);
                    }

                    /**
                     * 如果Key 过期了，程序没启动将不会收到过期的回调信息。
                     * @param pattern
                     * @param channel
                     * @param message
                     */
                    @Override
                    public void onPMessage(String pattern, String channel, String message) {

                        System.out.println("onPMessage pattern " + pattern + " " + channel + " " + message);
                    }
                    //监听数据库索引是13的key过期
                }, "__keyevent@13__:expired");
            });


            try {

                Thread.sleep(2000);
            } catch (Exception ex) {

            }

            CompletableFuture.runAsync(() ->
            {
                Jedis jedis = jedisPool.getResource();
                jedis.select(13);
                jedis.flushDB();
                jedis.set("stringKey", "fancky");
                jedis.expire("stringKey", 10);
            });

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
    //endregion
}
