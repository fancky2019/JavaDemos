package Test.opensource.redis;

import com.google.common.base.Stopwatch;
import io.netty.util.concurrent.CompleteFuture;
import utility.Configs;
import redis.clients.jedis.*;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 数据类型的首字母找对应的数据类型的操作
 * 操作命令中文文档：http://www.redis.cn/commands/lpushx.html
 */
public class RedisTest {
    private Jedis jedis;//非切片额客户端连接
    private JedisPool jedisPool;//非切片连接池
    private ShardedJedis shardedJedis;//切片额客户端连接
    private ShardedJedisPool shardedJedisPool;//切片连接池

    public RedisTest() {
        initialPool();
//        initialShardedPool();
//        shardedJedis = shardedJedisPool.getResource();
//        获取Jedis实例
        jedis = jedisPool.getResource();
        //设置数据库索引
        jedis.select(0);
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
        JedisPoolConfig config = configJedisPoolConfig();

        // slave链接
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379, "master"));

        // 构造池
        shardedJedisPool = new ShardedJedisPool(config, shards);
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
        redisQueue();
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
        //拼接字符串
        jedis.append("stringKey", ".com");
        jedis.incr("stringKey3");//加1操作,如果Key不存在就新建一个Key


        //读
        String val = jedis.get("stringKey");
        //删除数据
        jedis.del("stringKey");

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
        //先向key java framework 中存放三条数据
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

        //删
        //取出并从库中移除
        jedis.lpop("listKey");
        jedis.del("listKey");

        System.out.println(jedis.lrange("listKey", 0, -1));


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

        //写
        jedis.zadd("sortedSetKey1", 1d, "sortedSetValue1");
        jedis.zadd("sortedSetKey1", 2d, "sortedSetValue12");
        jedis.zadd("sortedSetKey1", 3d, "sortedSetValue13");
        jedis.zadd("sortedSetKey4", 4d, "sortedSetValue4");
        jedis.zadd("sortedSetKey2", 2d, "sortedSetValue2");
        jedis.zadd("sortedSetKey3", 3d, "sortedSetValue3");

        //读
        //取值该Key的所有
        Set<String> setStr = jedis.zrange("sortedSetKey1", 0, -1);
        //取2个
        Set<String> setStr2 = jedis.zrange("sortedSetKey1", 0, 1);
        Iterator<String> zSetStringIterator = setStr.iterator();
        while (zSetStringIterator.hasNext()) {
            String str = zSetStringIterator.next();
            Integer n = 0;
        }

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

        //watch的可以一定要存在，否则无法控制事务。
        String watchKey = "lockKey";
        if (!jedis.exists(watchKey)) {
            jedis.incr(watchKey);
        }
        jedis.watch(watchKey);
        Transaction transaction = jedis.multi();

        transaction.incr("jedis");
        //如果watch的值改变，将不执行事务。
        transaction.exec();

        // 放弃事务
//        transaction.discard();

    }
    //endregion

    //region keyExpire 1.25s---2.25s为一个自然秒。不是1.25秒到了2秒就算1秒
    private void keyExpire() {
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
    public void redisQueue() {

        // region Producer
        CompletableFuture.runAsync(() ->
        {
            //在没带参数构造函数生成的Random对象的种子缺省是当前系统时间的毫秒数。
            Random random = new Random();
            for (int i = 0; i < 100; i++) {
                try {

                    String msg = MessageFormat.format("message - {0}", i);
                    String queueKey = "queueKey";
                    //返回队列长度
                    //LPUSH key value [value ...]
                    Long queueSize = jedis.lpush(queueKey, msg);
                    System.out.println(MessageFormat.format("redisQueueProducer - {0}", msg));
                    // [0,200)
                    int sleepMills = random.nextInt(200);
                    Thread.sleep(sleepMills);
                } catch (Exception ex) {

                }
            }

        });
        //endregion

        // region Consumer
        CompletableFuture.runAsync(() ->
        {
            String queueKey = "queueKey";
            //BRPOP key [key ...] timeout
            //JRedis没有超时参数，时间默认Infinite（无穷大）：就阻塞了。
            //删除，并获得该列表中的最后一个元素，或阻塞，直到有一个可用
            List<String> vals = jedis.brpop(queueKey);
            vals.forEach(p ->
            {
                System.out.println(MessageFormat.format("redisQueueConsumer - {0}", p));
            });

        });
        //endregion
    }
    //endregion

    //region 订阅
    private  void pubSub()
    {
        String pubSubChannel="";

        CompletableFuture.runAsync(() ->
        {
            String  message="";
            Long publish = jedis.publish(pubSubChannel, message);//返回订阅者数量


        });


        // region Consumer
        CompletableFuture.runAsync(() ->
        {

            jedis.subscribe(new JedisPubSub()
            {
                @Override
                /** JedisPubSub类是一个没有抽象方法的抽象类,里面方法都是一些空实现
                 * 所以可以选择需要的方法覆盖,这儿使用的是SUBSCRIBE指令，所以覆盖了onMessage
                 * 如果使用PSUBSCRIBE指令，则覆盖onPMessage方法
                 * 当然也可以选择BinaryJedisPubSub,同样是抽象类，但方法参数为byte[]
                 **/
                public void onMessage(String channel, String message) {
                    System.out.println(Thread.currentThread().getName()+"-接收到消息:channel=" + channel + ",message=" + message);
                    //接收到exit消息后退出
                    if (pubSubChannel.equals(message)) {
                        System.exit(0);
                    }
                }

            }, pubSubChannel);//第一个参数是处理接收消息，第二个参数是订阅的消息频道
        });
        //endregion

    }

    }
    //endregion

    //region 过期回调
    //EVENT NOTIFICATION 配置节点
    //endregion
}
