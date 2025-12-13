package Test.test2020;

/**
 *
 *      *lombok不会生成Getter Setter
 *      * @data lombox 生成的.class (应idea打开)会先生成get,然后生成set hashcode toString
 *
 *

一、安装LomBok Plugin 插件。
二、添加依赖 <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <!--            <version>1.18.12</version>-->
            <!--            <scope>provided</scope>-->
        </dependency>


        @Data相当于@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode这5个注解的合集

        在类是继承父类的情况下：
EqualsAndHashCode实则就是在比较两个对象的属性；
当@EqualsAndHashCode(callSuper = false)时不会比较其继承的父类的属性可能会导致错误判断；
当@EqualsAndHashCode(callSuper = true)时会比较其继承的父类的属性；


不需要编译其他类就可以访问到getter、setter

    /
     *lombok不会生成Getter Setter
     * @data lombox 生成的.class (应idea打开)会先生成get,然后生成set hashcode toString


Lombok默认不处理静态字段：Lombok的@Getter和@Setter注解默认只对实例字段生成方法，不会为静态字段生成静态getter/setter。

即使手动指定也不会生成：
 @Getter // 这个不会为静态字段生成getter
 private static volatile PageData instance;
 */

import lombok.*;

/**
 * @Auther fancky
 * @Date 2020-10-26 10:50
 * @Description
 */

@Data
//或者只用
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder//有问题
public class LomBoxTest {
}
