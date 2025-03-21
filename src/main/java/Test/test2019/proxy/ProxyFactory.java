package Test.test2019.proxy;

import com.google.common.base.Stopwatch;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * JDK代理：
 * 创建动态代理对象
 * 动态代理不需要实现接口,但是需要指定接口类型
 *
 * ProxyFactory<T>
 */
public class ProxyFactory {
    private Object interfaceImp;

    /**
     * @param interfaceImp 接口的实现类
     */
    public ProxyFactory(Object interfaceImp) {
        this.interfaceImp = interfaceImp;
    }


    //给目标对象生成代理对象
    public <T> T getProxyInstance() {
//        return Proxy.newProxyInstance(
//                target.getClass().getClassLoader(),
//                target.getClass().getInterfaces(),
//                new InvocationHandler() {
//                    @Override
//                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                        System.out.println("开始事务2");
//                        //执行目标对象方法
//                        Object returnValue = method.invoke(target, args);
//                        System.out.println("提交事务2");
//                        return returnValue;
//                    }
//                }
//        );

        /*
       参数 (ClassLoader loader, Class<?>[] interfaces,InvocationHandler h)
         */
//        return Proxy.newProxyInstance(
//                interfaceImp.getClass().getClassLoader(),
//                interfaceImp.getClass().getInterfaces(),
//                (proxy, method, args) -> {
//                    //调用目标方法之前执行，对已有方法进行功能拓展
//                    Stopwatch stopwatch = Stopwatch.createStarted();
//                    System.out.println("Before Invoke");
//                    //执行目标对象方法
//                    Object returnValue = method.invoke(interfaceImp, args);
//
//                    stopwatch.stop();
//                    //199 milliSeconds:没有重置接着从第一次start()的时候计时
//                    System.out.println(MessageFormat.format("{0} milliSeconds", stopwatch.elapsed(TimeUnit.MILLISECONDS)));
//
//                    //调用目标方法之后执行，对已有方法进行功能拓展
//                    System.out.println("After Invoke");
//                    return returnValue;
//                }
//
//        );

        /**
          jdk 内部会生成 实现接口  final  修饰的方法

         loader :类加载器，用于加载代理对象。
         interfaces : 被代理类实现的一些接口；
         h : 实现了 InvocationHandler 接口的对象；
         */

        T t= (T)Proxy.newProxyInstance(
                interfaceImp.getClass().getClassLoader(),
                interfaceImp.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    //调用目标方法之前执行，对已有方法进行功能拓展
                    Stopwatch stopwatch = Stopwatch.createStarted();
                    System.out.println("Before Invoke");
                    //执行目标对象方法
                    Object returnValue = method.invoke(interfaceImp, args);
//                    Object returnValue=null;
//                    if (Object.class.equals(method.getDeclaringClass())) {
//                         returnValue = method.invoke(this, args);
//                    } else {
//                        returnValue = description( args);
//                    }
                    stopwatch.stop();
                    //199 milliSeconds:没有重置接着从第一次start()的时候计时
                    System.out.println(MessageFormat.format("{0} milliSeconds", stopwatch.elapsed(TimeUnit.MILLISECONDS)));

                    //调用目标方法之后执行，对已有方法进行功能拓展
                    System.out.println("After Invoke");
                    return returnValue;
                }

        );
        return  t;

    }
    private Object description( Object... args)
    {
        System.out.println("没有走");
        return  null;

    }

}