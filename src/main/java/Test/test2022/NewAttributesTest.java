package Test.test2022;

import com.google.errorprone.annotations.Var;
import io.reactivex.internal.operators.parallel.ParallelFromPublisher;

public class NewAttributesTest {

    public NewAttributesTest() {

    }

    public void test() {

    }

    private void fun() {
        //类型推断
        // 只能用于带有构造器的局部变量，以下场景不适用
        // 除了局部变量，for循环是唯一可以使用 var的地方：
        var str = "";
        //文本快
        String ss =
                """
                sd
                """;

        Object obj = new NewAttributesTest();
        if (obj instanceof NewAttributesTest newAttributesTest) {

        }

    }
}

