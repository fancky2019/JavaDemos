package Test.test2021.extendImplement;


@FunctionalInterface
public interface InterfaceB {
    String funA();

    static String fun1() {
        return "";
    }

    default String fun2() {
        return "";
    }
}
