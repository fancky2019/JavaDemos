package Test.test2021;

/*
 * C#访问修饰符对重载没有影响，java继承的重载不能比父类的访问修饰符窄
 */
public class OverLoadTest extends OverLoadParent {
    //编译通过
//    protected String getName()
//    {
//        return "name1";
//    }

//    //编译通过
//    public String getName()
//    {
//        return "name";
//    }


//    //编译不通过
//    private String getName()
//    {
//        return "name";
//    }
}

class OverLoadParent {
    protected String getName() {
        return "name";
    }

}
