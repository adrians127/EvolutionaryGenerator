package cool.generator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

class AnimalComparatorTest {
    @Test
    void CompareTest() {
        WorldMap map = new WorldMap();
        Animal firstAnimal = new Animal(new Coordinates(2,2), 20, 3, map);
        Animal secondAnimal = new Animal(new Coordinates(2,2), 30, 3, map);
        PriorityQueue<Animal> PQ = new PriorityQueue<>(new AnimalComparator());
        PQ.add(firstAnimal);
        PQ.add(secondAnimal);
        assertEquals(PQ.poll().getEnergy(), secondAnimal.getEnergy());
        assertEquals(PQ.poll().getEnergy(), firstAnimal.getEnergy());

        ArrayList<Animal> AL = new ArrayList<>();
        AL.add(firstAnimal);
        AL.add(secondAnimal);
        AL.sort(new AnimalComparator());
        assertEquals(firstAnimal.getEnergy(), AL.get(1).getEnergy());
        assertEquals(secondAnimal.getEnergy(), AL.get(0).getEnergy());
        System.out.println(AL.get(0).getEnergy());
    }

}