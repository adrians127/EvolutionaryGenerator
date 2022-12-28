package cool.generator;

import java.util.Comparator;

public class AnimalComparator implements Comparator<Animal> {
    @Override
    public int compare(Animal o1, Animal o2) {
        // condition that animal with greater energy is stronger
        if (o1.getEnergy() > o2.getEnergy()) {
            return -1;
        } else if (o2.getEnergy() > o1.getEnergy()){
            return 1;
        }
        // condition that animal older is stronger
        if (o1.getAliveFor() > o2.getAliveFor()){
            return -1;
        } else if (o2.getAliveFor() > o1.getAliveFor()){
            return 1;
        }
        // condition that animal with more children is stronger
        if (o1.getChildrenCreated() > o2.getChildrenCreated()) {
            return -1;
        } else if (o2.getChildrenCreated() > o1.getChildrenCreated()) {
            return 1;
        }
        // else it's random
        return 0;
    }
}
