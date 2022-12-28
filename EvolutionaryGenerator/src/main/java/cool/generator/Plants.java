package cool.generator;

public class Plants extends MapElement {
    public Plants(Coordinates position) {
        this.position = position;
    }
    @Override
    public String toString() {
        return "*";
    }

}
