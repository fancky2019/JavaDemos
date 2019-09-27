package Test.opensource.redis;

import common.Configs;
import redis.clients.jedis.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

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
        jedis.select(10);
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
        transactionTest();

    }

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

    /**
     * 事务
     * Redis 单线程。
     * 如果只有一个连接时候不会有并发问题，但是有两个连接的时候就会有并发问题要加事务。
     * 一主多从，主写从读。
     */
    private void transactionTest() {
        //启动事务前watch  key,如果key在exec执行是否改变了，将不执行，否则执行。exec后将取消watch key。
        //乐观锁:原理有点像CAS(比较交换compare and swap).CAS 存在ABA问题，解决 乐观锁（version、时间戳).
        jedis.watch("lockKey");
        Transaction transaction = jedis.multi();

//        transaction.incr("jedis");
        transaction.set("lockKey", "22");
        transaction.exec();

        // 放弃事务
//        transaction.discard();


    }

    /**
     * redis操作字符串
     */
    public void stringTest() {
        //写
        jedis.set("stringKey", "fancky");
        //拼接字符串
        jedis.append("stringKey", ".com");

        //读
        String val = jedis.get("stringKey");
        //删除数据
        jedis.del("stringKey");
        //设置多个键值对
        jedis.mset("stringKey2", "stringValue", "stringKey3", "22", "stringKey4", "stringValue4");
        jedis.incr("stringKey3");//加1操作,如果Key不存在就新建一个Key
    }


    /**
     * redis操作map集合
     */
    public void hash() {
        //添加数据
        HashMap<String, String> hash = new HashMap();

        hash.put("fieldName", "yc");
        hash.put("fieldAge", "22");
        hash.put("fieldQQ", "22");
        //添加
        jedis.hmset("hashKey1", hash);

        //取出users中的Name,执行结果:[minxr]-->注意结果是一个泛型的List
        //第一个参数是存入redis中map对象的key,后面跟的是放入map中对象的key,后面的key可以是多个，是可变的
        //查询
        List<String> rsmap = jedis.hmget("hashKey1", "fieldName", "fieldAge", "fieldQQ");
        System.out.println(rsmap);


        //删除Hash表的key:hashKey1的field是age的值
        jedis.hdel("hashKey1", "age");
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

    /**
     * redis操作List集合
     */
    public void list() {
        //开始前，先移除所有的内容
        jedis.del("listKey");
        //先向key java framework 中存放三条数据
        jedis.lpush("listKey", "spring");
        jedis.lpush("listKey", "struts");
        jedis.lpush("listKey", "hibernate");

        //再取出所有数据jedis.lrange是按范围取出
        //第一个是key,第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
        //取出所有，但是不删除库
        List<String> listAll = jedis.lrange("listKey", 0, -1);

        //取出并从库中移除
        jedis.lpop("listKey");

        jedis.del("listKey");
        jedis.rpush("listKey", "spring");
        jedis.rpush("listKey", "struts");
        jedis.rpush("listKey", "hibernate");
        System.out.println(jedis.lrange("listKey", 0, -1));


    }


    /**
     * redis操作set集合
     */
    public void set() {
        //添加
        jedis.sadd("setKey1", "setValue1");
        jedis.sadd("setKey2", "setValue2");
        jedis.sadd("setKey3", "setValue3");
        jedis.sadd("setKey5", "setValue5");
        jedis.sadd("setKey4", "setValue4");
        jedis.sadd("setKey4", "setValue41");
        jedis.sadd("setKey4", "setValue42");
        //删除值
        jedis.srem("setKey1", "setValue1");
        //删除
        jedis.del("setKey2");
        Set<String> allVal = jedis.smembers("setKey3");//获取所有加入的value
        Boolean exist = jedis.sismember("setKey4", "setValue4");//判断who是否是user集合的元素
        String val = jedis.srandmember("setKey4");// set 集合中的“随机”元素
        Long count = jedis.scard("setKey4");//返回集合的元素个数
    }


    public void sortedSet() {

        jedis.zadd("sortedSetKey1", 1d, "sortedSetValue1");
        jedis.zadd("sortedSetKey1", 2d, "sortedSetValue12");
        jedis.zadd("sortedSetKey1", 3d, "sortedSetValue13");
        jedis.zadd("sortedSetKey4", 4d, "sortedSetValue4");
        jedis.zadd("sortedSetKey2", 2d, "sortedSetValue2");
        jedis.zadd("sortedSetKey3", 3d, "sortedSetValue3");

        //取值该Key的所有
        Set<String> setStr = jedis.zrange("sortedSetKey1", 0, -1);
        //取2个
        Set<String> setStr2 = jedis.zrange("sortedSetKey1", 0, 1);
        Iterator<String> zSetStringIterator = setStr.iterator();
        while (zSetStringIterator.hasNext()) {
            String str = zSetStringIterator.next();
            Integer n = 0;
        }
        //删除Key中的一个值
        jedis.zrem("sortedSetKey1", "sortedSetValue12");
        //删除
        jedis.del("sortedSetKey2");
    }


}
