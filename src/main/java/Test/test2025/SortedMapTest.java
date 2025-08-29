package Test.test2025;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * TreeMap：基于红黑树实现，是SortedMap最常用的实现类，支持高效的插入、删除和查找操作（时间复杂度为O(logn)）。
 *
 *
 *LinkedHashMap：按插入顺序或访问顺序（LRU）排序，不保证键的自然顺序。
 * TreeMap：始终按键排序，适合需要范围查询的场景。
 */
public class SortedMapTest {

    public  void  test()
    {
        fun1();
    }
    private  void  fun1()
    {
        //根据key 排序，默认升序
        SortedMap<Long,String> sortedMap=new TreeMap<>() ;
        sortedMap.put(Long.valueOf(2),"b");
        sortedMap.put(Long.valueOf(1),"a");
        System.out.println(sortedMap); // 输出: {1=A, 2=B}
        System.out.println(sortedMap.firstKey()); // 输出: 1
        int n=0;
    }
}
