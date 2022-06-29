package Test.test2022.b;

import Test.test2022.a.ChildClassA;
import Test.test2022.a.ParentClassA;

/*
在JAVA中有四种访问控制权限，分别为：private, default, protected, public:
1、Private
如果一个成员方法或变量名前使用了private， 那么这个成员只能在类的内部使用。
2、Default
如果一个成员方法或变量名前没有使用任何访问控制符，就为default。默认的访问控制成员可以被这个包中的其它类访问
（包括这个包中的子类内部以及这个包中所有类以对象.成员的方式访问）。
如果这个子类不在这个包中，则子类也不能访问父类的默认访问控制成员
3、Protected
这个成员既可以被这个包中其它类访问，也可以被不同包中的子类内部访问
4、Public
可以被所有类访问，不管访问类与被访问类在不在同一个包中
 */
public class ChildClassB extends ParentClassA {

    private String privateParam="p_a";
    public String publicParam="p_b";
    protected String protectedParam="p_c";
    String defaultParam="p_d";//默认 Default
    public void test()
    {
       String aa=this.privateParam;
//       aa=super.privateParam; //访问不到父类私有
//        aa = super.defaultParam;//default 只能在同一包内
        aa = super.protectedParam;
        aa = super.publicParam;

        super.protectedFun();
        super.publicFun();
    }

}

class ClassAccessLevelTest2
{
    public void test() {
        ParentClassA parentClassA=new ChildClassA();
//        parentClassA.defaultFun();//包内
//        parentClassA.protectedFun();//子类类或包内
        parentClassA.publicFun();

    }

}
