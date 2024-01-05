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



SPI（ Service Provider Interface）服务发现机制
配置的key就是接口完整限定名，value就是接口的各个实现类，用","号隔开。
META-INF文件夹spring.factories文件


组合注解:
@Retention修饰注解，用来表示注解的生命周期，生命周期的长短取决于@Retention的属性RetentionPolicy指定的值
@Documented 元注解 生成文档(idea--tools--generate javadoc)是否包含此类或注解
@Inherited 如果父类加了Inherited修饰的注解，子类默认会拥有父类@nherited修饰的注解
@Configuration标注在某个类上，表示这是一个 springboot的配置类。可以向容器中注入组件。内部继承@Component注解
@Component 注入到容器,类
@Bean 方法
@ComponentScan 果没有定义特定的包，将从“声明该注解的类的包”开始扫描。
@Indexed之后，编译打包的时候会在项目中自动生成META-INT/spring.components文件。
当Spring应用上下文执行ComponentScan扫描时，META-INT/spring.components将会被CandidateComponentsIndexLoader
 读取并加载，转换为CandidateComponentsIndex对象，这样的话@ComponentScan不在扫描指定的package，
 而是读取CandidateComponentsIndex对象，从而达到提升性能的目的。




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

    int salary = 0;
//    Integer age1() default 25;//不支持
//    LocalDateTime time()  ;//不支持
}
