package Test.test2018;

import Test.test2018.Person;

public class PersonImp implements Person {
    //Final 必须赋初值，可以不是静态的。一般声明为静态
    public final Integer SUM = 10;
    public static final Integer SUM1 = 10;

    @Override
    public Integer getAge() {
        return 0;
    }

    @Override
    public Integer getCout() {
        return 0;
    }

    @Override
    public void getName() {
        try {
            System.out.println("getName");
            testException();
        } catch (Exception ex) {
            String s = ex.getMessage();
            String str = ex.toString();
        }
    }

    //异常和C#处理一样
    private void testException() throws NumberFormatException {
//        try {
        Integer m = 4;
        Integer a = Integer.parseInt("t");
        int n = 6;
//        }
//        catch (Exception ex)
//        {
//            throw  ex;
//        }


    }

}
