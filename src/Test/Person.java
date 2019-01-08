package Test;

//接口多继承
//接口成员访问修饰符默认：public static final
//接口、类默认访问修饰符是default:包内,和C#一样internal程序集内
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

//跟C#一样类在前，接口在后
class Imp extends BaseImp implements Person, Worker {
//    @Override
//    public void getCout() {
//
//    }

    @Override
    public void getName() {

    }

    @Override
    public Integer getAge() {
         return  0;
    }

    @Override
    public void getJob() {

    }
}
