package Test.test2018;

/*
枚举：就是类的实例，和C#不一样C#是值类型字段。枚举内可以定义类的成员字段
 */

import Test.opensource.Netty.MessageType;
import Test.test2018.reflection.Job;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumTest1{

    REGISTER(100000, "注册使用"),
    FORGET_PASSWORD(100001, "忘记密码使用"),
    UPDATE_PHONE_NUMBER(100002, "更新手机号码使用");

    private final int code;
    private final String message;

    EnumTest1(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
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
    public static EnumTest1 getItem(int value) {
        //values= MessageType.values()
        for (EnumTest1 item : EnumTest1.values()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "PinType{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }


    private  void test()
    {
        System.out.println(EnumTest1.FORGET_PASSWORD.getCode());
        System.out.println(EnumTest1.FORGET_PASSWORD.getMessage());
        System.out.println(EnumTest1.FORGET_PASSWORD.toString());
    }

}