package Test.opensource.commonspool;

import Model.Student;
import com.google.type.Money;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
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
            GenericObjectPool<Student> ObjectPool = new GenericObjectPool<>(new DemoPooledObjectFactory<>(Student.class));
            Student student1 = ObjectPool.borrowObject();
            student1.setName("fancky1");
            Student student2 = ObjectPool.borrowObject();
            student2.setName("fancky2");
            ObjectPool.returnObject(student1);

            //
            Student student3 = ObjectPool.borrowObject();
            //true
            if(student1==student3)
            {
                System.out.println("student1==student3");
            }
            Student student4 = ObjectPool.borrowObject();


            //当流程走完就归还借用的object
            ObjectPool.returnObject(student2);

//              ObjectPool.clear();
            ObjectPool.close();//应用程序退出时候使用,释放所有归还的对象
            int m = 0;

        } catch (Exception ex) {

        }


    }
}
