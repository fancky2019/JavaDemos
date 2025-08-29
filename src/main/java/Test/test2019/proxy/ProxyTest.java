package Test.test2019.proxy;

import com.google.common.base.Stopwatch;

import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * 代理模式是一种结构型设计模式，主要用于控制对对象的访问，并在访问前后添加额外逻辑。
*JDK动态代理的两个核心接口(类)分别是InvocationHandler和Proxy。注意：只能代理接口。
*CGLIB动态代理模拟
 *
 *
 *  代理模式："控制访问"（隐藏对象，增强非业务逻辑）。
 *  装饰器模式："增强功能"（透明扩展，支持嵌套组合）。
 *  两者选择取决于需求：需控制权限或隐藏细节时用代理；需动态扩展功能时用装饰器
 */
public class ProxyTest {
    public void test() {
//        IProxy proxy = (IProxy) new ProxyFactory(new ProxyImp()).getProxyInstance();
//        proxy.display("fancy");

        //返回代理对象
        IProxy proxy = new ProxyFactory(new ProxyImp()).getProxyInstance();

//        IProxy proxy = (IProxy) new ProxyFactory(new ProxyImp()).getProxyInstance();
        /* 执行代理对象的方法 ：实际执行的是代理对象实现的接口InvocationHandler的Invoke()方法，Invoke()方法
           再调用display()方法
        */
//        proxy.display("fancy");
//
//        proxy.displayA("fancy");





        int n = 0;
        //代理类内并没有实现接口的方法，只是
        IProxy proxy1 = (IProxy) Proxy.newProxyInstance(
                IProxy.class.getClassLoader(),
                new Class<?>[]{IProxy.class},
                (proxy11, method, args) -> {
                    //调用目标方法之前执行，对已有方法进行功能拓展
                    Stopwatch stopwatch = Stopwatch.createStarted();
                    System.out.println("Before Invoke");
                    //执行目标对象方法
                    //如果方法不在目标类内，报异常：object is not an instance of declaring class
                    // Object returnValue = method.invoke(this, args);
                    Object returnValue = null;
                    if (Object.class.equals(method.getDeclaringClass())) {
                        returnValue = method.invoke(this, args);
                    } else {
                        returnValue = description(args);
                    }
                    stopwatch.stop();
                    //199 milliSeconds:没有重置接着从第一次start()的时候计时
                    System.out.println(MessageFormat.format("{0} milliSeconds", stopwatch.elapsed(TimeUnit.MILLISECONDS)));

                    //调用目标方法之后执行，对已有方法进行功能拓展
                    System.out.println("After Invoke");
                    return returnValue;
                }

        );
        proxy1.display("fancy");

    }

    private Object description(Object... args) {
        System.out.println("没有走");
        return null;

    }

}
