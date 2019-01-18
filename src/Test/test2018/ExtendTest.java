package Test.test2018;

public class ExtendTest extends ParentTest {

    public void test()
    {

    }

    /**
     *
     @Override可以不加，加了可以检查Method does not override method from its superclass
     */
    @Override
    public void  publicFunction()
    {
        System.out.println("ExtendTest:publicFunction()");
    }

    public void  publicFunction1()
    {
        System.out.println("ExtendTest:publicFunction1()");
    }

}

class ParentTest
{

    protected void protectedFunction()
    {
        System.out.println("ParentTest:protectedFunction()");
    }

    public void  publicFunction()
    {
        System.out.println("ParentTest:publicFunction()");
    }

    public void  publicFunction1()
    {
        System.out.println("ParentTest:publicFunction1()");
    }

    public void  publicFunction2()
    {
        System.out.println("ParentTest:publicFunction2()");
    }
}
