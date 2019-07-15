package Model;


import Test.test2018.Description;

import java.io.Serializable;

/**
 * json序列化，不需要Serializable接口
 */
public class Student extends StudentParent implements StudentInterface   {
    // @SuppressWarnings("")
    @Description("姓名")
    private String name;
    @Description(value = "年龄",color="red",age = 25)
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
