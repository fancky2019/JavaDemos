package Test.opensource.Netty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Jackson对枚举进行序列化,默认输出枚举的String名称
 */

public enum MessageType {
    HeartBeat(0,"心跳"),
    Data(1,"数据");

    private final int code;
    private final String message;

    MessageType(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    //JsonVale：序列化时 枚举对应生成的值:0或1
    @JsonValue
    public int getValue() {
        return this.code;
    }

    //JsonCreator ：反序列化时的 初始化函数，入参为 对应该枚举的 json值
    @JsonCreator
    public static MessageType getItem(int value) {
        //values= MessageType.values()
        for (MessageType item : MessageType.values()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return null;
    }

}