package Test.test2021.extendImplement;

public class ChildrenClassB extends BaseClass{
    public String funD() {
        return "BaseClass+funB";
    }
    @Override
    public String funA() {
        return "ChildrenClassB+funA";
    }

}
