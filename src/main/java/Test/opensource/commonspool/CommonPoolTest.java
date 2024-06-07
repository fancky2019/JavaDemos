package Test.opensource.commonspool;

import Model.Student;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

/*
 依赖：commons-pool2
 */
public class CommonPoolTest {
    public void test() {
        fun();
    }

    private void fun() {


        try {
//            GenericObjectPool<Student> objectPool1 = new GenericObjectPool<>();
            DefaultPooledObject defaultPooledObject=  new DefaultPooledObject<Student>(new Student());
//            defaultPooledObject.
            GenericObjectPool<Student> objectPool = new GenericObjectPool<>(new PooledObjectFactoryImpl<>(Student.class));
            Student student1 = objectPool.borrowObject();
            student1.setName("fancky1");
            Student student2 = objectPool.borrowObject();
            student2.setName("fancky2");
            objectPool.returnObject(student1);

            //
            Student student3 = objectPool.borrowObject();
            //true
            if(student1==student3)
            {
                System.out.println("student1==student3");
            }
            Student student4 = objectPool.borrowObject();


            //当流程走完就归还借用的object
            objectPool.returnObject(student2);

//              ObjectPool.clear();
            objectPool.close();//应用程序退出时候使用,释放所有归还的对象
            int m = 0;

        } catch (Exception ex) {

        }


    }
}
