package Test.test2021.designpattern;

/**
 *
 *
 * 1、单一职责原则	Single Responsibility Principle (SRP)	一个类只做一件事
 * 2. 开闭原则	Open-Closed Principle (OCP)	对扩展开放，对修改关闭
 * 3. 里氏替换原则	Liskov Substitution Principle (LSP)	子类必须能替换父类
 * 4. 接口隔离原则	Interface Segregation Principle (ISP)	接口要小而专一
 * 5. 依赖倒置原则	Dependency Inversion Principle (DIP)	依赖抽象，不依赖具体。要面向接口编程，而不是面向实现编程
 *                 定义：
 *                     高层模块不应该依赖低层模块，两者都应该依赖抽象
 *                     抽象不应该依赖细节，细节应该依赖抽象
 * 6. 迪米特法则	Law of Demeter (LoD)	最少知识原则 （尽量不要公开内部对象，通过封装返回对象成员信息）
 * 7. 组合复用原则	Composition Over Inheritance	优先使用组合而非继承   组合优于继承
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *适配器模式：桥接，中转使其支持某种功能
 *对外提供的接口支持现有的类功能：接口的实现类的方法实现中使用现有的实现类
 *
 * 使用：构造函数传入需要适配的类
 *
 *
 * 装饰器模式关注功能增强，要求装饰器和被装饰对象实现同一接口；适配器模式关注接口兼容，将一个接口转换成客户端期望的另一个接口
 */
public class AdapterPattern {

    /**类适配器（继承）
     // 目标接口：客户端期望的接口
     public interface Target {
     void request();
     }

     // 适配者：需要被适配的现有类
     public class Adaptee {
     public void specificRequest() {
     System.out.println("适配者的特定请求");
     }
     }

     // 类适配器：通过继承适配者，实现目标接口
     public class ClassAdapter extends Adaptee implements Target {
    @Override public void request() {
    // 调用父类的特定方法
    specificRequest();
    }
    }

     // 使用示例
     public class ClassAdapterDemo {
     public static void main(String[] args) {
     Target target = new ClassAdapter();
     target.request(); // 输出：适配者的特定请求
     }
     }
     */

    /** 2、 对象适配器（组合）： 构造函数包装需要适配的类对象
     *
     // 对象适配器：通过组合适配者，实现目标接口
     public class ObjectAdapter implements Target {
     private Adaptee adaptee;

     public ObjectAdapter(Adaptee adaptee) {
     this.adaptee = adaptee;
     }

     @Override public void request() {
     // 调用适配者的方法
     adaptee.specificRequest();
     }
     }

     // 使用示例
     public class ObjectAdapterDemo {
     public static void main(String[] args) {
     Adaptee adaptee = new Adaptee();
     Target target = new ObjectAdapter(adaptee);
     target.request(); // 输出：适配者的特定请求
     }
     }
     */
}
