package cool.generator;

import java.util.HashMap;
import java.util.PriorityQueue;

public class Map {
    HashMap<Coordinates, PriorityQueue<Animal>> animals = new HashMap<>();
    HashMap<Coordinates, Plants> plants = new HashMap<>();
    public int width;
    public int height;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public void placeAnimal(Coordinates coordinates, Animal animal){
        PriorityQueue<Animal> priorityQueue = animals.get(coordinates);
        if (priorityQueue == null){
            priorityQueue = new PriorityQueue<>(new AnimalComparator());
            priorityQueue.add(animal);
            this.animals.put(coordinates, priorityQueue);
            return;
        }
        priorityQueue.add(animal);
    }
    public void placeAnimal(Animal animal) {
        PriorityQueue<Animal> priorityQueue = animals.get(animal.getPosition());
        try {
            priorityQueue.add(animal);
        } catch (NullPointerException exception) {
            System.out.println("Jakims cudem dzieciaczek pojawil sie nie tam gdzie jego rodzice");
            // jakis error fajnie byloby tu klepnac
        }
    }
}
