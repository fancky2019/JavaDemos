package Test.opensource.msgpack;

import Test.opensource.Netty.MessageInfo;
import Test.opensource.Netty.MessageType;
import Test.opensource.redis.RedisTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.msgpack.jackson.dataformat.MessagePackMapper;
import redis.clients.jedis.Jedis;
import utility.Hex;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

/**
 * msgpack：
 * https://github.com/msgpack/msgpack-java/blob/develop/msgpack-core/src/test/java/org/msgpack/core/example/MessagePackExample.java
 * <p>
 * jackson-dataformat-msgpack：
 * https://github.com/msgpack/msgpack-java/blob/develop/msgpack-jackson/README.md
 * <p>
 * <p>
 * msgpack不能跨语言通信，请用jackson-dataformat-msgpack
 * jackson-dataformat-msgpack序列化,比msgpack序列化灵活。
 */
public class MsgPackTest {
    public void test() {
        try {
            basicUsage();
            jacksonDataFormatMsgpack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Basic usage example
     *
     * @throws IOException
     */
    public static void basicUsage()
            throws IOException {
        // Serialize with MessagePacker.
        // MessageBufferPacker is an optimized version of MessagePacker for packing data into a byte array
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer
                .packInt(1)
                .packString("leo")
                .packArrayHeader(2)
                .packString("xxx-xxxx")
                .packString("yyy-yyyy");
        packer.close(); // Never forget to close (or flush) the buffer


        byte[] bytes = packer.toByteArray();

        //写入redis maspack 格式无法查看
        Jedis jedis = RedisTest.getInstance().getRedisClient();
        jedis.set("msgPackBasicKey".getBytes(), bytes);

        byte[] bytesRedis = jedis.get("msgPackBasicKey".getBytes());
        // Deserialize with MessageUnpacker
        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(bytesRedis);
        int id = unpacker.unpackInt();             // 1
        String name = unpacker.unpackString();     // "leo"
        int numPhones = unpacker.unpackArrayHeader();  // 2
        String[] phones = new String[numPhones];
        for (int i = 0; i < numPhones; ++i) {
            phones[i] = unpacker.unpackString();   // phones = {"xxx-xxxx", "yyy-yyyy"}
        }
        unpacker.close();

        System.out.println(String.format("id:%d, name:%s, phone:[%s]", id, name, join(phones)));
    }

    private static String join(String[] in) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < in.length; ++i) {
            if (i > 0) {
                s.append(", ");
            }
            s.append(in[i]);
        }
        return s.toString();
    }

    /**
     * Packing various types of data
     *
     * @throws IOException
     */
    public static void packer()
            throws IOException {
        // Create a MesagePacker (encoder) instance
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        // pack (encode) primitive values in message pack format
        packer.packBoolean(true);
        packer.packShort((short) 34);
        packer.packInt(1);
        packer.packLong(33000000000L);

        packer.packFloat(0.1f);
        packer.packDouble(3.14159263);
        packer.packByte((byte) 0x80);

        packer.packNil();

        // pack strings (in UTF-8)
        packer.packString("hello message pack!");

        // [Advanced] write a raw UTF-8 string
        byte[] s = "utf-8 strings".getBytes(MessagePack.UTF8);
        packer.packRawStringHeader(s.length);
        packer.writePayload(s);

        // pack arrays
        int[] arr = new int[]{3, 5, 1, 0, -1, 255};
        packer.packArrayHeader(arr.length);
        for (int v : arr) {
            packer.packInt(v);
        }

        // pack map (key -> value) elements
        packer.packMapHeader(2); // the number of (key, value) pairs
        // Put "apple" -> 1
        packer.packString("apple");
        packer.packInt(1);
        // Put "banana" -> 2
        packer.packString("banana");
        packer.packInt(2);

        // pack binary data
        byte[] ba = new byte[]{1, 2, 3, 4};
        packer.packBinaryHeader(ba.length);
        packer.writePayload(ba);

        // Write ext type data: https://github.com/msgpack/msgpack/blob/master/spec.md#ext-format-family
        byte[] extData = "custom data type".getBytes(MessagePack.UTF8);
        packer.packExtensionTypeHeader((byte) 1, 10);  // type number [0, 127], data byte length
        packer.writePayload(extData);

        // Pack timestamp
        packer.packTimestamp(Instant.now());

        // Succinct syntax for packing
        packer
                .packInt(1)
                .packString("leo")
                .packArrayHeader(2)
                .packString("xxx-xxxx")
                .packString("yyy-yyyy");
    }

    /*
        https://github.com/msgpack/msgpack-java/blob/develop/msgpack-jackson/README.md
     */
    private void jacksonDataFormatMsgpack() {

        try {
            //list
            ObjectMapper objectMapper = new MessagePackMapper();
            // Serialize a List to byte array
            List<Object> list = new ArrayList<>();
            list.add("Foo");
            list.add("Bar");
            list.add(42);
            byte[] bytes = objectMapper.writeValueAsBytes(list);

            // Deserialize the byte array to a List
            List<Object> deserialized = objectMapper.readValue(bytes, new TypeReference<  List<Object>>() {
            });


            //map
            // Instantiate ObjectMapper for MessagePack
//        ObjectMapper objectMapper = MessagePackMapper();

            // Serialize a Map to byte array
            Map<String, Object> map = new HashMap<>();
            map.put("name", "komamitsu");
            map.put("age", 42);
            byte[] bytesHashMap = objectMapper.writeValueAsBytes(map);

            // Deserialize the byte array to a Map
            Map<String, Object> deserializedHashMap = objectMapper.readValue(bytesHashMap, new TypeReference<Map<String, Object>>() {
            });
            System.out.println(deserialized); // => {name=komamitsu, age=42}
        } catch (Exception e) {
            e.printStackTrace();
        }


        MsgPackPojo pojo = new MsgPackPojo();
        pojo.setName("fancky");
        pojo.setAge(27);

        Sub sub = new Sub();
        sub.setName("dfancky");
        pojo.setSub(sub);
        pojo.setEn(MsgPackEnum.HeartBeat);

        MessageInfo msg = new MessageInfo();
        msg.setMessageType(MessageType.HeartBeat);
        msg.setBody("data");

        // Instantiate ObjectMapper for MessagePack
        ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());

        // Serialize a Java object to byte array
        byte[] bytes = new byte[0];
        try {
            bytes = objectMapper.writeValueAsBytes(pojo);
            Jedis jedis = RedisTest.getInstance().getRedisClient();
            jedis.set("msgPackKey".getBytes(), bytes);

            /*
            在rdb中查看key值，通过msgpack格式查看是如下json 格式
             {
              "name": "fancky",
              "age": 27,
              "en": "HeartBeat",
              "sub": {
                "name": "dfancky"
              }
            }
             */
            byte[] bytesRedis = jedis.get("msgPackKey".getBytes());
            // Deserialize the byte array to a Java object
            MsgPackPojo deserialized = objectMapper.readValue(bytesRedis, MsgPackPojo.class);
            System.out.println(deserialized.getName());
            int m = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}


class MsgPackPojo {
    private String name;
    private Integer age;
    private MsgPackEnum en;

    private Sub sub;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setEn(MsgPackEnum en) {
        this.en = en;
    }

    public MsgPackEnum getEn() {
        return en;
    }

    public void setSub(Sub sub) {
        this.sub = sub;
    }

    public Sub getSub() {
        return sub;
    }
}


enum MsgPackEnum {
    HeartBeat,
    Data;

    MsgPackEnum() {
    }

}

class Sub {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
