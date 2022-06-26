package Test.test2018;

public class ExtendTest extends ParentTest {

    public void test() {

    }

    /**
     * @Override可以不加，加了可以检查Method does not override method from its superclass
     */
    @Override
    public void publicFunction() {
        System.out.println("ExtendTest:publicFunction()");
    }

    public void publicFunction1() {
        System.out.println("ExtendTest:publicFunction1()");
    }

    public AbstractParent getAbstractParent()
    {
        //直接new 抽象类或接口
        return new AbstractParent() {
            @Override
            void fun1() {

            }
        };
    }

}

class ParentTest extends AbstractParent {

    protected void protectedFunction() {
        System.out.println("ParentTest:protectedFunction()");
    }

    public void publicFunction() {
        System.out.println("ParentTest:publicFunction()");
    }

    public void publicFunction1() {
        System.out.println("ParentTest:publicFunction1()");
    }

    public void publicFunction2() {
        System.out.println("ParentTest:publicFunction2()");
    }

    @Override
    void fun1() {
        
    }
}

abstract class AbstractParent {
    private int age;

    abstract void fun1();
}
