package Test.test2019.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/*
基于继承的方式实现
 */
public class CglibProxy<T> implements MethodInterceptor {
    private T target;//需要代理的目标对象

    //重写拦截方法
    @Override
    public Object intercept(Object obj, Method method, Object[] arr, MethodProxy proxy) throws Throwable {
        /**
         * arr 参数值
         */
        System.out.println("Cglib动态代理，监听开始！");

        Parameter[] parameters = method.getParameters();
        //获取参数
        for (Parameter parameter : parameters) {
            //参数名称
            String parameterName = parameter.getName();
            //类型名称
            String parameterName1 = parameter.getType().getSimpleName();
            int nn = 0;

        }
        for (int i = 0; i < arr.length; i++) {
            Parameter parameter = method.getParameters()[i];
            String paramName = parameter.getName();
            //  parameter.getAnnotation()
            int mm = 0;
        }


        //获取参数类型
        for (Class<?> cla : method.getParameterTypes()) {
            //类型名称
            String parameterName = cla.getSimpleName();
            int nn = 0;
        }
        Long lp = 0L;
        String name = lp.getClass().getSimpleName();
//        return null;  //如果方法有返回结果就返回，没有就返回null
        Object invoke = method.invoke(target, arr);//方法执行，参数：target 目标对象 arr参数数组


        System.out.println("Cglib动态代理，监听结束！");
        return invoke;
    }

    //定义获取代理对象方法
//    @SuppressWarnings("unchecked") //去处类型checked校验
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
