package Test.test2018;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;

/*
声明注解，和C#的特性一样

注解的成员变量在注解的定义中以“无形参的方法”形式来声明，其方法名定义了该成员变量的名字，其返回值定义了该成员变量的类型

注解元素数据类型：
所有基本类型（int,float,boolean,byte,double,char,long,short）
String
Class（不是类，是(type)）
enum
Annotation
上述类型的数组



使用：
    @Description("姓名")
     @Description(value = "姓名")
分别赋值给属性：
    @Description(value = "年龄", color = "red", age = 25)
*/

//自定义注解
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
//注解的成员变量在注解的定义中以“无形参的方法”形式来声明，其方法名定义了该成员变量的名字，其返回值定义了该成员变量的类型
//把他们看成C#的属性。
    //    @Description(value = "年龄",color="red",age = 25)
    // @Description( "年龄")
    /*
    value():不需要指定属性名称。自动赋值给value,如@Description("年龄")
    属性实现了字段的封装，属性有get、set 。属性用来控制字段
     */
    String value();

    String color() default "blue";//为属性指定默认值

    int age() default 25;
    int salary=0;
//    Integer age1() default 25;//不支持
//    LocalDateTime time()  ;//不支持
}
