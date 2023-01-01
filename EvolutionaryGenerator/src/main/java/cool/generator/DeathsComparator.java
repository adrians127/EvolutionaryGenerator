package cool.generator;

import java.util.Comparator;
import java.util.Map;

public class DeathsComparator implements Comparator<Coordinates> {
    Map<Coordinates, Integer> map;
    public DeathsComparator(Map<Coordinates, Integer> map) {
        this.map = map;
    }
    @Override
    public int compare(Coordinates first, Coordinates second) {
        int val1 = map.get(first);
        int val2 = map.get(second);
        return val1-val2;
    }
}
