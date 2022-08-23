package Test.opensource.encache;

import Model.Student;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.net.URL;

/*
官网： https://www.ehcache.org/
 */
public class EhcacheTest {
    /*
    <!-- https://mvnrepository.com/artifact/org.ehcache/ehcache -->
<dependency>
    <groupId>org.ehcache</groupId>
    <artifactId>ehcache</artifactId>
    <version>3.10.0</version>
</dependency>

     */

    public void test() {
        fun();
    }

    private void fun() {
////        Ehcache在启动的时候会扫描classes目录下的ehcache.xml配置文件，创建CacheManager对象，如果将ehcache.xml
////        文件放到classes目录下，可以通过无参形式加载配置文件。
//        CacheManager manager = new CacheManager();
////        EhcacheTest.class.getClassLoader().getResource(/ehcache.xml)
//        URL url = EhcacheTest.class.getResource("/ehcache.xml");
//        CacheManager manager1 = new CacheManager(url);


        Student student = new Student();
        student.setAge(27);
        student.setName("fancky");
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("preConfigured",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class,
                                        ResourcePoolsBuilder.heap(100))
                                .build())
                .build(true);

        Cache<String, Object> preConfigured = cacheManager.getCache("preConfigured", String.class, Object.class);
        preConfigured.put("student", student);
      //  Object obj = preConfigured.get("student");
        Student stu = (Student) preConfigured.get("student");
        ;

        Cache<Long, String> myCache = cacheManager.createCache("myCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                        ResourcePoolsBuilder.heap(100)).build());

        myCache.put(1L, "da one!");
        String value = myCache.get(1L);

        cacheManager.close();
    }
}
