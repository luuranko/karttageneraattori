package karttageneraattori.Logic;

public class Tile {
    private TileType type;

    public Tile() {
        this.type = TileType.EMPTY;
    }

    public Tile(TileType type) {
        this.type = type;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

}
