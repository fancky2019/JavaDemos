package Model;

import scala.collection.immutable.ListSet;

import java.util.Comparator;

public class ComparableModelComparator implements Comparator<ComparableModel> {
    @Override
    public int compare(ComparableModel o1, ComparableModel o2) {
        if (o1 == null && o1 == null) {
            return 0;
        } else {
            if (o1 == null && o2 != null) {
                return -1;
            } else if (o1 != null && o2 == null) {
                return 1;
            } else {
                return o1.getAge().compareTo(o2.getAge());
            }
        }

    }
}
