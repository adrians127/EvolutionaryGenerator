package cool.generator;

import java.util.ArrayList;
import java.util.Random;

public class Animal extends MapElement {
    private int energy;
    private ArrayList<Integer> genotype = new ArrayList<>();
    private int n; //długość genotypu

    private int plantsEaten = 0;
    private int childrenCreated = 0;
    private boolean isAlive = true; //w momencie powstania na logike jest żywe
    private int dayOfBirth;
    private int aliveFor = 0;
    private int dayOfDeath;
    public MoveDirection direction; //narazie tylko to tu jest
    private int activeGeneIndex;

    private final WorldMap map;

    public Random random = new Random();

    //STARTOWE ZWIERZE - nie rodzi się
    //initialPosition - pozycja wyliczona w klasie mapy - losowa
    //initialEnergy - initial energy określona z parametrów globalnych symulacji
    //N - długość genotypu określona z parametrów globalnych symulacji
    public Animal(Coordinates initialPosition, int initialEnergy, int n, WorldMap map) {
        this.dayOfBirth = 0; //powstał na początku symulacji
        this.position = initialPosition;
        this.energy = initialEnergy;
        this.n = n;
        this.map = map;

        this.getRandomGenotype();
        this.getRandomDirection();
        this.getRandomActiveGene();
    }

    //RODZACE SIE ZWIERZE
    //mutationsRandomness - zmienna oznaczająca wybrany wariant generowania genotypu
    //day - aktualny dzień symulacji
    public Animal(Animal strongerParent, Animal weakerParent, int day, int energy, WorldMap map) {
        this.map = map;
        this.position = strongerParent.position;
        this.dayOfBirth = day;
        this.n = strongerParent.n;
        this.inheritGenotype(strongerParent, weakerParent);
        //        mutations
        mutateGenotype(map.isMutationsRandomness(), map.minMutations, map.maxMutations);
        this.getRandomDirection();
        this.getRandomActiveGene();
        this.energy = energy;
    }

    public void getRandomDirection() { //losowy gen z genotypu - niekoniecznie pierwszy
        direction = MoveDirection.NORTH.changeDirection(random.nextInt(8));
    }

    public void getRandomActiveGene() { //losowanie pierwszego aktywnego genu i jego pozycji w genotype
        activeGeneIndex = random.nextInt(n);
    }

    public ArrayList<Integer> getGenotype() {
        return genotype;
    }

    public void getRandomGenotype() { //w pełni losowy genotyp dla startowych
        genotype = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            genotype.add(random.nextInt(8));
        }
    }

    public void inheritGenotype(Animal strongerParent, Animal weakerParent) {
        //getting genotype
        int howMuch = Math.round(((float) strongerParent.getEnergy() / (float) (strongerParent.getEnergy() + weakerParent.getEnergy())) * n);
        int whichSide = random.nextInt(2);
        if (whichSide == 0) { //lewa strona
            for (int i = 0; i < howMuch; i++) {
                genotype.add(strongerParent.genotype.get(i));
            }
            for (int i = howMuch; i < n; i++) {
                genotype.add(weakerParent.genotype.get(i));
            }
        } else {
            for (int i = 0; i < n - howMuch; i++) {
                genotype.add(weakerParent.genotype.get(i));
            }
            for (int i = n - howMuch; i < n; i++) {
                genotype.add(strongerParent.genotype.get(i));
            }
        }
    }

    public void changeEnergy(int value) {
        energy += value;
    }
    public boolean death() {
        if (energy <= 0) {
            isAlive = false;
            dayOfDeath = dayOfBirth + aliveFor;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return direction.toString();
    }

    public void mutateGenotype(boolean mutationsRandomness, int minMutations, int maxMutations) {
        int mutationsNumber = random.nextInt(maxMutations - minMutations + 1) + minMutations;
        if (mutationsRandomness) { //pełna losowość
            for (int i = 0; i < mutationsNumber; i++) {
                genotype.set(random.nextInt(n), random.nextInt(8));
            }
            return;
        }  //1 w góre lub w dół
        for (int i = 0; i < mutationsNumber; i++) {
            int upOrDown = random.nextInt(2);
            int randomIndex = random.nextInt(n);
            if (upOrDown == 0) {
                genotype.set(randomIndex, (genotype.get(randomIndex) + 1) % 8);
            } else {
                genotype.set(randomIndex, (genotype.get(randomIndex) - 1 + 8) % 8);
            }
        }
    }

    public void move() {
        this.direction = this.direction.changeDirection(activeGeneIndex);
        this.position = this.position.addCoordinate(direction.toUnitVector());
        if (!map.isPortalEnabled()) {
            if (position.getX() >= map.width) {
                this.position = new Coordinates(0, position.getY());
            } else if (position.getX() < 0) {
                this.position = new Coordinates(map.width - 1, position.getY());
            }
            if (position.getY() < 0) {
                this.position = new Coordinates(this.position.getX(), 0);
                this.direction = this.direction.changeDirection(4);
            } else if (position.getY() >= map.height) {
                this.position = new Coordinates(this.position.getX(), map.height - 1);
                this.direction = this.direction.changeDirection(4);
            }
        } else {
            if (position.getX() >= map.width || position.getX() < 0
                    || position.getY() < 0 || position.getY() >= map.height) {
                changeEnergy(map.getLossOnCoupleEnergy() * -1);
                position = new Coordinates(random.nextInt(map.width), random.nextInt(map.height));
            }
        }
        if (map.isMoveRandomness()) {
            random = new Random();
            int lottery = random.nextInt(5);
            if (lottery == 1) {
                getRandomActiveGene();
                return;
            }
        }
        activeGeneIndex = (activeGeneIndex + 1) % genotype.size();
    }
    private void doMove(){
        position.addCoordinate(direction.changeDirection(genotype.get(activeGeneIndex)).toUnitVector());
    }

    @Override
    public String getImageSrc() {
        return "src/main/resources/animal.png";
    }

    public int getEnergy() {
        return energy;
    }

    public int getAliveFor() {
        return aliveFor;
    }

    public int getChildrenCreated() {
        return childrenCreated;
    }

    public int getPlantsEaten() {
        return plantsEaten;
    }

    public void addPlantsEaten(){
        plantsEaten++;
    }

    public void addChildrenCreated(){
        childrenCreated++;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getDayOfDeath() {
        return dayOfDeath;
    }
    public void addDay(){
        aliveFor++;
    }

    public int getActiveGeneIndex() {
        return activeGeneIndex;
    }
}