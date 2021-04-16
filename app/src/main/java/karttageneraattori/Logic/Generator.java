package karttageneraattori.Logic;

import java.util.Random;
/**
 * The Generator handles initializing any given Map.
 * <p>
 */
public class Generator {
    
    private int islandNum;
    private double landToSeaRatio;
    private Random rng;
    private Map m;
    private int amt; // Used when determining areas

    public Generator(int islandNum, double landToSeaRatio, Random random) {
        this.m = new Map(100, 100);
        this.islandNum = islandNum;
        this.landToSeaRatio = landToSeaRatio;
        this.rng = random;
        amt = 0;
    }

    public Generator(Random random) {
        this.m = new Map(100, 100);
        this.rng = random;
        setIslandNum(2);
        setLandToSeaRatio(0.3);
        amt = 0;
    }

    public void newMap(int x, int y) {
        m = new Map(m, x, y);
    }

    public Map getMap() {
        return m;
    }

    public void newValues() {
        setIslandNum(rng.nextInt(3) + 1);
        setLandToSeaRatio(((double) rng.nextInt(4) / 20.0) + 0.2);
    }

    public void setIslandNum(int islandNum) {
        this.islandNum = islandNum;
    }

    public void setLandToSeaRatio(double landToSeaRatio) {
        this.landToSeaRatio = landToSeaRatio;
    }

    /**
     * The Generator's current Map is initialized.
     * The map's borders are made into SEA-tiles.
     * LAND-tiles are placed based on 1-3 random starting points.
     *  Any single tiles are removed
     *  and made to conform to the surrounding tiles.
     * Remaining EMPTY-tiles are turned into SEA-tiles.
     *  Any SEA-areas are turned into LAKE-areas, if landlocked.
     * <p>
     * @return      void
     */
    public void initMap() {
        long start = System.currentTimeMillis();
        long alg = start;
        double time;

        createIslands();
        time = (double) (System.currentTimeMillis() - alg) * 0.001;
        System.out.println("Islands created in " + time);
        alg = System.currentTimeMillis();

        removeSingles(5);
        time = (double) (System.currentTimeMillis() - alg) * 0.001;
        System.out.println("Removed lonely tiles (5) in " + time);
        alg = System.currentTimeMillis();

        removeSingles(2);
        time = (double) (System.currentTimeMillis() - alg) * 0.001;
        System.out.println("Removed lonely tiles (2) in " + time);
        alg = System.currentTimeMillis();

        fillInSea();
        time = (double) (System.currentTimeMillis() - alg) * 0.001;
        System.out.println("Filled in sea in " + time);
        alg = System.currentTimeMillis();

        processEntities();
        time = (double) (System.currentTimeMillis() - alg) * 0.001;
        System.out.println("Filled in lakes in " + time);
        alg = System.currentTimeMillis();

        time = (double) (System.currentTimeMillis() - start) * 0.001;
        System.out.println("Generating the map took "
            + time + " seconds altogether");
    }


    // Makes all type EMPTY tiles type SEA
    public void fillInSea() {
        for (int y = 0; y < m.getHeight(); y++) {
            for (int x = 0; x < m.getWidth(); x++) {
                if (m.getMap()[x][y].getType() == Type.EMPTY) {
                    m.getMap()[x][y].setType(Type.SEA);
                }
            }
        }
    }

