package Test.test2019;

/**
 * java 里没有静态构造函数，可以用静态块代替
 *
 *super 必须是构造函数里的第一句
 *
 */
public class ConstructorTest {

    public void test() {
        ConstructorChild constructorChild = new ConstructorChild("fancky");
        Integer m = 0;
    }

}

class ConstructorParent {
    private Integer age;
    private String name;
    private String parent = "parent";
    private static String staticParent = "staticParent";

    {
        System.out.println("ConstructorParent");
    }
     //java 里没有静态构造函数，可以用静态块代替
    static {
        System.out.println("staticConstructorParent");
    }

    ConstructorParent(String name) {
        //有点像C#的串联构造函数
        this(name, 0);
    }

    ConstructorParent(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}

class ConstructorChild extends ConstructorParent {
    private String address;
    private Double salary;
    private String child = "child";
    private static String staticChild = "staticChild";

    {
        System.out.println("ConstructorChild");
    }

    static {
        System.out.println("staticConstructorChild");
    }

    ConstructorChild(String name) {
        this(name, 0, "", 0d);
    }

    ConstructorChild(String name, String address) {
        this(name, 0, address, 0d);
    }

    ConstructorChild(String name, Integer age, String address, Double salary) {
        //super 必须是构造函数里的第一句
        super(name, age);
        this.address = address;
        this.salary = salary;
    }
}

