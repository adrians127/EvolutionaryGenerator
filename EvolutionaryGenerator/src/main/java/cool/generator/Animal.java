package cool.generator;

import java.util.ArrayList;
import java.util.Random;

public class Animal extends MapElement {
    private int energy;
    private ArrayList<Integer> genotype = new ArrayList<>();
    private int n; //długość genotypu

    private int plantsEaten = 0;
    public int childrenCreated = 0;
    private boolean isAlive = true; //w momencie powstania na logike jest żywe
    private int dayOfBirth;
    private int aliveFor = 0;
    private int dayOfDeath;
    public MoveDirection direction; //narazie tylko to tu jest
    public int activeGeneIndex;
    public boolean mutationsRandomness = true;
    //true - wariant pełnej losowości //false - lekka korekta (1 w dół lub 1 w góre)
    //dla startowych zwierząt musi być true - bo nie ma genotypów rodziców, od których można przejąć tą wartość

    public int minMutations;
    public int maxMutations;
    //liczba genów przeznaczonych do mutacji po narodzinach dziecka

    public Random random = new Random();

    //STARTOWE ZWIERZĘ - nie rodzi się
    //initialPosition - pozycja wyliczona w klasie mapy - losowa
    //initialEnergy - initial energy określona z parametrów globalnych symulacji
    //N - długość genotypu określona z parametrów globalnych symulacji
    public Animal(Coordinates initialPosition, int initialEnergy, int n) {
        this.dayOfBirth = 0; //powstał na początku symulacji
        this.position = initialPosition;
        this.energy = initialEnergy;
        this.n = n;

        this.getRandomGenotype();
        this.getRandomDirection();
        this.getRandomActiveGene();
    }

    //RODZĄCE SIĘ ZWIERZĘ
    //mutationsRandomness - zmienna oznaczająca wybrany wariant generowania genotypu
    //day - aktualny dzień symulacji
    public Animal(Animal strongerParent, Animal weakerParent, boolean mutationsRandomness, int day) {
        this.position = strongerParent.position;
        this.mutationsRandomness = mutationsRandomness;
        this.dayOfBirth = day;
        this.inheritGenotype(strongerParent, weakerParent);
        this.getRandomDirection();
        this.getRandomActiveGene();
    }

    //ROBOCZE PÓKI NIE MA KLASY DIRECTION
    public void getRandomDirection() { //losowy gen z genotypu - niekoniecznie pierwszy
        direction = MoveDirection.NORTH.changeDirection(random.nextInt(8));
        //przerobienie na direction czy coś bo to klasa będzie pewnie?
    }

    public void getRandomActiveGene() { //losowanie pierwszego aktywnego genu i jego pozycji w genotype
        activeGeneIndex = random.nextInt(n);
    }

    public void getRandomGenotype() { //w pełni losowy genotyp dla startowych
        genotype = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            genotype.add(random.nextInt(8));
        }
    }

    public void inheritGenotype(Animal strongerParent, Animal weakerParent) {
        //getting genotype
        int howMuch = Math.round(strongerParent.energy / (strongerParent.energy + weakerParent.energy)) * n;
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
        //mutations
        mutateGenotype();
    }

    @Override
    public String toString() {
        return direction.toString();
    }

    public void mutateGenotype(){
        int mutationsNumber = random.nextInt(minMutations, maxMutations);
        if (mutationsRandomness) { //pełna losowość
            for (int i = 0; i < mutationsNumber; i++) {
                genotype.set(random.nextInt(n), random.nextInt(8));
            }
            return;
        }  //1 w góre lub w dół
        for (int i=0; i<mutationsNumber; i++){
            int upOrDown = random.nextInt(2);
            int randomIndex = random.nextInt(n);
            if (upOrDown == 0) {
                genotype.set(randomIndex, (genotype.get(randomIndex)+1)%8);
            }
            else {
                genotype.set(randomIndex, (genotype.get(randomIndex)-1)%8);
            }
        }

    }
}