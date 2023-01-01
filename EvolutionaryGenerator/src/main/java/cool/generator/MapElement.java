package cool.generator;

abstract public class MapElement implements IMapElement{
    protected Coordinates position;
    @Override
    public Coordinates getPosition() {
        return position;
    }

    @Override
    public String getImageSrc() {
        return null;
    }
}
