package Test.test2023;

import java.io.Serializable;

public class SerializableTest implements Serializable {

    /*
    如果没有手动指定serialVersionUID，Java 会根据类的结构自动生成一个版本号。
    生成规则是基于类的字段、方法和父类等信息进行计算，并使用哈希算法生成一个唯一标识。
     */
    //自动生成的，由于类文件变化(增加字段或减少字段)，它也会发生变化，就会出现不一致的问题，导致反序列化失败。
    static final long serialVersionUID = -3387516993124229948L;
}
