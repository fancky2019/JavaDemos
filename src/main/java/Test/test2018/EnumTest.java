package Test.test2018;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
/*
由于枚举类型的实例是常量，因此按照命名惯例，它们都用大写字母表示（如果名称中含有多个单词，使用下划线分隔）。
 */
enum EnumDemo {
    Blue,
    Black,
    Yellow,
    Grey {
        //重写toString() 方法
        public String toString() {

            return "grey";
        }
    },
    Green;
}

public class EnumTest {
    public void test() {


        EnumDemo enumDemo = EnumDemo.Blue;

        String enumName = EnumDemo.Black.toString();//Black
        String greyEnumName = EnumDemo.Grey.toString();//grey
        MessageType messageType=MessageType.Data;
        MessageType  messageType1=  MessageType.fromString("Data");

        Integer val=messageType.getValue();

        //包装类 拆箱造成异常
//        Integer  nunm=null;
//        String messageType2= MessageType.getdescription(nunm);
        Integer m = 0;
    }
}

/**
 * Jackson对枚举进行序列化,默认输出枚举的String名称
 */
enum MessageType {
    HeartBeat(0),
    //    Test {   public String toString() {
//        return "asc";
//    }},
    //指定构造函数的值
    Data(1);

    private int value;

    private String description;

    private MessageType() {

    }

    private MessageType(int value) {
        this.value = value;
    }

    public static MessageType fromString(String str) {
       return MessageType.valueOf(str);
//        MessageType.HeartBeat;
//        MessageType.Data;
    }
    public static String getdescription(int value) {
        MessageType[] values = values();
        for (MessageType messageType : values) {
            if (messageType.value ==value) {
                return messageType.description;
            }
        }
        return "未确认";
    }
    //JsonVale：序列化时 枚举对应生成的值:0或1
    @JsonValue
    public int getValue() {
        return this.value;
    }

    //JsonCreator ：反序列化时的 初始化函数，入参为 对应该枚举的 json值
    @JsonCreator
    public static Test.opensource.Netty.MessageType getItem(int value) {
        //values= MessageType.values()
        for (Test.opensource.Netty.MessageType item : Test.opensource.Netty.MessageType.values()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return null;
    }

}
