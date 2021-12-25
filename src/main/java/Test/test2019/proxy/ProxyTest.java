package Test.test2019.proxy;

/*
JDK动态代理的两个核心接口(类)分别是InvocationHandler和Proxy。注意：只能代理接口。

CGLIB动态代理模拟
 */
public class ProxyTest {
    public void test() {
        //  IProxy proxy = (IProxy) new ProxyFactory( new ProxyImp()).getProxyInstance();

        //返回代理对象
        IProxy proxy = (IProxy) new ProxyFactory(new ProxyImp()).getProxyInstance();
        /* 执行代理对象的方法 ：实际执行的是代理对象实现的接口InvocationHandler的Invoke()方法，Invoke()方法
           再调用display()方法
        */
        proxy.display("fancy");

        proxy.displayA("fancy");
    }
}
