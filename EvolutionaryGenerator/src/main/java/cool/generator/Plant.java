package cool.generator;

public class Plant extends MapElement {
    public Plant(Coordinates position) {
        this.position = position;
    }
    @Override
    public String toString() {
        return "*";
    }

    @Override
    public String getImageSrc() {
        return "src/main/resources/plant.png";
    }
}
