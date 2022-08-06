package Test.test2021.extendImplement;

import java.util.ArrayList;

public class ExtendImplementTest {
    public void test() {
        InterfaceA interfaceA = new ChildrenClassA();
        System.out.println(interfaceA.funA());
        System.out.println(interfaceA.funB());
        InterfaceA interfaceA1 = new ChildrenClassB();
        System.out.println(interfaceA1.funA());
        System.out.println(interfaceA1.funB());

        ChildrenClassA childrenClassA = new ChildrenClassA();
        childrenClassA.funD();

        ArrayList<?> list = new ArrayList<Integer>();
//        list.add(1);//因为制定的类型是？，只能添加类型是？的数据，就造成list只能读。
        ArrayList<? extends ChildrenClassA> arrayListA = new ArrayList<>();

        ArrayList<? extends ChildrenClassB> arrayListB = new ArrayList<>();
        String strA = arrayListA.getClass().getSimpleName();//ArrayList
        String strB = arrayListB.getClass().getSimpleName();//ArrayList

        //ArrayList: 类型擦除后没有了类型
        boolean result = arrayListA.getClass().equals(arrayListB.getClass());//true

//        ChildrenClassA.super.funD();

        GenericsErasureD<ChildrenClassB> d = new GenericsErasureD<>(new ChildrenClassB());
        GenericsErasureC<ChildrenClassB> d1 = new GenericsErasureC<>(new ChildrenClassB());
        String name1 = d.getObj().getClass().getSimpleName();
        String name11 = d1.getObj().getClass().getSimpleName();
        boolean re11 = d.getObj().getClass().equals(d1.getObj().getClass());//trur
        int m = 0;
    }


}

class GenericsErasureD<T extends BaseClass> {
    private T obj;

    public GenericsErasureD(T obj) {
        this.obj = obj;
    }

    public T getObj() {
        return obj;
    }
}

class GenericsErasureC<T> {
    private T obj;

    public GenericsErasureC(T obj) {
        this.obj = obj;
    }

    public T getObj() {
        return obj;
    }
}
