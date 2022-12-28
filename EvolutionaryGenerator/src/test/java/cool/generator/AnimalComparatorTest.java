package cool.generator;

import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

class AnimalComparatorTest {
    @Test
    void CompareTest() {
        Animal firstAnimal = new Animal(new Coordinates(2,2), 20, 3);
        Animal secondAnimal = new Animal(new Coordinates(2,2), 30, 3);
        PriorityQueue<Animal> PQ = new PriorityQueue<>(new AnimalComparator());
        PQ.add(firstAnimal);
        PQ.add(secondAnimal);
        assertEquals(PQ.poll().getEnergy(), secondAnimal.getEnergy());
        assertEquals(PQ.poll().getEnergy(), firstAnimal.getEnergy());
    }

}