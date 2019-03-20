package Test.test2018;


//跟C#一样类在前，接口在后
//接口多继承
//接口成员访问修饰符默认：public static final
//接口、类默认访问修饰符是default:包内,和C#一样internal程序集内
//类成员的默认访问修饰符是default:包内，C#是private
class Imp extends BaseImp implements Person, Worker {
//    @Override
//    public void getCount() {
//
//    }


    private Integer impPrivateFieldAge;
    String impDefaultFieldName;
    protected String impProtectedFieldAddress;
    public String impPublicFieldJob;

    @Override
    public void getName() {

    }

    @Override
    public Integer getAge() {
        return 0;
    }

    @Override
    public void getJob() {

    }
}

public class ImpTest {
    public void test() {
        Imp imp=new Imp() ;
     String nmae=   imp.impDefaultFieldName;
     String address=imp.impProtectedFieldAddress;
     String job=imp.impPublicFieldJob;
    }

}


