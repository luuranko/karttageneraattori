package karttageneraattori.Logic;

import java.util.Random;

public class Generator {
    
    private int tileMax;
    private Random rng;

    public Generator(int tileMax, Random random) {
        this.tileMax = tileMax;
        this.rng = random;
    }

    /**
     * Initializes a Map by going through all of the Map's coordinates 
     * and calling the Map's insert() on each of them, 
     * inserting a TileType determined by function determineTile. 
     * <p>
     * @param  Map  The Map that is to be initialized.
     * @return      void
     */
    public void initMap(Map map) {
        // long start = System.currentTimeMillis();
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                map.insert(determineTile(map, x, y), x, y);
            }
        }
        // long end = System.currentTimeMillis();
        // double time = (double) (end - start) * 0.01;
        // System.out.println("Generating map took " + time + " seconds");
    }

    /**
     * Returns a TileType that should be inserted to the given position. 
     * Goes through the surrounding Tiles in all four directions 
     * to check what TileTypes are preferred in the area. 
     * Depending on if the x-coordinate is even or odd, 
     * also goes through the nortwest and northeast 
     * or the southwest and southeast Tiles.
     * Tiles at the borders of the Map are always decided to be of type SEA.
     * <p> 
     * @param  map  Map being processed
     * @param  x the y-coordinate of the Tile that will be processed
     * @param  y the y-coordinate of the Tile that will be processed
     * @return      TileType
     */
    public TileType determineTile(Map map, int x, int y) {
        TileType choice = TileType.values()[1];
        int[] points = new int[tileMax + 1];

        if (x % 2 == 0) {
            if (y - 1 >= 0) {
                if (x - 1 >= 0) {
                    points[awardPoints(map.getMap()[x - 1][y - 1])]++;
                }
                if (x + 1 < map.getWidth()) {
                    points[awardPoints(map.getMap()[x + 1][y - 1])]++;
                } 
            } else {
                return choice;
            }
        } else {
            if (y + 1 < map.getHeight()) {
                if (x - 1 >= 0) {
                    points[awardPoints(map.getMap()[x - 1][y + 1])]++;
                }
                if (x + 1 < map.getWidth()) {
                    points[awardPoints(map.getMap()[x + 1][y + 1])]++;
                } 
            } else {
                return choice;
            }
        }

        if (x - 1 >= 0) {
            points[awardPoints(map.getMap()[x - 1][y])]++;
        } else {
            return choice;
        }
        if (x + 1 < map.getWidth()) {
            points[awardPoints(map.getMap()[x + 1][y])]++;
        } else {
            return choice;
        }
        if (y - 1 >= 0) {
            points[awardPoints(map.getMap()[x][y - 1])]++;
        } else {
            return choice;
        }
        if (y + 1 < map.getHeight()) {
            points[awardPoints(map.getMap()[x][y + 1])]++;
        } else {
            return choice;
        }

        int most = 0;
        for (int i = 1; i < points.length; i++) {
            if (points[i] > most) {
                most = points[i];
                choice = TileType.values()[i];
            }
        }
        return choice;
    }

    /**
     * Checks the chances for each TileType for the Tile's TileType 
     * and uses a random number generator to decide between those types.
     * <p> 
     * @param  Tile the Tile being processed
     * @return      int
     */
    private int awardPoints(Tile tile) {
        double roll = nextDouble();
        if (roll < tile.getType().seaChance()) {
            return 1;
        } else if (roll < tile.getType().seaChance()
                + tile.getType().sandChance()) {
            return 2;
        }
        return 1;
    }

    public double nextDouble() {
        return rng.nextDouble();
    }





}
