package Test.test2021.designpattern.builder;

import lombok.Data;
import lombok.Getter;

@Getter
public class BuilderTest {
    private String name;
    private Integer age;

//不提供@Setter智能通过构造者模式生成对象
//    public  BuilderTest()
//    {
//
//    }


    private BuilderTest(BuilderTestBuilder builder) {
        this.name = builder.name;
        this.age = builder.age;
    }

//    public static final class Builder{}
    @Data
    public static class BuilderTestBuilder {
        private String name;
        private Integer age;

        public BuilderTestBuilder () {
        }

        public BuilderTestBuilder (String name, int age) {
            this.name =name;
            this.age = age;
        }

        public BuilderTestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public BuilderTestBuilder age(int age) {
            this.age = age;
            return this;
        }

        public BuilderTest builder()
        {
            //增减校验逻辑
            if (this.getAge() < 0 || this.getAge() > 255) {
                throw new IllegalStateException("Age out of range:" + this.getAge());// 线程安全
            }
            return new BuilderTest(this);
        }

    }
}
