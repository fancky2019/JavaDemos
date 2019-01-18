package Test.test2018;

//接口多继承
//接口成员访问修饰符默认：public static final
//接口、类默认访问修饰符是default:包内,和C#一样internal程序集内
//类成员的默认访问修饰符是default:包内，C#是private
 public  interface Person extends Animal, Children {
    //接口成员访问修饰符默认：public static final
    Integer COUNT = 10;

    void getName();
}

interface Animal {
    Integer getAge();
}

interface Children {
    default Integer getCout() {
        return 1;
    }
}

interface Worker {
    void getJob();
}

class BaseImp {

}


