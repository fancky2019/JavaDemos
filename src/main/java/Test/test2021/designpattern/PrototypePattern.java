package Test.test2021.designpattern;

/*
用一个已经创建的实例作为原型，通过复制该原型对象来创建一个和原型相同或相似的新对象
 */
public class PrototypePattern implements Cloneable {
    public void test() {
        PrototypePattern prototypePattern=new PrototypePattern();
        PrototypePattern prototypePatternClone=(PrototypePattern)prototypePattern.clone();
    }

    private void fun() {
        Object obj = new Object();

    }

    /*
    Object.clone()
    Object类的clone方法只会拷贝对象中的基本的数据类型，对于数组、容器对象、引用对象等进行浅拷贝
   */
    @Override
    public PrototypePattern clone() {
        try {
            //非原始类型要自己手动转换
            return (PrototypePattern) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
