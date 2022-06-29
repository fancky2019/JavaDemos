package Test.test2022.a;

public class ChildClassA extends ParentClassA {
    private String privateParam = "p_a";
    public String publicParam = "p_b";
    protected String protectedParam = "p_c";
    String defaultParam = "p_d";//默认 Default

    public void test() {
        String aa = this.privateParam;
        //       aa=super.privateParam; //访问不到父类私有
//        aa = super.defaultParam;//default 只能在同一包内
        aa = super.defaultParam;////default 同一包内可以访问
        aa = super.protectedParam;
        aa = super.publicParam;

        super.defaultFun();
        super.protectedFun();
        super.publicFun();
        protectedStaticFun();
        ParentClassA.protectedStaticFun();

    }

    @Override
    public void overrideFun()
    {
        //非静态可以调用静态方法，静态方法不能调用非静态方法
        staticFun();
    }

    private static void staticFun()
    {
        //Non-static method 'overrideFun()' cannot be referenced from a static context
//        overrideFun();
    }
}

class ClassAccessLevelTest1
{
    public void test() {
        ParentClassA parentClassA=new ChildClassA();
        parentClassA.defaultFun();//包内
        parentClassA.protectedFun();//子类类或包内
        parentClassA.publicFun();

    }

}
