package Test.test2018;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import utility.EnumField;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 枚举：就是类的实例，和C#不一样C#是值类型字段。枚举内可以定义类的成员字段
 * 由于枚举类型的实例是常量，因此按照命名惯例，它们都用大写字母表示（如果名称中含有多个单词，使用下划线分隔）。
 * <p>
 * Jackson对枚举进行序列化,默认输出枚举的String名称。名字要对应，区分大小写。如:Zhi
 * 前端传枚举成员名称（注：不能加双引号）给枚举字段。
 */

/**
 * @JsonValue 不能和 @JsonFormat 同时使用
 * @JsonFormat(shape = JsonFormat.Shape.OBJECT) String enumJson = mapper.writeValueAsString( EnumUnit.values());
 * [{"value":0,"description":"只"},{"value":1,"description":"头"},{"value":2,"description":"ge"}]
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EnumUnit {
    ZHI(0, "只"),
    TOU(1, "头"),
    //    Tou(1,"头") {
//        //重写toString() 方法
//        public String toString() {
//
//            return "TOU";
//        }
//    },
    GE(2, "ge");

    private int value;
    private String description;


    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    private EnumUnit(int value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 名称要对应  如：Zhi
     *
     * @param str
     * @return
     */
    public static EnumUnit fromString(String str) {
        try {
            //名称要对应  如：Zhi
            return EnumUnit.valueOf(str);
        } catch (Exception ex) {
            String msg = ex.getMessage();
            return null;
        }

    }

    public static String getDescription(int value) {
        EnumUnit[] values = values();
        for (EnumUnit enumUnit : values) {
            if (enumUnit.value == value) {
                return enumUnit.description;
            }
        }
        return "Unknown value";
    }

    //jackson序列化

    //JsonVale：序列化时 枚举对应生成的值:0或1。不加注解json 就是队对象名称
    //和
    @JsonValue
    public int getValue() {
        return this.value;
    }

    //JsonCreator ：反序列化时的 初始化函数，入参为 对应该枚举的 json值
    @JsonCreator
    public static EnumUnit getUnitEnum(int value) {
        //values= MessageType.values()
        for (EnumUnit item : EnumUnit.values()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return null;
    }

    public static String getJsonStr() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(Arrays.stream(values()).map(p -> new EnumField(p.value, p.description)).collect(Collectors.toList()));
    }
}
