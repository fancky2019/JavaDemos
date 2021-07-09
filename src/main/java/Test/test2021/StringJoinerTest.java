package Test.test2021;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class StringJoinerTest {
    public  void test()
    {
        fun();
    }

    private  void  fun()
    {
        StringJoiner stringJoiner=new StringJoiner(",");
        List<Integer> list=new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.forEach(p->
        {
            stringJoiner.add(p.toString());
        });
        String str=stringJoiner.toString();
        int m=0;

    }
}
