package Test.opensource.Jackson;

import Model.JacksonPojo;
import Test.opensource.Netty.MessageInfo;
import Test.opensource.Netty.MessageType;
import Test.opensource.orgapachecommons.commonpool.ShipOrderInfo;
import Test.test2018.EnumUnit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.alibaba.fastjson.JSONPatch.OperationType.test;

/**
 * Jackson对枚举进行序列化，将只能简单的输出枚举的String名称：
 */
public class JacksonTest {

    private ObjectMapper mapper = new ObjectMapper();

    public void test() {

        seri();
        jdkSerialization();
        serialization();
        fun();
    }

    private void seri() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);


        MessageInfo msg = null;
        try {
            //[1,2,3,4]
            String listJson = mapper.writeValueAsString(list);
            msg = new MessageInfo();

            msg.setMessageType(MessageType.HeartBeat);
            msg.setBody("data");
            //序列化
            String jsonStr = mapper.writeValueAsString(msg);
            //反序列化
            MessageInfo pojo = mapper.readValue(jsonStr, MessageInfo.class);
            int m = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fun() {

        JacksonPojo jacksonPojo = new JacksonPojo();
        jacksonPojo.setName("fancky");
        jacksonPojo.setAddress("上海");
        jacksonPojo.setAge(27);
        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //string 转换
        LocalDateTime localDateTime = LocalDateTime.parse("2018-12-31 13:12:00", dateTimeFormatter1);
        jacksonPojo.setBirthday(localDateTime);
        jacksonPojo.setJob("程序员");


        try {
            //序列化单个对象

            //序列化
            String jsonStr = mapper.writeValueAsString(jacksonPojo);
            //反序列化
            JacksonPojo pojo = mapper.readValue(jsonStr, JacksonPojo.class);


            //序列化List
            List<JacksonPojo> list = new ArrayList<>();
            list.add(jacksonPojo);
            list.add(pojo);

            String jsonListStr = mapper.writeValueAsString(list);
            //第二个参数为TypeReference ，其为抽象类，new  一个匿名内部类
            List<JacksonPojo> list1 = mapper.readValue(jsonListStr, new TypeReference<List<JacksonPojo>>() {
            });


            //序列化map
            //原始类型map
            HashMap<Integer, String> hashMap = new HashMap<>();
            hashMap.put(1, "li");
            hashMap.put(2, "si");
            //{"1":"li","2":"si"}
            String jsonMapStr = mapper.writeValueAsString(hashMap);
            HashMap<Integer, String> hashMap1 = mapper.readValue(jsonMapStr, new TypeReference<HashMap<Integer, String>>() {
            });


            //泛型map。hashmap json 序列化，通过添加每个字段值的hashmao 可代替java 对象，避免声明对象
            HashMap<String, JacksonPojo> pojoHashMap = new HashMap<>();
            pojoHashMap.put(jacksonPojo.getName(), jacksonPojo);
            //key  重加入不了HashMap。C#报错
            pojo.setName("张三");
            pojoHashMap.put(pojo.getName(), pojo);
            String jsonPojoMapStr = mapper.writeValueAsString(pojoHashMap);
            HashMap<String, JacksonPojo> pojoHashMap1 = mapper.readValue(jsonPojoMapStr, new TypeReference<HashMap<String, JacksonPojo>>() {
            });

            //String jsonStr ="{\"uid\":100003,\"password\":\"123456\"},";
            // 2
            JavaType javaType = mapper.getTypeFactory().constructParametricType(HashMap.class, String.class, JacksonPojo.class);
            HashMap<String, JacksonPojo> pojoHashMap2 = mapper.readValue(jsonPojoMapStr, javaType);

            //可将字段 key value 的map  转 java 对象
//            String json=  searchHit.getSourceAsString();
//            ShipOrderInfo shipOrderInfo=    objectMapper.readValue(json, ShipOrderInfo.class);


            //读取json 写入hashmap  将嵌套多层json 转hashmap，注意hashmap的类型参数 HashMap<String, Object>
            //不能是 HashMap<String, String>
            String jStr = "{\"code\":200,\"msg\":null,\"content\":\"9\",\"test\":{\"name\":\"test\",\"name1\":\"test1\"}}";
            HashMap<String, Object> pojoHashMap1111 = mapper.readValue(jStr, mapper.getTypeFactory().constructParametricType(HashMap.class, String.class, Object.class));
            HashMap<String, String> testHashMap = (HashMap<String, String>) pojoHashMap1111.get("test");
            String testVal = testHashMap.get("name");
            HashMap<String, Object> pojoHashMap11 = mapper.readValue(jStr, new TypeReference<HashMap<String, Object>>() {
            });

            //region 枚举

            String enumJson = mapper.writeValueAsString(EnumUnit.values());
            String enumJson1 = EnumUnit.getJsonStr();
            //endregion

            int m = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 二进制序列化，序列化后比msgpack序列化要打。二进制用msgpack序列化
     */
    private void serialization() {
        try {
            //  Serialization/Deserialization
            JacksonPojo jacksonPojo = new JacksonPojo();
            jacksonPojo.setName("fancky");
            jacksonPojo.setAddress("上海");
            jacksonPojo.setAge(27);
            //56 bytes
            byte[] jacksonBytes = mapper.writeValueAsBytes(jacksonPojo);
            JacksonPojo pojoDe = mapper.readValue(jacksonBytes, JacksonPojo.class);
            int m = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jdkSerialization() {
        try {
            //  Serialization/Deserialization
            JacksonPojo jacksonPojo = new JacksonPojo();
            jacksonPojo.setName("fancky");
            jacksonPojo.setAddress("上海");
            jacksonPojo.setAge(27);

            //jdk序列化
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(jacksonPojo);
            //256 bytes
            byte[] jdkBytes = byteArrayOutputStream.toByteArray();
            out.close();
            byteArrayOutputStream.close();


            //jdk 反序列化
            ByteArrayInputStream fileIn = new ByteArrayInputStream(jdkBytes);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            JacksonPojo jdkDes = (JacksonPojo) in.readObject();
            in.close();
            fileIn.close();
            int m = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
