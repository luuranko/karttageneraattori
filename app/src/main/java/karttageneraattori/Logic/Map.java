package karttageneraattori.Logic;

public class Map {

    private int width;
    private int height;
    private Tile[][] map;
    
    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new Tile[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[x][y] = new Tile();
            }
        }
    }

    public Map(int width) {
        this(width, width);
    }

    public Map() {
        this(100, 100);
    }

    public void insert(TileType type, int x, int y) {
        if (map[x][y].getType() == TileType.EMPTY) {
            map[x][y].setType(type);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile[][] getMap() {
        return map;
    }  
}