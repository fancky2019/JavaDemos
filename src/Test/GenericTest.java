package Test;

import java.util.ArrayList;
import java.util.List;

public class GenericTest {

    private void test() {
        GenTest<? extends Object> a = new GenTest<>();
        GenaTest<Integer> genaTest = new GenaTest<>();
        funVoid(Integer.class);
        try {
            funT(Integer.class);
        } catch (Exception ex) {

        }

        //java 泛型类可不带类型变量
        GenaTest.Display();
        GenaTest.Display1();
        GenaTest genaTest1 = new GenaTest();
        genaTest1.Display2();
        GenaTest<String> genaTest2 = new GenaTest<>();
    }

    public <T> void funVoid(Class<T> clazz) {

    }

    public <T> T funT(Class<T> clazz) throws Exception {
        T t = clazz.newInstance();
        return t;
    }

    class GenTest<T> {
        public void test() {

        }
    }


}

class GenaTest<T> {

    static void Display() {

    }

    static <T> void Display1() {

    }

    <T> void Display2() {

    }
}
