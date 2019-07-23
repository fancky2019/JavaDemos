package Test.test2019;

import Test.test2018.Person;
import Test.test2018.PersonImp;

public class TypeJudgeTest {
    public void test() {
        typeJudge();
    }

    private void typeJudge() {
        PersonImp imp = new PersonImp();

        //instanceof:判断某个对象是否是某个类的实例或者某个类的子类的实例
        if (imp instanceof PersonImp) {
            System.out.println("imp instanceof PersonImp");
        }

        //Class.equals():一定是本身才返回true，子类或者父类都返回false
        if (imp.getClass().equals(PersonImp.class)) {
            System.out.println("imp.getClass().equals(PersonImp.class)");
        }

        //Class.isInstance():instanceof完全等价
        if (PersonImp.class.isInstance(imp)) {
            System.out.println("PersonImp.class.isInstance(imp)");
        }

        //反射用此种方法比较多
        //  Class.isAssignableFrom:   判断某个类是否是另一个类和其子类。
        // isAssignableFrom(子类.class)
        if (Person.class.isAssignableFrom(imp.getClass())) {
            System.out.println("PersonImp.class.isAssignableFrom(Person.class)");
        }
    }


}
