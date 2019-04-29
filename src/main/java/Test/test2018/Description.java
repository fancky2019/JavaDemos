package Test.test2018;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDateTime;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;

/*
注解元素数据类型：
所有基本类型（int,float,boolean,byte,double,char,long,short）
String
Class（不是类，是(type)）
enum
Annotation
上述类型的数组
*/

//自定义注解
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {

    //    @Description(value = "年龄",color="red",age = 25)
    // @Description( "年龄")
    String value();
    String color() default "blue";//为属性指定缺省值
    int age() default 25;
//    Integer age1() default 25;//不支持
//    LocalDateTime time()  ;//不支持
}