    public void createIslands() {
        int totalTiles = (m.getWidth() - 2) * (m.getHeight() - 2);
        int landTiles = 0;
        int landTilesGoal = (int) (totalTiles * landToSeaRatio);

        // Coordinates for islands' starting points
        int[][] islands_x = new int[islandNum][landTilesGoal + 1];
        int[][] islands_y = new int[islandNum][landTilesGoal + 1];

        int minDistance_x = (int) (m.getWidth() * 0.2); 
        int minDistance_y = (int) (m.getHeight() * 0.2);
        minDistance_x = minDistance_x > 1 ? minDistance_x : 2;
        minDistance_y = minDistance_y > 1 ? minDistance_y : 2;

        // Checking that the islands' starting points
        // are not too close together
        int okIslands = 0;
        while (true) {
            if (okIslands == islandNum) {
                break;
            } else {
                okIslands = 0;
            }
            for (int i = 0; i < islandNum; i++) {
                int x;
                int y;
                if (islands_x[i][0] == 0) {
                    x = rng.nextInt(m.getWidth()
                        - 1 - 2 * minDistance_x) + minDistance_x;
                    y = rng.nextInt(m.getHeight()
                        - 1 - 2 * minDistance_y) + minDistance_y;
                } else {
                    x = islands_x[i][0];
                    y = islands_y[i][0];
                }
                boolean change = false;
                for (int j = 0; j < islandNum; j++) {
                    if (i == j) {
                        continue;
                    }
                    if (islands_x[j][0] != 0) {
                        int x_diff = x - islands_x[j][0];
                        int y_diff = y - islands_y[j][0];
                        x_diff = x_diff > 0 ? x_diff : - x_diff;
                        y_diff = y_diff > 0 ? y_diff : - y_diff;
                        if (x_diff < minDistance_x) {
                            x = rng.nextInt(m.getWidth()
                                - 1 - 2 * minDistance_x) + minDistance_x;
                            change = true;
                        } else if (y_diff < minDistance_y) {
                            y = rng.nextInt(m.getHeight()
                                - 1 - 2 * minDistance_y) + minDistance_y;
                            change = true;
                        }
                    }
                }
                if (change == false) {
                    okIslands++;
                }

                islands_x[i][0] = x;
                islands_y[i][0] = y;
                
                m.getMap()[x][y].setType(Type.LAND);
                landTiles++;
            }
        }
        int[] landTilesPerIsland = new int[islandNum];
        int[] indices = new int[islandNum];
        landForming:
        while (landTiles < landTilesGoal) {
            for (int n = 0; n < islandNum; n++) {
                int x = islands_x[n][indices[n]];
                int y = islands_y[n][indices[n]];
                boolean changed = false;
                Tile chosen = null;

                Tile[] adjacentTiles = adjacentTiles(x, y);
                int emptyAdjacentTileNum = 0;

                for (int i = 0; i < 8; i++) {
                    if (adjacentTiles[i] != null) {
                        if (adjacentTiles[i].getType() == Type.EMPTY) {
                            emptyAdjacentTileNum++;
                        }
                    }
                }
                if (emptyAdjacentTileNum > 0) {
                    Tile[] emptyAdjacentTiles = new Tile[emptyAdjacentTileNum];
                    emptyAdjacentTileNum = 0;
                    for (int i = 0; i < 8; i++) {
                        if (adjacentTiles[i] != null) {
                            if (adjacentTiles[i].getType() == Type.EMPTY) {
                                emptyAdjacentTiles[emptyAdjacentTileNum++]
                                    = adjacentTiles[i];
                            }
                        }
                    }
                    chosen = emptyAdjacentTiles[
                        rng.nextInt(emptyAdjacentTileNum)];
                    changed = true;
                }
                if (changed && chosen != null) {
                    m.getTile(chosen.getX(), chosen.getY()).setType(Type.LAND);
                    landTilesPerIsland[n]++;
                    islands_x[n][landTilesPerIsland[n]] = chosen.getX();
                    islands_y[n][landTilesPerIsland[n]] = chosen.getY();
                    landTiles++;
                    indices[n] = landTilesPerIsland[n];

                } else {
                    indices[n]--;
                    if (indices[n] == 0) {
                        break landForming;
                    }
                }
                
            }
        }

    }
    // If there are lonely tiles that are 
    // not attached to any tile of the same type,
    // those lonely tiles are made to conform to the surroundings.
    // Limit defines the amount of tiles that,
    // when grouped together, counts as "single"
    public void removeSingles(int limit) {

        Type current = m.getTile(0, 0).getType();

        for (int x = 0; x < m.getWidth(); x++) {
            for (int y = 0; y < m.getHeight(); y++) {
                if (m.getTile(x, y).getType() != current) {
                    Tile[] surroundingTiles = adjacentTiles(x, y);
                    int similarTiles = 0;
                    for (int i = 0; i < surroundingTiles.length; i++) {
                        if (surroundingTiles[i] != null) {
                            if (surroundingTiles[i].getType()
                                == m.getTile(x, y).getType()) {
                                similarTiles++;
                                if (similarTiles == limit) {
                                    break;
                                }
                            }
                        }
                    }
                    if (similarTiles < limit && limit >= 4) {
                        if (similarTiles < limit - 1) {
                            m.getTile(x, y).setType(current);
                        } else if (similarTiles == limit - 1) {
                            if (rng.nextDouble() < 0.5) {
                                m.getTile(x, y).setType(current);
                            }
                        }
                    } else if (similarTiles < limit) {
                        m.getTile(x, y).setType(current);
                    } else {
                        current = m.getTile(x, y).getType();
                    }
                }
            }
        }
    }

    // Converts Sea-areas into Lakes
    // if they do not touch the border; are surrounded by land
    private void processEntities() {
        Tile[][] entities = entities();
        for (Tile[] entity: entities) {
            if (entity[0].getType() == Type.SEA) {
                if (!areaConnectedToBorder(entity)) {
                    for (Tile tile: entity) {
                        tile.setType(Type.LAKE);
                    }
                }
            }
        }
    }

