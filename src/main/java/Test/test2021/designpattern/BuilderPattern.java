package Test.test2021.designpattern;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/*
@Builder:lombok 方式



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

/**
 *
 * // 产品类
 * public class Computer {
 *     private String cpu;
 *     private String memory;
 *     private String storage;
 *     private String graphics;
 *     private String motherboard;
 *     private String powerSupply;
 *     private String case_;
 *
 *     // 私有构造器，只能通过Builder创建
 *     private Computer() {}
 *
 *     // 只提供getter方法
 *     public String getCpu() { return cpu; }
 *     public String getMemory() { return memory; }
 *     public String getStorage() { return storage; }
 *     public String getGraphics() { return graphics; }
 *     public String getMotherboard() { return motherboard; }
 *     public String getPowerSupply() { return powerSupply; }
 *     public String getCase() { return case_; }
 *
 *     @Override
 *     public String toString() {
 *         return String.format("Computer{cpu='%s', memory='%s', storage='%s', graphics='%s'}",
 *                 cpu, memory, storage, graphics);
 *     }
 *
 *     // 静态内部Builder类
 *     public static class Builder {
 *         private Computer computer = new Computer();
 *
 *         public Builder cpu(String cpu) {
 *             computer.cpu = cpu;
 *             return this;
 *         }
 *
 *         public Builder memory(String memory) {
 *             computer.memory = memory;
 *             return this;
 *         }
 *
 *         public Builder storage(String storage) {
 *             computer.storage = storage;
 *             return this;
 *         }
 *
 *         public Builder graphics(String graphics) {
 *             computer.graphics = graphics;
 *             return this;
 *         }
 *
 *         public Builder motherboard(String motherboard) {
 *             computer.motherboard = motherboard;
 *             return this;
 *         }
 *
 *         public Builder powerSupply(String powerSupply) {
 *             computer.powerSupply = powerSupply;
 *             return this;
 *         }
 *
 *         public Builder case_(String case_) {
 *             computer.case_ = case_;
 *             return this;
 *         }
 *
 *         // 构建方法：可以添加验证逻辑
 *         public Computer build() {
 *             // 必填项验证
 *             if (computer.cpu == null) {
 *                 throw new IllegalStateException("CPU不能为空");
 *             }
 *             if (computer.memory == null) {
 *                 throw new IllegalStateException("内存不能为空");
 *             }
 *             if (computer.storage == null) {
 *                 throw new IllegalStateException("硬盘不能为空");
 *             }
 *
 *             // 设置默认值
 *             if (computer.motherboard == null) {
 *                 computer.motherboard = "标准主板";
 *             }
 *             if (computer.powerSupply == null) {
 *                 computer.powerSupply = "500W电源";
 *             }
 *
 *             return computer;
 *         }
 *     }
 * }
 *
 * // 使用示例
 * public class ClassicBuilderDemo {
 *     public static void main(String[] args) {
 *         // 创建游戏电脑
 *         Computer gamingPC = new Computer.Builder()
 *                 .cpu("Intel i9-13900K")
 *                 .memory("32GB DDR5")
 *                 .storage("1TB NVMe SSD")
 *                 .graphics("RTX 4090")
 *                 .motherboard("Z790")
 *                 .powerSupply("1000W")
 *                 .case_("游戏机箱")
 *                 .build();
 *
 *         System.out.println("游戏电脑: " + gamingPC);
 *
 *         // 创建办公电脑（使用默认值）
 *         Computer officePC = new Computer.Builder()
 *                 .cpu("Intel i5-13400")
 *                 .memory("16GB DDR4")
 *                 .storage("512GB SSD")
 *                 .build();
 *
 *         System.out.println("办公电脑: " + officePC);
 *     }
 * }
 *
 */
