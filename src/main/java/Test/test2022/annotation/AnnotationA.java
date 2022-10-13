package Test.test2022.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD,})//注解目标，只加在类上
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AnnotationA {

}
