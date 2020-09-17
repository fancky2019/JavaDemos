package Test.test2018;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

enum EnumDemo {
    Blue,
    Black,
    Yellow
}

public class EnumTest {
    public void test() {
        EnumDemo enumDemo = EnumDemo.Blue;
        String enumName = EnumDemo.Black.toString();
        Integer m = 0;
    }
}
/**
 * Jackson对枚举进行序列化,默认输出枚举的String名称
 */
enum MessageType {
    HeartBeat(0),
    Data(1);

    private int value;

    private MessageType(int value) {
        this.value = value;
    }

    //JsonVale：序列化时 枚举对应生成的值:0或1
    @JsonValue
    public int getValue()
    {
        return this.value;
    }

    //JsonCreator ：反序列化时的 初始化函数，入参为 对应该枚举的 json值
    @JsonCreator
    public static Test.opensource.Netty.MessageType getItem(int value){
        //values= MessageType.values()
        for(Test.opensource.Netty.MessageType item : Test.opensource.Netty.MessageType.values()){
            if(item.getValue() == value){
                return item;
            }
        }
        return null;
    }

}
