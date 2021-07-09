package Test.test2021;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

public class RandomTest {
    public void test() {
        fun();
    }

    private void fun() {
        Random random = new Random(1);
        int r1 = random.nextInt(10000);
        int r2 = random.nextInt(10000);
        int n = 0;
    }
}
