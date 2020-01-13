package Model;


import Test.test2018.Description;
import com.fasterxml.jackson.databind.ser.Serializers;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Random;

/**
 * json序列化，不需要Serializable接口
 * Idea 生成equals和hashCode ：Alt+ Insert-->选中"equals and hashCode"-->选中字段
 *
 * 如果不重写hashCode，则返回的是对象的内存地址。
 */
public class Student extends StudentParent implements StudentInterface {
    // @SuppressWarnings("")
    @Description("姓名")
    private String name;
    @Description(value = "年龄", color = "red", age = 25)
    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private  Integer id;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(name, student.name) &&
                Objects.equals(age, student.age);
    }

    /**
     * 如果不重写hashCode，则返回的是对象的内存地址的整数值。
     * HashCode不同， HashSet<Person> 对此调用同一对象add,只添加一次
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }


    /**
     * HashCode相同， HashSet<Person> 对此调用同一对象add,添加多次，不能判断重复。
     * @return
     */
//    @Override
//    public int hashCode() {
//      // nextInt(100): [0, 100)
//        return new Random().nextInt(100);
//    }

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

    @Override
    public  String toString()
    {
        return MessageFormat.format( "Name:{0},Age:{1}",this.name,this.age);
    }
}
