package cool.generator;

public class KittiesWorld {
    public static void main(String[] args) {

        System.out.println("witajcie kitties");
        Coordinates coordinates =  new Coordinates(2,3);
        Coordinates coordinates1 = new Coordinates(2,4);

        System.out.println(coordinates.equals(coordinates1));
    }
}
