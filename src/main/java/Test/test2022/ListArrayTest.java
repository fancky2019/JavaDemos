package Test.test2022;

import java.util.ArrayList;
import java.util.List;

public class ListArrayTest {
    public void test()
    {
        fun1();
    }

    private void  fun1()
    {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        //[1, 2, 3, 4]
        String str = list.toString();

        int[] array=new int[4];
        array[0]=1;
        array[1]=2;
        array[2]=3;
        array[3]=4;
        //[I@4ec6a292
        String str1 = array.toString();



    }
}
