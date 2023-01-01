package cool.generator;

import java.util.*;
import java.util.stream.Stream;

public class WorldMap {
    /**
     * warianty mapy:
     */
    private final boolean isPortalEnabled;

    // false = zalesione rowniki, true = toksyczne trupy
    private boolean preferedPlantSpawnToxic;

    //true - wariant pełnej losowości //false - lekka korekta (1 w dół lub 1 w góre)
    //dla startowych zwierząt musi być true - bo nie ma genotypów rodziców, od których można przejąć tą wartość
    private boolean mutationsRandomness;
    private final boolean moveRandomness;


    /**
     * stałe dla mapy:
     */
    public int width;
    public int height;
    private int energyFromPlant;
    private int dailyPlants;
    private int genotypeLength;
    // wariant do dodania
    private int startEnergyForAnimal;
    private int readyToCoupleEnergy;
    private int lossOnCoupleEnergy;
    public int minMutations;
    public int maxMutations;
    /**
     * zmienne dla mapy:
     */
    private int simulationDays;
    public int emptyFields;

    public HashMap<Coordinates, ArrayList<Animal>> animals = new HashMap<>();
    public HashMap<Coordinates, Plant> plants = new HashMap<>();

    //true -> 80% szans ze na nim bedzie, false -> 20%
    public HashMap<Coordinates, Boolean> mapForPlants = new HashMap<>();
    private HashMap<Coordinates, Integer> deathsOnFields = new HashMap<>();

    public int aliveAnimalsNumber;

    public WorldMap() {
        this.dailyPlants = 1;
        this.height = 25;
        this.width = 25;
        this.minMutations = 3;
        this.maxMutations = 3;
        this.lossOnCoupleEnergy = 5;
        this.genotypeLength = 8;
        this.energyFromPlant = 10;
        this.startEnergyForAnimal = 2000;
        this.readyToCoupleEnergy = 10;
        this.simulationDays = 0;

        this.mutationsRandomness = false;
        this.moveRandomness = true;
        this.isPortalEnabled = true;
        this.preferedPlantSpawnToxic = false;
        initialPlace(10, 0);
    }

    public WorldMap(int width, int height, int initialNumberOfPlants, int energyFromPlant,
                    int dailyPlants, int initialNumberOfAnimals, int startEnergyForAnimal, int readyToCoupleEnergy,
                    int lossOnCoupleEnergy, int minMutations, int maxMutations, boolean mutationsRandomness, int genotypeLength,
                    boolean moveRandomness, boolean isPortalEnabled, boolean preferedPlantSpawn) {
        this.isPortalEnabled = isPortalEnabled;
        this.moveRandomness = moveRandomness;

        this.width = width;
        this.height = height;
        this.energyFromPlant = energyFromPlant;
        this.dailyPlants = dailyPlants;
        this.startEnergyForAnimal = startEnergyForAnimal;
        this.readyToCoupleEnergy = readyToCoupleEnergy;
        this.lossOnCoupleEnergy = lossOnCoupleEnergy;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.mutationsRandomness = mutationsRandomness;
        this.genotypeLength = genotypeLength;
        this.simulationDays = 0;
    }

    public void calculateEmptyFields() {
        Set<Coordinates> takenFields = new HashSet<>(animals.keySet());
        takenFields.addAll(plants.keySet());
        emptyFields = height * width - takenFields.size();
    }

    void initialPlace(int initialNumberOfAnimals, int initialNumberOfPlants) {
        int row = (int) Math.round(height * 0.2);
        int centerStart = (height - row) / 2;
        for (int y = 0; y < centerStart; y++) {
            for (int x = 0; x < width; x++) {
                mapForPlants.put(new Coordinates(x, y), false);
            }
        }
        for (int y = centerStart; y < centerStart + row; y++) {
            for (int x = 0; x < width; x++) {
                mapForPlants.put(new Coordinates(x, y), true);
            }
        }
        for (int y = centerStart + row; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mapForPlants.put(new Coordinates(x, y), false);
            }
        }

