package Model;


import Test.test2018.Description;

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


//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (obj instanceof Student) {
//            Student student = (Student) obj;
//            return this.getName() == student.getName() && this.getAge() == student.getAge();
//        } else {
//            return false;
//        }
//    }
}
