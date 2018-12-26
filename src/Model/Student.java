package Model;


import Test.Description;

public class Student {
    // @SuppressWarnings("")
    @Description("姓名")
    private String name;
    @Description("年龄")
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
        //  System.out.println("setAgefddffddf");
    }

    public Student() {

    }

    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