    // Calculate if the area of tiles touches the border at any point
    private boolean areaConnectedToBorder(Tile[] area) {
        boolean connected = false;
        for (Tile tile: area) {
            if (tile.getX() == 0 || tile.getX() == m.getWidth() - 1 
                || tile.getY() == 0 || tile.getY() == m.getHeight() - 1) {
                connected = true;
                break;
            }
        }
        return connected;
    }

    private Tile[][] entities() {
        Tile[][] entities = new Tile[m.getWidth() * m.getHeight() / 2][];
        int ind = 0;
        boolean[][] processed = new boolean[m.getWidth()][m.getHeight()];
        for (int x = 0; x < m.getWidth(); x++) {
            for (int y = 0; y < m.getHeight(); y++) {
                if (processed[x][y]) {
                    continue;
                } else {
                    Tile[] entity = entityAt(x, y, processed);
                    entities[ind] = entity;
                    ind++;
                }
            }
        }
        Tile[][] final_entities = new Tile[ind][];
        for (int i = 0; i < ind; i++) {
            final_entities[i] = entities[i];
        }
        return final_entities;
    }

    // A group of Tiles of same type that are linked together through adjacency
    private Tile[] entityAt(int x, int y, boolean[][] processed) {
        amt = 1;
        boolean[][] included = new boolean[m.getWidth()][m.getHeight()];
        boolean[][] flagged = new boolean[m.getWidth()][m.getHeight()];
        included[x][y] = true;
        processTile(x, y, processed, included, flagged);
        Tile[] tiles = new Tile[amt];
        int ind = 0;
        for (int i = 0; i < m.getWidth(); i++) {
            for (int j = 0; j < m.getHeight(); j++) {
                if (included[i][j]) {
                    tiles[ind++] = m.getTile(i, j);
                }
            }
        }
        return tiles;
    }

    private void processTile(int x, int y,
        boolean[][] processed, boolean[][] included, boolean[][] flagged) {
        if (processed[x][y]) {
            return;
        }
        included[x][y] = true;
        Tile[] processable = unprocessedSimilarAdjacent(x, y,
            processed, included, flagged);
        if (processable != null) {
            for (int i = 0; i < processable.length; i++) {
                if (!(processed[processable[i].getX()][processable[i].getY()]
                    || included[processable[i].getX()]
                        [processable[i].getY()])) {
                    processTile(processable[i].getX(), processable[i].getY(),
                        processed, included, flagged);
                }
            }
        }
        processed[x][y] = true;
    }

    private Tile[] unprocessedSimilarAdjacent(int x, int y,
        boolean[][] processed, boolean[][] included, boolean[][] flagged) {
        Tile[] tiles = new Tile[8];
        int num = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x + i < 0 || x + i > m.getWidth() - 1
                    || y + j < 0 || y + j > m.getHeight() - 1) {
                    continue;
                } else if (i == 0 && j == 0) {
                    continue;
                } else if (processed[x + i][y + j]) {
                    continue;
                } else if (included[x + i][y + j]) {
                    continue;
                } else if (flagged[x + i][y + j]) {
                    continue;
                }  else if (m.getTile(x + i, y + j).getType()
                    == m.getTile(x, y).getType()) {
                    tiles[num] = m.getTile(x + i, y + j);
                    flagged[x + i][y + j] = true;
                    amt++;
                    num++;
                }
            }
        }
        if (num == 0) {
            return null;
        } else {
            Tile[] final_tiles = new Tile[num];
            for (int i = 0; i < num; i++) {
                final_tiles[i] = tiles[i];
            }
            return final_tiles;
        }
    }
    
    private void sortTileArray(Tile[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {  
            for (int j = i + 1; j < arr.length; j++) { 
                // System.out.println(i + " compared to " + j);
                Tile tmp;
                if (arr[i].compareTo(arr[j]) == 1) {  
                    tmp = arr[i];  
                    arr[i] = arr[j];  
                    arr[j] = tmp;  
                }
            }
        }
    }

    public Tile[] adjacentTiles(int x, int y) {
        Tile[] tiles = new Tile[8];
        int ind = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x + i < 0 || x + i > m.getWidth() - 1
                    || y + j < 0 || y + j > m.getHeight() - 1) {
                    tiles[ind] = null;
                } else if (i == 0 && j == 0) {
                    continue;
                }  else {
                    tiles[ind] = m.getTile(x + i, y + j);
                }
                ind++;
            }
        }
        return tiles;
    }

    public int getIslandNum() {
        return islandNum;
    }

    public double getLandToSeaRatio() {
        return landToSeaRatio;
    }
}
