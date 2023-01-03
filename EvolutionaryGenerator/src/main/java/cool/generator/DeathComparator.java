package cool.generator;

import java.util.Comparator;
import java.util.HashMap;

public class DeathComparator implements Comparator<Coordinates> {
    HashMap<Coordinates, Integer> map;

    public DeathComparator(HashMap<Coordinates, Integer> map) {
        this.map = map;
    }

    @Override
    public int compare(Coordinates first,  Coordinates second) {
        return map.get(second).compareTo(map.get(first));
    }
}
