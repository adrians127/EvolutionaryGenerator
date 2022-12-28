package cool.generator;

public enum MoveDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;
    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "Polnoc";
            case NORTHEAST -> "Polnoc-wschod";
            case EAST -> "Wschod";
            case SOUTHEAST -> "Poludnie-wschod";
            case SOUTH -> "Poludnie";
            case SOUTHWEST -> "Poludnie-zachod";
            case WEST -> "Zachod";
            case NORTHWEST -> "Polnoc-zachod";
        };
    }
    /**
     * changeDirection is changing MoveDirection with given number [0,1,...,7]
     * Example: changeDirection(2) with actual value NORTH will change direction to EAST
     * WARNING: if given number is > 7 then it will change by modulo of 8
     */
    public MoveDirection changeDirection(int number) {
        MoveDirection[] values = MoveDirection.values();
        int index = (this.ordinal() + number) % values.length;
        return values[index];
    }
    public Coordinates toUnitVector() {
        return switch (this) {
            case NORTH -> new Coordinates(0,1);
            case NORTHEAST -> new Coordinates(1,1);
            case EAST -> new Coordinates(1,0);
            case SOUTHEAST -> new Coordinates(1,-1);
            case SOUTH -> new Coordinates(0,-1);
            case SOUTHWEST -> new Coordinates(-1,-1);
            case WEST -> new Coordinates(-1,0);
            case NORTHWEST -> new Coordinates(-1,1);
        };
    }

}
