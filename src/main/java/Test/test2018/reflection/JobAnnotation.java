package Test.test2018.reflection;
//自定义注解
//@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})


import Test.opensource.Netty.MessageType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Target({TYPE})//注解目标，只加在类上
@Retention(RetentionPolicy.RUNTIME)
public @interface JobAnnotation {

    String value();

    String description();

    //最好使用枚举
//    MessageType messageType() default MessageType.HeartBeat;
}
