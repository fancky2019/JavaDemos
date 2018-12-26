package Test;

import java.util.ArrayList;
import java.util.List;

public class GenericTest {

    private void test() {
        GenTest<? extends Object> a = new GenTest<>();
        GenaTest<Integer> genaTest = new GenaTest<>();
        funVoid(Integer.class);

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

    class GenaTest<T> {

    }
}
