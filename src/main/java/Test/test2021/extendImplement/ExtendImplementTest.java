package Test.test2021.extendImplement;

public class ExtendImplementTest {
    public  void test()
    {
        InterfaceA interfaceA=new ChildrenClassA();
        System.out.println(interfaceA.funA());
        System.out.println(interfaceA.funB());
        InterfaceA interfaceA1=new ChildrenClassB();
        System.out.println(interfaceA1.funA());
        System.out.println(interfaceA1.funB());

        ChildrenClassA childrenClassA=new ChildrenClassA();
        childrenClassA.funD();

//        ChildrenClassA.super.funD();
    }
}
