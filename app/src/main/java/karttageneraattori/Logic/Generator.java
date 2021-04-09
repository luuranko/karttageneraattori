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

    public Generator(int islandNum, double landToSeaRatio, Random random) {
        this.m = new Map(100, 100);
        this.islandNum = islandNum;
        this.landToSeaRatio = landToSeaRatio;
        this.rng = random;
    }

    public Generator(Random random) {
        this.m = new Map(100, 100);
        this.rng = random;
        setIslandNum(2);
        setLandToSeaRatio(0.3);
    }

    public void newMap(int x, int y) {
        m = new Map(m, x, y);
    }

    public Map getMap() {
        return m;
    }

    public void newValues() {
        setIslandNum(rng.nextInt(3) + 1);
        setLandToSeaRatio(((double) rng.nextInt(7) / 20.0) + 0.2);
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
     * The map's borders are cleaned so that the islands
     *  do not press into the map's borders too much.
     * Any single tiles are removed
     *  and made to conform to the surrounding tiles.
     * Lastly, remaining EMPTY-tiles are turned into SEA-tiles.
     * <p>
     * @return      void
     */
    public void initMap() {
        // System.out.println("Islands: " + islandNum);
        // System.out.println("Land to Sea ratio: " + landToSeaRatio);
        long start = System.currentTimeMillis();
        seaBorders();
        // System.out.println("Sea borders done");
        createIslands();
        // System.out.println("Islands created");
        cleanLandBorders();
        // System.out.println("Borders cleaned");
        removeSingles();
        // System.out.println("Removed lonely tiles");
        fillInSea();
        // System.out.println("Sea filled in");
        // fillInLakes(map);
        // System.out.println("Lakes filled in");
        long end = System.currentTimeMillis();
        double time = (double) (end - start) * 0.01;
        System.out.println("Generating the map took " + time + " seconds");
    }

    // Makes the borders type SEA
    public void seaBorders() {
        for (int x = 0; x < m.getWidth(); x++) {
            m.getMap()[x][0].setType(Type.SEA);
            m.getMap()[x][m.getHeight() - 1].setType(Type.SEA);
            
        }
        for (int y = 1; y < m.getHeight() - 1; y++) {
            m.getMap()[0][y].setType(Type.SEA);
            m.getMap()[m.getWidth() - 1][y].setType(Type.SEA);
        }
    }

    // Makes all type EMPTY tiles type SEA
    public void fillInSea() {
        for (int y = 1; y < m.getHeight() - 1; y++) {
            for (int x = 1; x < m.getWidth() - 1; x++) {
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
        // TODO Use these arrays to check if the islands connect to each other
        int[][] islands_x = new int[islandNum][landTilesGoal + 1];
        int[][] islands_y = new int[islandNum][landTilesGoal + 1];

        int minDistance_x = (int) (m.getWidth() * 0.2); 
        int minDistance_y = (int) (m.getHeight() * 0.2);
        minDistance_x = minDistance_x > 1 ? minDistance_x : 2;
        minDistance_y = minDistance_y > 1 ? minDistance_y : 2;

        // Checking that the islands' starting points are not too close together
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

                for (int i = 0; i < 6; i++) {
                    if (adjacentTiles[i] != null) {
                        if (adjacentTiles[i].getType() == Type.EMPTY) {
                            emptyAdjacentTileNum++;
                        }
                    }
                }
                if (emptyAdjacentTileNum > 0) {
                    Tile[] emptyAdjacentTiles = new Tile[emptyAdjacentTileNum];
                    emptyAdjacentTileNum = 0;
                    for (int i = 0; i < 6; i++) {
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

    public void cleanLandBorders() {
        int x_cuttableLength = (int) (m.getWidth() * 0.04)
            > 1 ? (int) (m.getWidth() * 0.04) : 2;
        int y_cuttableLength = (int) (m.getHeight() * 0.04)
            > 1 ? (int) (m.getHeight() * 0.04) : 2;
        double chanceToRemove = 0.9;
        int succession_1 = 0;
        int succession_2 = 0;
        int removedAmount = 0;
        for (int x = 1; x < m.getWidth(); x++) {
            if (m.getMap()[x][1].getType() == Type.LAND) {
                succession_1++;
            } else {
                succession_1 = 0;
            }
            if (succession_1 == x_cuttableLength) {
                if (rng.nextDouble() < chanceToRemove) {
                    removedAmount = x_cuttableLength
                        - rng.nextInt((int) (x_cuttableLength * 0.4) > 0 ?
                            (int) (x_cuttableLength * 0.4) : 1);
                    if (removedAmount <= 0) {
                        removedAmount = 1;
                    }
                    for (int i = x; i > x - removedAmount; i--) {
                        m.getMap()[i][1].setType(Type.SEA);
                    }
                }
                succession_1 = rng.nextInt(x_cuttableLength)
                    - rng.nextInt(x_cuttableLength);
            }
            if (m.getMap()[x][2].getType() == Type.LAND && removedAmount > 0) {
                succession_2 += removedAmount;
            }
            if (succession_2 >= (x_cuttableLength - 1)) {
                if (rng.nextDouble() < chanceToRemove) {
                    removedAmount -= rng.nextInt(3);
                    if (removedAmount <= 0) {
                        removedAmount = 1;
                    }
                    for (int i = x; i > x - removedAmount; i--) {
                        m.getMap()[i][2].setType(Type.SEA);
                    }
                }
                succession_2 = rng.nextInt((x_cuttableLength - 1))
                    - rng.nextInt((x_cuttableLength - 1));
            }
            removedAmount = 0;
            
        }
        // System.out.println("Processed top");
        succession_1 = 0;
        succession_2 = 0;
        for (int x = 1; x < m.getWidth(); x++) {
            if (m.getMap()[x][m.getHeight() - 2].getType() == Type.LAND) {
                succession_1++;
            } else {
                succession_1 = 0;
            }
            if (succession_1 == x_cuttableLength) {
                if (rng.nextDouble() < chanceToRemove) {
                    removedAmount = x_cuttableLength
                        - rng.nextInt((int) (x_cuttableLength * 0.4) > 0 ?
                            (int) (x_cuttableLength * 0.4) : 1);
                    if (removedAmount <= 0) {
                        removedAmount = 1;
                    }
                    for (int i = x; i > x - removedAmount; i--) {
                        m.getMap()[i][m.getHeight() - 2].setType(Type.SEA);
                    }
                }
                succession_1 = rng.nextInt(x_cuttableLength)
                    - rng.nextInt(x_cuttableLength);
            }
            if (m.getMap()[x][m.getHeight() - 3].getType() == Type.LAND
                    && removedAmount > 0) {
                succession_2 += removedAmount;
            }
            if (succession_2 >= x_cuttableLength - 1) {
                if (rng.nextDouble() < chanceToRemove) {
                    removedAmount -= rng.nextInt(3);
                    if (removedAmount <= 0) {
                        removedAmount = 1;
                    }
                    for (int i = x; i > x - removedAmount; i--) {
                        m.getMap()[i][m.getHeight() - 3].setType(Type.SEA);
                    }
                }
                succession_2 = rng.nextInt((x_cuttableLength - 1))
                    - rng.nextInt((x_cuttableLength - 1));
            }
            removedAmount = 0;
        }
        // System.out.println("Processed below");
        succession_1 = 0;
        succession_2 = 0;
        for (int y = 1; y < m.getHeight() - 2; y++) {
            if (m.getMap()[1][y].getType() == Type.LAND) {
                succession_1++;
            } else {
                succession_1 = 0;
            }  
            if (succession_1 == y_cuttableLength) {
                if (rng.nextDouble() < chanceToRemove) {
                    // Removes the entire length or slightly less
                    removedAmount = y_cuttableLength
                        - rng.nextInt((int) (y_cuttableLength * 0.4) > 0 ?
                            (int) (y_cuttableLength * 0.4) : 1);
                    if (removedAmount <= 0) {
                        removedAmount = 1;
                    }
                    for (int i = y; i > y - removedAmount; i--) {
                        m.getMap()[1][i].setType(Type.SEA);
                    }
                }
                succession_1 = rng.nextInt(y_cuttableLength)
                    - rng.nextInt(y_cuttableLength);
            }
            if (m.getMap()[2][y].getType() == Type.LAND && removedAmount > 0) {
                succession_2 += removedAmount;
            }
            if (succession_2 >= (y_cuttableLength - 1)) {
                if (rng.nextDouble() < chanceToRemove) {
                    removedAmount -= rng.nextInt(3);
                    if (removedAmount <= 0) {
                        removedAmount = 1;
                    }
                    for (int i = y; i > y - removedAmount; i--) {
                        m.getMap()[2][i].setType(Type.SEA);
                    }
                }
                succession_2 = rng.nextInt((y_cuttableLength - 1))
                    - rng.nextInt((y_cuttableLength - 1));
            }
        }
        // System.out.println("Processed left");
        succession_1 = 0;
        succession_2 = 0;
        for (int y = 1; y < m.getHeight() - 2; y++) {
            if (m.getMap()[m.getWidth() - 2][y].getType() == Type.LAND) {
                succession_1++;
            } else {
                succession_1 = 0;
            }
            if (succession_1 == y_cuttableLength) {
                if (rng.nextDouble() < chanceToRemove) {
                    removedAmount = y_cuttableLength
                        - rng.nextInt((int) (y_cuttableLength * 0.6) > 0 ?
                            (int) (y_cuttableLength * 0.6) : 1);
                    if (removedAmount <= 0) {
                        removedAmount = 1;
                    }
                    for (int i = y; i > y - removedAmount; i--) {
                        m.getMap()[m.getWidth() - 2][i].setType(Type.SEA);
                    }
                }
                succession_1 = rng.nextInt(y_cuttableLength)
                    - rng.nextInt(y_cuttableLength);
            }
            if (m.getMap()[m.getWidth() - 3][y].getType() == Type.LAND
                && removedAmount > 0) {
                succession_2 += removedAmount;
            }
            if (succession_2 >= y_cuttableLength - 1) {
                if (rng.nextDouble() < chanceToRemove) {
                    removedAmount -= rng.nextInt(3);
                    if (removedAmount <= 0) {
                        removedAmount = 1;
                    }
                    for (int i = y; i > y - removedAmount; i--) {
                        m.getMap()[m.getWidth() - 3]
                            [i > 1 ? i : 0].setType(Type.SEA);
                    }
                }
                succession_2 = rng.nextInt(y_cuttableLength - 1)
                    - rng.nextInt(y_cuttableLength - 1);
            }
            removedAmount = 0;
        }
        // System.out.println("Processed right");
        
    }

    // If there are lonely tiles that are 
    // not attached to any tile of the same type,
    // those lonely tiles are made to conform to the surroundings.
    public void removeSingles() {
        Type current = m.getTile(1, 1).getType();

        for (int x = 1; x < m.getWidth() - 1; x++) {
            for (int y = 1; y < m.getHeight() - 1; y++) {
                if (m.getTile(x, y).getType() != current) {
                    Tile[] surroundingTiles = adjacentTiles(x, y);
                    boolean mustBeRemoved = true;
                    for (int i = 0; i < surroundingTiles.length; i++) {
                        if (surroundingTiles[i].getType()
                                == m.getTile(x, y).getType()) {
                            mustBeRemoved = false;
                            break;
                        }
                    }
                    if (mustBeRemoved) {
                        m.getTile(x, y).setType(current);
                    } else {
                        current = m.getTile(x, y).getType();
                    }
                }
            }
        }
    }

    // TODO
    // Determine if the SEA-area should be a lake or not
    // private void fillInLakes() {}

    // TODO
    // Calculate if the area of tiles touches the border at any point
    // private boolean areaConnectedToBorder(Tile tile) {
    //     boolean connected = false;
    //     return connected;
    // }

    public Tile[] adjacentTiles(int x, int y) {
        boolean xIsEven = x % 2 == 0;
        Tile[] tiles = new Tile[6];
        int ind = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (xIsEven && i != 0 && j == 1) {
                    continue;
                } else if (!xIsEven && i != 0 && j == -1) {
                    continue;
                }
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
