package Test.test2021.extendImplement;

@FunctionalInterface
public interface InterfaceC {
    void display();
    static String fun1()
    {
        return "";
    }

    default String fun2()
    {
        return "";
    }
}
