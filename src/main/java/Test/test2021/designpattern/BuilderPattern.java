package Test.test2021.designpattern;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/*
一般用于配置文件等只读的类

核心思想是：通过builder ，将builder对象成员赋值给 对象（builder成员和对象成员完全匹配）
 */
@Getter
//@Builder //不会赋值默认值
public class BuilderPattern {
    private String name;
    private Integer age;

//不提供@Setter智能通过构造者模式生成对象
//    public  BuilderTest()
//    {
//
//    }

    public static void test()
    {
        BuilderPattern builderPattern=new Builder()
                .name("fancky")
                .age(27)
                .builder();
    }

    //将builder 成员赋值对象成员
    private BuilderPattern(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
    }

//    public static final class Builder{}
    @Data
    public static class Builder {
        private String name;
        private Integer age;

        public Builder() {
        }

        public Builder(String name, int age) {
            this.name =name;
            this.age = age;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public BuilderPattern builder()
        {
            //增减校验逻辑
            if (this.getAge() < 0 || this.getAge() > 255) {
                throw new IllegalStateException("Age out of range:" + this.getAge());// 线程安全
            }
            return new BuilderPattern(this);
        }

    }
}