        int i = 0;
        while (i < initialNumberOfPlants && plants.size() < (width * height)) {
            Random random = new Random();
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Plant plant = new Plant(new Coordinates(x, y));
            if (placePlant(plant)) {
                i++;
            }
        }
        for (i = 0; i < initialNumberOfAnimals; i++) {
            Random random = new Random();
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Animal animal = new Animal(new Coordinates(x, y), startEnergyForAnimal, genotypeLength, this);
            placeAnimal(animal.getPosition(), animal);
        }
        aliveAnimalsNumber = initialNumberOfAnimals;
    }

    public boolean placePlant(Plant plant) {
        if (plants.get(plant.getPosition()) == null) {
            plants.put(plant.getPosition(), plant);
            return true;
        } else return false;
    }

    public void placeAnimal(Coordinates coordinates, Animal animal) {
        ArrayList<Animal> animalsAtPosition = animals.get(coordinates);
        if (animalsAtPosition == null) {
            animalsAtPosition = new ArrayList<>();
            animalsAtPosition.add(animal);
            this.animals.put(coordinates, animalsAtPosition);
            return;
        }
        animalsAtPosition.add(animal);
        animalsAtPosition.sort(new AnimalComparator());
    }

    public void placeAnimal(Animal animal) {
        ArrayList<Animal> animalsAtPosition = animals.get(animal.getPosition());
        try {
            animalsAtPosition.add(animal);
            animalsAtPosition.sort(new AnimalComparator());
        } catch (NullPointerException exception) {
            System.out.println("Jakims cudem dzieciaczek pojawil sie nie tam gdzie jego rodzice");
            // jakis error fajnie byloby tu klepnac
        }
    }

    public void cycle() {
        handleDeaths();
        moveAnimals();
        eatPlants();
        couple();
        placePlants();
        endOfDay();
    }

    private void changePlantFields() {

    }

    //pierwszy cykl
    public void handleDeaths() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Coordinates position = new Coordinates(x, y);
                ArrayList<Animal> animalsAtPosition = animalsAt(position);
                if (animalsAtPosition == null) {
                    continue;
                }
                animalsAtPosition.removeIf(animal -> {
                    if (animal.death()){
                        if (deathsOnFields.containsKey(animal.getPosition())) {
                            int temp = deathsOnFields.get(animal.getPosition()) + 1;
                            deathsOnFields.remove(animal.getPosition());
                            deathsOnFields.put(animal.getPosition(), temp);
                        } else {
                            deathsOnFields.put(animal.getPosition(), 1);
                        }
                        aliveAnimalsNumber -= 1;
                        return true;
                    }
                    return false;
                });//we sprawdz jak cos
            }
        }
        if (preferedPlantSpawnToxic) {
            changePlantFields();
        }
        calculateEmptyFields();
    }

    public void draw() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                System.out.print("[");
                Coordinates position = new Coordinates(x, y);
                if (animalsAt(position) != null) {
                    System.out.print("Z");
                }
                System.out.print("]");
            }
            System.out.println();
        }
    }

    private void animalPlace(HashMap<Coordinates, ArrayList<Animal>> updateMap, Animal animal) {
        ArrayList<Animal> animalsAtPosition = updateMap.get(animal.getPosition());
        if (animalsAtPosition == null) {
            animalsAtPosition = new ArrayList<>();
            animalsAtPosition.add(animal);
            updateMap.put(animal.getPosition(), animalsAtPosition);
            return;
        }
        animalsAtPosition.add(animal);
        animalsAtPosition.sort(new AnimalComparator());
    }

    //drugi cykl
    public void moveAnimals() {
        HashMap<Coordinates, ArrayList<Animal>> updateMap = new HashMap<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Coordinates position = new Coordinates(x, y);
                ArrayList<Animal> animalsAtPosition = animalsAt(position);
                if (animalsAtPosition == null) {
                    continue;
                }
                ArrayList<Animal> copyAnimals = new ArrayList<>(animalsAtPosition);
                while (animalsAtPosition.size() > 0) {
                    animalsAtPosition.remove(0);
                }
                animals.remove(position);
                for (Animal animal : copyAnimals) {
                    animal.move();
                    animalPlace(updateMap, animal);
                }

            }
        }
        this.animals = updateMap;
        calculateEmptyFields();
    }

    // trzeci cykl
    public void eatPlants() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Coordinates position = new Coordinates(x, y);
                if (animals.get(position) != null && plants.get(position) != null) {
                    animals.get(position).get(0).changeEnergy(energyFromPlant);
                    plants.remove(position);
                }
            }
        }
        calculateEmptyFields();
    }

    // czwarty cykl
    public void couple() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Coordinates position = new Coordinates(x, y);
                if (animals.get(position) != null && animals.get(position).size() > 1) {
                    ArrayList<Animal> animalsAtPosition = animals.get(position);
                    for (int i = 0; i < animalsAtPosition.size() - 1; i += 2) {
                        if (animalsAtPosition.get(i).getEnergy() >= readyToCoupleEnergy &&
                                animalsAtPosition.get(i + 1).getEnergy() >= readyToCoupleEnergy) {
                            Animal animal = new Animal(animalsAtPosition.get(i), animalsAtPosition.get(i + 1), simulationDays,
                                    lossOnCoupleEnergy * 2, this);
                            placeAnimal(animal);
                            animalsAtPosition.get(i).changeEnergy(lossOnCoupleEnergy * -1);
                            animalsAtPosition.get(i + 1).changeEnergy(lossOnCoupleEnergy * -1);
                            aliveAnimalsNumber += 1;
                        }
                    }
                }
            }
        }
        System.out.println(this.animals.size());
    }

    private boolean placePlantLottery(Coordinates position) {
        Random random = new Random();
        int coupon = random.nextInt(5);
        if (mapForPlants.get(position)) {
            return coupon != 4;
        }
        return coupon == 4;
    }

    //piaty cykl
    public void placePlants() {
        int i = 0;
        while (i < dailyPlants && plants.size() < (width * height)) {
            Random random = new Random();
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Plant plant = new Plant(new Coordinates(x, y));
            if (placePlantLottery(plant.getPosition())) {
                if(placePlant(plant)) i++;
            }
        }
        calculateEmptyFields();
    }

    //szosty cykl
    public void endOfDay() {
        simulationDays++;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Coordinates position = new Coordinates(x, y);
                ArrayList<Animal> animalsAtPosition = animalsAt(position);
                if (animalsAtPosition != null) {
                    for (Animal animal : animalsAtPosition) {
                        animal.changeEnergy(-1);
                    }
                }
            }
        }
    }

    public ArrayList<Animal> animalsAt(Coordinates position) {
        return animals.get(position);
    }

    public Plant plantAt(Coordinates position) {
        return plants.get(position);
    }

    public boolean isMoveRandomness() {
        return moveRandomness;
    }

    public boolean isPortalEnabled() {
        return isPortalEnabled;
    }

    public boolean isMutationsRandomness() {
        return mutationsRandomness;
    }

    public int getLossOnCoupleEnergy() {
        return lossOnCoupleEnergy;
    }

    public int getReadyToCoupleEnergy() {
        return readyToCoupleEnergy;
    }

    public int getSimulationDays() {
        return simulationDays;
    }
}
