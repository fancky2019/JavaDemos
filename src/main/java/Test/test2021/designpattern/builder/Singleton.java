package Test.test2021.designpattern.builder;


/*
频繁创建销毁、工具类、耗时耗资源对象。
 */
public class Singleton {

    //类外外部禁用new
    private Singleton() {}

    //region  饿汉

//    private static final Singleton INSTANCE = new Singleton();
//    public static Singleton getInstance() {
//        return INSTANCE;
//    }

//    private static final Singleton INSTANCE;
//
//    //java 里没有静态构造函数，可以用静态块代替
//    static {
//        INSTANCE = new Singleton();
//    }
//
//    public static Singleton getInstance() {
//        return INSTANCE;
//    }
//endregion


    //region  双检索
//    private static volatile  Singleton instance;
//    public static Singleton getInstance() {
//        if(instance==null)
//        {
//            //注意静态方法 锁当前累的class  不是 this
//            synchronized (Singleton.class)
//            {
//                if(instance==null)
//                {
//                    instance=new Singleton();
//                }
//            }
//        }
//        return instance;
//    }
    //endregion


    //region 静态内部类,比较饿汉模式可以延迟加载，但是占用更多资源
    private static class StaticInnerClass{
        private static final Singleton instance=new Singleton();
    }

    public static Singleton getInstance() {
        return StaticInnerClass.instance;
    }
        //endregion
}
