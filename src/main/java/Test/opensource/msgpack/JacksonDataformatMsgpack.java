package Test.opensource.msgpack;

import Model.JacksonPojo;
import Test.test2020.JDKSerialization;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.jackson.dataformat.MessagePackFactory;

/**
 * @Auther fancky
 * @Date 2020-9-14 10:16
 * @Description
 *
 * 不用在pojo上家@Message注解,比msgpack灵活
 *
 * msgpack比jackson序列化后的字节数要小
 *
 * jackson-dataformat-msgpack序列化,
 *
 */
public class JacksonDataformatMsgpack {
    public void test() {
        serialize();
        serializationCompare();
    }

    private void serialize() {

        try {


            // Serialization/Deserialization
            // Instantiate ObjectMapper for MessagePack
            ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());

            // Serialize a Java object to byte array
            MsgPackPojo pojo = new MsgPackPojo();
            pojo.setName("fancky");
            pojo.setAge(27);

            Sub sub = new Sub();
            sub.setName("dfancky");
            pojo.setSub(sub);
            pojo.setEn(MsgPackEnum.HeartBeat);


            //49byte
            byte[] bytes = objectMapper.writeValueAsBytes(pojo);
            //msgpack 不支持json序列化,使用jackson的json序列化
//            String jsonStr = objectMapper.writeValueAsString(pojo);
            // Deserialize the byte array to a Java object
            MsgPackPojo deserialized = objectMapper.readValue(bytes, MsgPackPojo.class);


            //Jackson序列化
            ObjectMapper mapperJackSon = new ObjectMapper();
            //序列化
            String jsonStr = mapperJackSon.writeValueAsString(pojo);
            //反序列化
            MsgPackPojo pojoJacksonPojo = mapperJackSon.readValue(jsonStr, MsgPackPojo.class);

            //68 bytes
            byte[] jacksonBytes = mapperJackSon.writeValueAsBytes(pojo);
            MsgPackPojo pojoDe = mapperJackSon.readValue(jacksonBytes, MsgPackPojo.class);

            int m = 0;
        } catch (Exception ex) {
            String msg = ex.getMessage();

        }
    }

    private void serializationCompare() {
        try {


            JacksonPojo pojo = new JacksonPojo();
            pojo.setName("fancky");
            pojo.setAddress("上海");
            pojo.setAge(27);
            ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
            //36 byte
            byte[] bytes = objectMapper.writeValueAsBytes(pojo);
            //msgpack 不支持json序列化,使用jackson的json序列化
//            String jsonStr = objectMapper.writeValueAsString(pojo);
            // Deserialize the byte array to a Java object
            JacksonPojo deserialized = objectMapper.readValue(bytes, JacksonPojo.class);


            //Jackson序列化
            ObjectMapper mapperJackSon = new ObjectMapper();
            //序列化
            String jsonStr = mapperJackSon.writeValueAsString(pojo);
            //反序列化
            JacksonPojo pojoJacksonPojo = mapperJackSon.readValue(jsonStr, JacksonPojo.class);

            //56 byte
            byte[] jacksonBytes = mapperJackSon.writeValueAsBytes(pojo);
            JacksonPojo pojoDe = mapperJackSon.readValue(jacksonBytes, JacksonPojo.class);


            //jdk 序列化
            //256 byte
            byte[] jdkBytes = JDKSerialization.serialization(pojo);
            JacksonPojo jdkPojo = JDKSerialization.deserialization(jdkBytes);

            int m=0;
        } catch (Exception ex) {

        }
    }

}
