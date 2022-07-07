package Test.test2021.extendImplement;

public abstract class BaseClass implements InterfaceA,InterfaceB,InterfaceC {

    /*
     * 指定抽象不提供具体实现
     * 指定public 访问修饰符实现接口
     */
    public abstract String funA();

    @Override
    public void display() {

    }

    /*
     继承的多个接口有相同的默认方法，实现类必须重写默认方法
     */
    @Override
    public String fun2() {
        return InterfaceB.super.fun2();
    }


    @Override
    public String funB() {
        return "BaseClass+funB";
    }

    private String funC() {
        return "BaseClass+funB";
    }

    public String funD() {
        return "BaseClass+funB";
    }
}
