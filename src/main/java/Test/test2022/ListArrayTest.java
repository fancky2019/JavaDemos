package Test.test2022;

import Test.test2019.queue.BlockingQueueTest;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class ListArrayTest {
    public void test() {
        delete();
        fun1();
        fun(1);

        fun(2, 2);
        fun(2, 2, 3, 4);
        int[] arr = new int[]{1, 2, 3, 4};
        fun(2, arr);

        //不能传list，只能传数组
//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        fun(2,list);

        Integer[] arr1 = new Integer[]{1, 2, 3, 4};
        fun1(2, arr1);

    }

    private void fun1() {

//底层结构：双向链表
//        private static class Node<E> {
//        E item;
//        LinkedList.Node<E> next;
//        LinkedList.Node<E> prev;
        LinkedList<Integer> ll = new LinkedList<>();
        ll.add(1);


        // 底层存储 transient Object[] elementData;   object数组
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        //[1, 2, 3, 4]
        String str = list.toString();


        Integer[] array11 = new Integer[list.size()];
        array11 = list.toArray(array11);

        //list1111只读
        List<Integer> list1111 = Arrays.asList(array11);
//        list1111.add(12);

        int[] array = new int[4];
        array[0] = 1;
        array[1] = 2;
        array[2] = 3;
        array[3] = 4;
        //[I@4ec6a292
        String str1 = array.toString();
        String arrStr = Arrays.toString(array);
//        Arrays.stream(array).filter()


        List<Integer> ll1 = Collections.synchronizedList(ll);
        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();

    }

    /**
     * 变长参数必须是最后一个参数，只能有一个变长参数：Vararg parameter must be the last in the list
     * 不传参数，数组是个空数组，长度为0，但是不是null。
     * 可以传单个元素，或者数组，或者逗号隔开的n个元素
     *
     * @param a
     * @param b
     */
    private void fun(int a, int... b) {
        if (b.length > 0) {
            int c = b[1];
        }

    }

    private void fun1(int a, Integer... b) {
        if (b.length > 0) {
            int c = b[1];
        }

    }

    //报错：Vararg parameter must be the last in the list
//    private void fun2(int a,Integer... b,String... str)
//    {
//        if(b.length>0)
//        {
//            int c= b[1];
//        }
//
//    }


    private void delete() {
        //倒叙删除，集合长度改变不影响遍历--删除的元素已经遍历过
        List<Integer> list = new ArrayList<Integer>();
        list.add(9);
        list.add(6);
        list.add(8);
        list.add(5);
        list.add(6);
        list.add(9);
        for (int i = list.size() - 1; i >= 0; i--) {
            if (i == 0) {
                int m = 0;
            }
            int num = list.get(i);
            if (num == 6) {
                //remove  内部还是循环从0索引未知开始便利删除，只会删除第一个匹配的索引未知
//                list.remove((Object)num);
                //int 默认匹配索引的重载，不是object而是索引，比删除元素少了一层遍历
                list.remove(i);
            }
        }


        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            Integer num = iterator.next();
            if (num == 6) {
                iterator.remove();
            }
        }

        list.removeIf(item -> item == 6);

        int m = 0;
    }

}
