package Test.opensource.orgapachecommons;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionsCommons {
    public void test() {
        List<Integer> list = new ArrayList<>();
        List<Integer> list1 = null;

        //判空
        Boolean re = CollectionUtils.isEmpty(list);
        Boolean re1 = CollectionUtils.isEmpty(list);

        list1 = new ArrayList<>();

        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list1.add(1);
        list1.add(2);
        list1.add(5);
        list1.add(6);

        //
//        CollectionUtils.addAll(list,list1);

        //交集
        Collection<Integer> newCollection = CollectionUtils.intersection(list, list1);

        List<Integer> newList = newCollection.stream().collect(Collectors.toList());
        newList.clear();
        newList = (List<Integer>) newCollection;

        //差集
        CollectionUtils.removeAll(list, list1);

        //并集
        CollectionUtils.union(list, list1);


        CollectionUtils.containsAny(list, 1);
        Integer m = 0;
    }
}
