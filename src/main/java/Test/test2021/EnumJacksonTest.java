package Test.test2021;

import Model.MessageEnum;
import Test.opensource.Netty.MessageInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EnumJacksonTest {

    public void test() {
        fun();
    }


    private void fun() {
        MessageEnum messageEnum = MessageEnum.DATA;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonStr = objectMapper.writeValueAsString(messageEnum);

            MessageEnum messageEnum1 = objectMapper.readValue(jsonStr, MessageEnum.class);
            int m = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
