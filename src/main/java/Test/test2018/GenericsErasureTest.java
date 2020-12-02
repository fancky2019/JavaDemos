package Test.test2018;

import com.sun.media.sound.EmergencySoundbank;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
泛型类型擦除
泛型擦除：T-->object、上界(如果指定上界)、下界（如果指定下界）
 */
public class GenericsErasureTest {
    public void Test() {

    }


    private void fun1() {
        //
        ArrayList arrayList = new ArrayList();
        List<Integer> list = new ArrayList<>();
        List<String> stringList = new LinkedList<>();

        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();
        ConcurrentLinkedQueue<Integer> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();
//        List<Integer>  list1=
    }

    private void fun2() {

    }
}
