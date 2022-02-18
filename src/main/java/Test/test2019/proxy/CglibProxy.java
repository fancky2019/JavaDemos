package Test.test2019.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/*
基于继承的方式实现
 */
public class CglibProxy<T> implements MethodInterceptor {
    private T target;//需要代理的目标对象

    //重写拦截方法
    @Override
    public Object intercept(Object obj, Method method, Object[] arr, MethodProxy proxy) throws Throwable {
        System.out.println("Cglib动态代理，监听开始！");
//        return null;  //如果方法有返回结果就返回，没有就返回null
        Object invoke = method.invoke(target, arr);//方法执行，参数：target 目标对象 arr参数数组
        System.out.println("Cglib动态代理，监听结束！");
        return invoke;
    }

    //定义获取代理对象方法
    public T getCglibProxy(T objectTarget) {
        //为目标对象target赋值
        this.target = objectTarget;
        Enhancer enhancer = new Enhancer();
        //设置父类,因为Cglib是针对指定的类生成一个子类，所以需要指定父类
        enhancer.setSuperclass(objectTarget.getClass());
        enhancer.setCallback(this);// 设置回调
        T result = (T) enhancer.create();//创建并返回代理对象
        return result;
    }

    public void test() {
        CglibProxy<ProxyImp> cglib = new CglibProxy<>();//实例化CglibProxy对象
        ProxyImp proxyImp = cglib.getCglibProxy(new ProxyImp());//获取代理对象
//        IProxy proxy =  cglib.getCglibProxy(new ProxyImp());//获取代理对象
        String str = proxyImp.display("admin");//执行删除方法

        int m = 0;
    }
}
