package karttageneraattori.Logic;
/**
 * A Map contains a two-dimensional Tile-array.
 * <p>
 */
public class Map {
    private Tile[][] map;

    // Recycles the Tiles of the previous map if able
    public Map(Map old, int newWidth, int newHeight) {
        
        Tile[][] newMap = new Tile[newWidth][newHeight];

        if (old.getWidth() < newWidth) {
            if (old.getHeight() < newHeight) {
                for (int y = 0; y < old.getHeight(); y++) {
                    for (int x = 0; x < old.getWidth(); x++) {
                        newMap[x][y] = old.getMap()[x][y];
                        newMap[x][y].setType(Type.EMPTY);
                    }
                }
                for (int y = 0; y < old.getHeight(); y++) {
                    for (int x = old.getWidth(); x < newWidth; x++) {
                        newMap[x][y] = new Tile(x, y);
                    }
                }
                for (int y = old.getHeight(); y < newHeight; y++) {
                    for (int x = 0; x < newWidth; x++) {
                        newMap[x][y] = new Tile(x, y);
                    }
                }
            } else {
                for (int y = 0; y < newHeight; y++) {
                    for (int x = 0; x < old.getWidth(); x++) {
                        newMap[x][y] = old.getMap()[x][y];
                        newMap[x][y].setType(Type.EMPTY);
                    }
                }
                for (int y = 0; y < newHeight; y++) {
                    for (int x = old.getWidth(); x < newWidth; x++) {
                        newMap[x][y] = new Tile(x, y);
                    }
                }
            }
        } else {
            if (old.getHeight() < newHeight) {
                for (int y = 0; y < old.getHeight(); y++) {
                    for (int x = 0; x < newWidth; x++) {
                        newMap[x][y] = old.getMap()[x][y];
                        newMap[x][y].setType(Type.EMPTY);
                    }
                }
                for (int y = old.getHeight(); y < newHeight; y++) {
                    for (int x = 0; x < newWidth; x++) {
                        newMap[x][y] = new Tile(x, y);
                    }
                }
            } else {
                for (int y = 0; y < newHeight; y++) {
                    for (int x = 0; x < newWidth; x++) {
                        newMap[x][y] = old.getMap()[x][y];
                        newMap[x][y].setType(Type.EMPTY);
                    }
                }
            }
        }
        map = newMap;
    }
    
    public Map(int width, int height) {
        this.map = new Tile[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[x][y] = new Tile(x, y);
            }
        }
    }

    public Map(int width) {
        this(width, width);
    }

    public Map() {
        this(100, 100);
    }

    public Tile getTile(int x, int y) {
        return getMap()[x][y];
    }

    public int getWidth() {
        return getMap().length;
    }

    public int getHeight() {
        return getMap()[0].length;
    }

    public Tile[][] getMap() {
        return map;
    }  
}