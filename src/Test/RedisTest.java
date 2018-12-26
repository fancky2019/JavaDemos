package Test;

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
        Configs configs = new Configs();
        //可设置DBIndex,默认0
        //   jedisPool = new JedisPool(configJedisPoolConfig(), configs.getRedisIP(), Integer.parseInt(configs.getRedisPort()));
        jedisPool = new JedisPool(configJedisPoolConfig(), configs.getRedisIP(), Integer.parseInt(configs.getRedisPort()),
                configs.getTimeout(), configs.getRedisPossword(), configs.getDbIndex());

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
        stringTest();
        hash();
        list();
        set();
        sortedSet();
    }

    /**
     * redis操作字符串
     */
    public void stringTest() {
        //添加数据
        jedis.set("stringKey", "fancky");
        //拼接字符串
        jedis.append("stringKey", ".com");
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
        jedis.sadd("setKey", "liuling");
        jedis.sadd("setKey", "xinxin");
        jedis.sadd("setKey", "ling");
        jedis.sadd("setKey", "zhangxinxin");
        jedis.sadd("setKey", "who");

        //删除值liuling
        jedis.srem("setKey", "liuling");
        Set<String> allVal = jedis.smembers("setKey");//获取所有加入的value
        Boolean exist = jedis.sismember("setKey", "who");//判断who是否是user集合的元素
        String val = jedis.srandmember("setKey");// set 集合中的“随机”元素
        Long count = jedis.scard("setKey");//返回集合的元素个数
    }


    /**
     * redis排序
     */
    public void sortedSet() {

        //jedis 排序
        //注意，此处的rpush和lpush是List的操作。是一个双向链表（但从表现来看的)
        jedis.del("sortedSetKey");//先清除数据，再加入数据进行测试
        jedis.rpush("sortedSetKey", "1");
        jedis.lpush("sortedSetKey", "6");
        jedis.lpush("sortedSetKey", "3");
        jedis.lpush("sortedSetKey", "9");
        //取出所有
        List<String> listAll = jedis.lrange("sortedSetKey", 0, -1);
        //取出后排序
        List<String> sortedList = jedis.sort("sortedSetKey");//[1,3,6,9]
    }


}
