package Test.test2021.extendImplement;

public abstract class BaseClass implements InterfaceA {

    /*
     * 指定抽象不提供具体实现
     * 指定public 访问修饰符实现接口
     */
    public abstract String funA();

    @Override
    public String funB() {
        return "BaseClass+funB";
    }
}
