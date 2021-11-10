package Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageEnum {
    HEART_BEAT(0, "心跳"),
    DATA(1, "数据");

    private int value;
    private String description;

    MessageEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }


    /*
     必须指定jackson序列化的值
     */
    @JsonValue
    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

//    /*
//     可以不用重写
//     */
//    @JsonCreator
//    public static MessageEnum getMessageEnum(int code) {
//        //values= MessageType.values()
//        for (MessageEnum item : MessageEnum.values()) {
//            if (item.getValue() == code) {
//                return item;
//            }
//        }
//        return null;
//    }

}
