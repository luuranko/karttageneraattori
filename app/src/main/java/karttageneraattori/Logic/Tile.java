package karttageneraattori.Logic;
/**
 * Tiles contain a Type enum and coordinates x and y.
 * <p>
 */
public class Tile {
    private Type type;
    private int x;
    private int y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = Type.EMPTY;
    }

    public Type getType() {
        return type;
    }
    
    public void setType(Type type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    
}
