package Test.test2019.proxy;

public class ProxyImp implements IProxy {
    public String display(String name) {

        System.out.println("ProxyImp.display():" + name);
        //类内调用，没有通过动态代理对象调用，不会执行代理对象内InvocationHandler的增强
        displayA(name);
        return  "display "+name;
    }

    public void displayA(String name) {
        System.out.println("ProxyImp.displayA():" + name);
    }


}
