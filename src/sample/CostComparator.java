package sample;

import java.util.Comparator;
import java.util.List;

public class CostComparator implements Comparator {
    @Override
    public int compare(Object o, Object t1) {
        List<Integer> obj1 = (List<Integer>) o;
        List<Integer> obj2 = (List<Integer>) t1;

        int fCostDiff = obj1.get(2).compareTo(obj2.get(2));
        int hCostDiff = obj1.get(1).compareTo(obj2.get(1));
        int gCostDiff = obj1.get(0).compareTo(obj2.get(0));

        if (fCostDiff != 0) return fCostDiff;
        else if(hCostDiff != 0) return hCostDiff;
        else return gCostDiff;
    }
}
