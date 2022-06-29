package Test.test2022;

import Model.Student;
import Test.opensource.protobuf.model.PersonProto;

/*
 类：不能继承
 方法：不能重写
 成员变量：声明时候赋初值，或者构造函数中赋值  此时final=C# readonly+const
 局部变量:声明时候可以不用赋值，一旦赋值就不能改变。
 引用类型：可以改变成员值，但是不能重新实例化，指向别的地址。

 成员变量有默认值，局部变量没有默认值，使用前必须赋值
 */
public final class FinalTest {

    //
    private final int age;
    private final Student student = new Student();

    public FinalTest() {
        age = 10;
    }

    public void test() {
        student.setName("1111");//name:111,改变了name 字段值

        //不能重新实例化
        //Cannot assign a value to final variable 'student'
//        student=new Student();

        final int m;

        m = 8;
        //Variable 'm' might already have been assigned to
//        m=7;
       final int n = 0;

       final int c;
//       int cc=c;//局部变量使用前必须赋值
       int aa=age;
    }

    private void fun() {

    }

    private final void finalFun() {

    }
}
