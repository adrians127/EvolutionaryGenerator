package cool.generator;

public class KittiesWorld {
    public static void main(String[] args) {

        System.out.println("witajcie kitties");

        MoveDirection north = MoveDirection.NORTH;
        System.out.println(north);
        north = north.changeDirection(2);
        System.out.println(north);
        north = north.changeDirection(12);
        System.out.println(north);
    }
}
