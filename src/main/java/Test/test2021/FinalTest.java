package Test.test2021;

import Model.Student;

/*
修饰类成员变量：
final=C# readonly+const
修饰局部变量：不用赋初始值


修饰方法：不能重写
修饰类  :不能继承
 */
public class FinalTest {
    public static final String NAME = "name";

    public final String address;

    public FinalTest() {
        address = "address";
    }

    public void test() {

    }

    /*
    局部变量不用赋初始值
     */
    private void fun() {
        final int age = 0;
        final int age1;
        age1=12;
        final Student student ;
    }

}
