package cool.generator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal {
    public Coordinates position;
    public int energy;
    public List<Integer> genotype = new ArrayList<>();
    public int n; //długość genotypu

    public int plantsEaten = 0;
    public int childrenCreated = 0;
    public boolean isAlive = true; //w momencie powstania na logike jest żywe
    public int dayOfBirth;
    public int aliveFor = 0;
    public int dayOfDeath;
    public int direction; //narazie tylko to tu jest
    public int activeGene;
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
    public Animal(Coordinates initialPosition, int initialEnergy, int N){
        this.dayOfBirth = 0; //powstał na początku symulacji
        this.position = initialPosition;
        this.energy = initialEnergy;
        this.n = N;

        this.getRandomGenotype();
        this.getRandomDirection();
        this.getRandomActiveGene();
    }

    //RODZĄCE SIĘ ZWIERZĘ
    //mutationsRandomness - zmienna oznaczająca wybrany wariant generowania genotypu
    //day - aktualny dzień symulacji
    public Animal(Animal strongerParent, Animal weakerParent, boolean mutationsRandomness, int day){
        this.position = strongerParent.position;
        this.mutationsRandomness = mutationsRandomness;
        this.dayOfBirth = day;
        this.inheritGenotype(strongerParent, weakerParent);
        this.getRandomDirection();
        this.getRandomActiveGene();
    }

    //ROBOCZE PÓKI NIE MA KLASY DIRECTION
    public void getRandomDirection(){ //losowy gen z genotypu - niekoniecznie pierwszy
        this.direction = random.nextInt(8);
        //przerobienie na direction czy coś bo to klasa będzie pewnie?
    }

    public void getRandomActiveGene(){ //losowanie pierwszego aktywnego genu i jego pozycji w genotype
        this.activeGene = random.nextInt(n);
    }

    public void getRandomGenotype(){ //w pełni losowy genotyp dla startowych
        this.genotype = new ArrayList<>();
        for (int i=0; i<this.n; i++){
            this.genotype.add(random.nextInt(8));
        }
    }

    public void inheritGenotype(Animal strongerParent, Animal weakerParent){
        //getting genotype
        int howMuch = Math.round(strongerParent.energy/(strongerParent.energy+weakerParent.energy));
        int whichSide = random.nextInt(2);
        if (whichSide == 0){ //lewa strona
            this.genotype = strongerParent.genotype.subList(0, howMuch);
//            this.genotype.add(weakerParent.genotype.subList(howMuch, this.n));
        }
        else { //prawa strona
//            this.genotype += weakerParent.genotype.substring(0, this.n - howMuch);
//            this.genotype += strongerParent.genotype.substring(this.n-howMuch, n);
        }

        //mutations
        if (this.mutationsRandomness){ //pełna losowość
            int mutationsNumber = random.nextInt(minMutations, maxMutations);
            for (int i=0; i<mutationsNumber; i++){
//                this.genotype.charAt(random.nextInt(this.n)) = (char) random.nextInt(this.n);
            }
        }
        else{ //1 w góre lub w dół
            return;
        }
    }

}

