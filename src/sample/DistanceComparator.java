package sample;

import java.util.Comparator;
import java.util.List;

public class DistanceComparator implements Comparator {
    @Override
    public int compare(Object o, Object t1) {
        List<Integer> obj1 = (List<Integer>) o;
        List<Integer> obj2 = (List<Integer>) t1;
        return obj1.get(0).compareTo(obj2.get(0));
    }
}
