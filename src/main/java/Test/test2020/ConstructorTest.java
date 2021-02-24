package Test.test2020;

import Model.Student;

import java.math.BigDecimal;

public class ConstructorTest extends Student {
    private BigDecimal salary;
    private String address;

    public ConstructorTest() {
        //this或super必须放在构造函数的第一行
        this(BigDecimal.ZERO);
    }

    public ConstructorTest(BigDecimal salary) {
        //this或super必须放在构造函数的第一行
        this(salary, "fancky","name",0);
    }
    public ConstructorTest(BigDecimal salary, String address) {
        //this或super必须放在构造函数的第一行
        this(salary,address,"fancky", 3);

    }
    public ConstructorTest(BigDecimal salary, String address,String name,Integer age) {
        //this或super必须放在构造函数的第一行
        super(name, age);
        this.salary = salary;
        this.address = address;
    }

    public void tets() {

    }
}


