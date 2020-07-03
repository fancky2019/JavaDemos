package Test.opensource.Jackson;

import Model.JacksonPojo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JacksonTest {

    private ObjectMapper mapper = new ObjectMapper();

    public void test() {
        fun();
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
            List<JacksonPojo> list1 = mapper.readValue(jsonListStr, new TypeReference<List<JacksonPojo>>() {});



            //序列化map
            HashMap<Integer,String> hashMap=new HashMap<>();
            hashMap.put(1,"li");
            hashMap.put(2,"si");
            String jsonMapStr = mapper.writeValueAsString(hashMap);
            HashMap<Integer,String> hashMap1=  mapper.readValue(jsonMapStr, new TypeReference<HashMap<Integer,String>>() {    });

            HashMap<String,JacksonPojo> pojoHashMap=new HashMap<>();
            pojoHashMap.put(jacksonPojo.getName(),jacksonPojo);
            //key  重加入不了HashMap。C#报错
            pojo.setName("张三");
            pojoHashMap.put(pojo.getName(),pojo);
            String jsonPojoMapStr = mapper.writeValueAsString(pojoHashMap);
            HashMap<String,JacksonPojo> pojoHashMap1=  mapper.readValue(jsonPojoMapStr, new TypeReference< HashMap<String,JacksonPojo>>() {    });


            int m = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
