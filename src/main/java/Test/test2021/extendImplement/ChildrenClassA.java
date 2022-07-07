package Test.test2021.extendImplement;

public class ChildrenClassA extends BaseClass{

    public String funD() {
        super.funD();
        return "BaseClass+funB";
    }
    @Override
    public String funA() {
        return "ChildrenClassA+funA";
    }

    @Override
    public String funB() {
        return "ChildrenClassA+funA";
    }
}
