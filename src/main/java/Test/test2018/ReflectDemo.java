package Test.test2018;

import Model.Student;
import Model.StudentInterface;
import Model.StudentParent;
import Test.test2018.Description;

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
            Class clazz = student.getClass();
            Class clas = Student.class;
            Class clssForName = Class.forName("Model.Student");
            //class创建对象的两种方式
            Student st = (Student) clazz.newInstance();
            Constructor constructor = clazz.getConstructor();
            Student stude = (Student) constructor.newInstance();

            String className = clas.getName();
            Class cls = Class.forName(className);

            //是否继承类、接口
            Boolean extendss = StudentParent.class.isAssignableFrom(cls);
            Boolean extendsI = StudentInterface.class.isAssignableFrom(cls);

            //对象判断
            Boolean ree = student instanceof StudentParent;
            Boolean rei = student instanceof StudentInterface;

            //反射字段、方法
            Field[] fields = clas.getDeclaredFields();
            //给字段赋值
            if (!fields[0].isAccessible()) {
                fields[0].setAccessible(true);
            } else {
                //取字段值
                fields[0].get(clas.newInstance());
            }
            fields[0].set(clas.newInstance(), "fancky");
            Method[] methods = clas.getDeclaredMethods();

            //反射调用方法
            Method method = cls.getDeclaredMethod("setAge", Integer.class);
            //反射创建实例
            Object object = cls.newInstance();
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

            System.out.println();  //插入断点
        } catch (Exception ex) {
            String msg = ex.getMessage();
            //   throw ex;

        }

    }


}
