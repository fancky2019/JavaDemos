package Test.test2018;

import Model.Student;
import Model.StudentInterface;
import Model.StudentParent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectDemo {
    public void test() throws ClassNotFoundException {
        try {
            //类型判断
            Object obj = new Student();
            boolean result = obj instanceof Student;

            //java 中的Class就像C#里Type.
            //创建class的三种方式
            Student student = new Student();
            Class<?> clazz = student.getClass();
            Class<Student> clas = Student.class;
            Class<?> classForName = Class.forName("Model.Student");
            //class创建对象的两种方式
            Student st = (Student) clazz.newInstance();
            Constructor<?> constructor = clazz.getConstructor();
            Student stude = (Student) constructor.newInstance();

            //clazz.newInstance();//实例化一个类，前提是该类存在无参构造参数

            //得到构造器，String.class是即将实例化类clazz的构造参数的类型,Integer.class 第二个参数
            Constructor<?> constructorParam = clazz.getConstructor(String.class, Integer.class);
            //传入构造函数的参数进行实例化
            Student objParam = (Student) constructorParam.newInstance("test", 23);


            String className = clas.getName();
            Class<?> cls = Class.forName(className);

            //是否继承类、接口
            Boolean extendss = StudentParent.class.isAssignableFrom(cls);
            Boolean extendsI = StudentInterface.class.isAssignableFrom(cls);

            //对象判断
            Boolean ree = student instanceof StudentParent;
            Boolean rei = student instanceof StudentInterface;

            //getField()只能获取公有属性字段，getDeclaredField()能获取全部属性字段。
            // 其中还需要注意的时getDeclaredField()方法获取私有属性字段时需要将 设置accessible属性为true
            //反射字段、方法
            Field[] fields = clas.getDeclaredFields();
            //给字段赋值
            if (!fields[0].isAccessible()) {
                //设置字段可访问
                fields[0].setAccessible(true);
            } else {
                //取字段值
                fields[0].get(clas.newInstance());
            }
            //字段赋值
            fields[0].set(clas.newInstance(), "fancky");


            Method[] methods = clas.getDeclaredMethods();

            //反射调用方法
            Method method = cls.getDeclaredMethod("setAge", Integer.class);
            //反射创建实例
            Object object = cls.newInstance();
            //调用无参构造函数
            Object instance = cls.getConstructor().newInstance();


            method.invoke(object, 2);


            //必须是某实例才能强制转换，否则抛异常
            if (object instanceof Student) {
                //前面已经通过反射给字段age赋值
                Student stu = (Student) object;
                System.out.println(stu.getAge());
            }

            //获取字段上的注解
            Field field = clas.getDeclaredField("name");
            Description description = field.getAnnotation(Description.class);
            String des = description.value();

            //是否加了某某注解
            Boolean isAnnotationPresent = field.isAnnotationPresent(Description.class);


            //Class 用作方法参数时候用Class<?>，最好指定上下界或者指定参数类型
            Class<?> voidClass1 = Void.class;
            Class<Void> voidClass = Void.class;
            String voidClassName = voidClass.getSimpleName();
            voidClass.getTypeName();
            //void
            String voidClassName1 = Void.TYPE.getSimpleName();

            //void
            String voidClassLowerCase = Void.TYPE.getSimpleName().toLowerCase();
            //Void的构造函数是私有的不能用newInstance
//            Void v1 = voidClass.newInstance();
            if (Void.TYPE.equals(Void.class)) {//false
                int m = 0;
            }

            if (Void.TYPE.getSimpleName().toLowerCase().equals(Void.class.getSimpleName().toLowerCase()))//true
            {
                int n = 0;
            }

            if (Void.TYPE.getSimpleName().equalsIgnoreCase(Void.class.getSimpleName()))//true
            {
                int n = 0;
            }
            System.out.println();  //插入断点
        } catch (Exception ex) {
            String msg = ex.getMessage();
            //   throw ex;

        }

    }

    private Void funVoid() {
        return null;
//        return ;
    }

    private void funVoid1() {
//        return null;
        return;
    }
}
