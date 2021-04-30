package karttageneraattori.Logic;

import java.util.Random;
/**
 * The Generator handles initializing any given Map.
 * <p>
 */
public class Generator {
    
    private int islandNum;
    private double landToSeaRatio;
    private double forestChance;
    private double chanceToDiscardSmall;
    private Random rng;
    private Map m;
    // Used when determining areas
    private int amt; 
    private TileQueue tq;


    public Generator(int islandNum, double landToSeaRatio,
        double forestChance, double chanceToDiscardSmall, Random random) {
        this.m = new Map(100, 100);
        this.islandNum = islandNum;
        this.landToSeaRatio = landToSeaRatio;
        this.forestChance = forestChance;
        this.chanceToDiscardSmall = chanceToDiscardSmall;
        this.rng = random;
        tq = new TileQueue(m.getWidth() * m.getHeight());
    }

    public Generator(Random random) {
        this.m = new Map(100, 100);
        this.rng = random;
        this.islandNum = 2;
        this.landToSeaRatio = 0.3;
        this.forestChance = 0.6;
        this.chanceToDiscardSmall = 0.5;
        tq = new TileQueue(m.getWidth() * m.getHeight());
    }

    public void newMap(int x, int y) {
        m = new Map(m, x, y);
    }

    public Map getMap() {
        return m;
    }

    /**
     * Generates random values for variables
     *  islandNum, landToSeaRatio, forestChance and chanceToDiscardSmall.
     */
    public void newValues() {
        setIslandNum(rng.nextInt(3) + 1);
        setLandToSeaRatio(((double) rng.nextInt(4) / 20.0) + 0.2);
        setForestChance(((double) rng.nextInt(12) / 20) + 0.3); 
        setChanceToDiscardSmall(((double) rng.nextInt(12) / 20) + 0.2);
    }

    public void setIslandNum(int islandNum) {
        this.islandNum = islandNum;
    }

    public void setLandToSeaRatio(double landToSeaRatio) {
        this.landToSeaRatio = landToSeaRatio;
    }

    public void setForestChance(double forestChance) {
        this.forestChance = forestChance;
    }

    public void setChanceToDiscardSmall(double chanceToDiscardSmall) {
        this.chanceToDiscardSmall = chanceToDiscardSmall;
    }

    /**
     * The Generator's current Map is initialized.
     * The map's borders are made into SEA-tiles.
     * LAND-tiles are placed based on 1-3 random starting points.
     *  Any single tiles are removed
     *  and made to conform to the surrounding tiles.
     *  Remaining EMPTY-tiles are turned into SEA-tiles.
     *  Very small islands or lakes may be discarded.
     *  Any SEA-areas are turned into LAKE-areas, if landlocked.
     *  Borders are drawn on LAND- and LAKE-areas.
     *  FOREST-areas may be created onto LAND-areas.
     *  Lastly, any single tiles are removed again,
     *  to tidy up the generated forests.
     */
    public void initMap() {
        long start = System.currentTimeMillis();
        long alg = start;
        double time;

        landforming();
        time = (double) (System.currentTimeMillis() - alg) * 0.001;
        System.out.println("Islands formed in " + time);
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
        System.out.println("Filled in lakes, "
            + "removed some small areas, "
            + "created forests, and filled in borders in " + time);
        alg = System.currentTimeMillis();

        removeSingles(3);
        time = (double) (System.currentTimeMillis() - alg) * 0.001;
        System.out.println("Removed lonely tiles (2) in " + time);
        alg = System.currentTimeMillis();

        time = (double) (System.currentTimeMillis() - start) * 0.001;
        System.out.println("Generating the map took "
            + time + " seconds altogether");
    }


    /**
     * Turns any EMPTY Tiles into SEA.
     */
    public void fillInSea() {
        for (int y = 0; y < m.getHeight(); y++) {
            for (int x = 0; x < m.getWidth(); x++) {
                if (m.getMap()[x][y].getType() == Type.EMPTY) {
                    m.getMap()[x][y].setType(Type.SEA);
                }
            }
        }
    }

    /**
     * Sets island starting positions for where forming LAND begins.
     *  Ensures that the positions are not too close to each other.
     *  Returns the list of Tiles that belong to each island.
     *  Requires integer for how many LAND Tiles should be in the Map.
     * @param int landTilesGoal 
     * @return Tile[][] islands
     */
    public Tile[][] islandsStartPos(int landTilesGoal) {
        Tile[][] islands = new Tile[islandNum][landTilesGoal + 1];

        int minDistance_x = (int) (m.getWidth() * 0.2); 
        int minDistance_y = (int) (m.getHeight() * 0.2);
        minDistance_x = minDistance_x > 1 ? minDistance_x : 2;
        minDistance_y = minDistance_y > 1 ? minDistance_y : 2;

        int okIslands = 0;
        while (true) {
            // When all starting positions have been confirmed as far enough
            // from the borders and from each other,
            // the starting positions are ready and the loop breaks.
            if (okIslands == islandNum) {
                break;
            } else {
                okIslands = 0;
            }
            for (int i = 0; i < islandNum; i++) {
                int x;
                int y;
                if (islands[i][0] == null) {
                    // Ensures that the starting position is not too close
                    // to the borders of the map
                    x = rng.nextInt(m.getWidth()
                        - 1 - 2 * minDistance_x) + minDistance_x;
                    y = rng.nextInt(m.getHeight()
                        - 1 - 2 * minDistance_y) + minDistance_y;
                } else {
                    x = islands[i][0].getX();
                    y = islands[i][0].getY();
                }
                // Iterates through the other initialized starting positions
                // and checks if the current one is far enough from them
                boolean mustResetIsland = false;
                for (int j = 0; j < islandNum; j++) {
                    if (i == j) {
                        continue;
                    }
                    if (islands[j][0] != null) {
                        int x_diff = x - islands[j][0].getX();
                        int y_diff = y - islands[j][0].getY();
                        x_diff = x_diff > 0 ? x_diff : - x_diff;
                        y_diff = y_diff > 0 ? y_diff : - y_diff;
                        if (x_diff < minDistance_x) {
                            x = rng.nextInt(m.getWidth()
                                - 1 - 2 * minDistance_x) + minDistance_x;
                            mustResetIsland = true;
                        } else if (y_diff < minDistance_y) {
                            y = rng.nextInt(m.getHeight()
                                - 1 - 2 * minDistance_y) + minDistance_y;
                            mustResetIsland = true;
                        }
                    }
                }
                if (mustResetIsland == false) {
                    okIslands++;
                }

                islands[i][0] = m.getTile(x, y);
                m.getMap()[x][y].setType(Type.LAND);
            }
        }
        return islands;
    }

    /**
     * Creates islands based on starting positions. 
     *  Iterates through the islands and adds a new LAND Tile to each list
     *  during each loop by picking a random EMPTY Tile from the surrounding
     *  Tiles of the current Tile. If no EMPTY Tiles surround it, the next
     *  Tile that will be checked for them will be picked from the previously
     *  added Tiles. 
     */
    public void landforming() {
        int totalTiles = (m.getWidth()) * (m.getHeight());
        int landTiles = 0;
        int landTilesGoal = (int) (totalTiles * landToSeaRatio);

        Tile[][] islands = islandsStartPos(landTilesGoal);
        landTiles += islandNum;

        int[] landTilesPerIsland = new int[islandNum];
        int[] indices = new int[islandNum];

        landForming:
        while (landTiles < landTilesGoal) {
            for (int n = 0; n < islandNum; n++) {
                Tile chosen = islands[n][indices[n]];
                boolean changedChosen = false;
                Tile[] adjacentTiles
                    = adjacentTiles(chosen.getX(), chosen.getY());
                int emptyAdjacentTileNum = 0;

                for (int i = 0; i < 8; i++) {
                    if (adjacentTiles[i] != null) {
                        if (adjacentTiles[i].getType() == Type.EMPTY) {
                            emptyAdjacentTileNum++;
                        }
                    }
                }
                // Picks a random EMPTY Tile from the adjacent Tiles, if able
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
                    changedChosen = true;
                }
                // New chosen Tile is set into the island
                if (changedChosen) {
                    chosen.setType(Type.LAND);
                    landTilesPerIsland[n]++;
                    islands[n][landTilesPerIsland[n]] = chosen;
                    landTiles++;
                    indices[n] = landTilesPerIsland[n];
                // If there were no EMPTY Tiles adjacent, previously added
                // Tiles are checked for any. If none exist, the loop is
                // aborted to prevent infinite loops.
                } else {
                    indices[n]--;
                    if (indices[n] == 0) {
                        break landForming;
                    }
                }
                
            }
        }

    }

    
    /**
     * If there are lonely tiles that are
     *  not attached to any tile of the same type,
     *  those lonely tiles are made to conform to the surroundings.
     *  Limit defines the amount of tiles that,
     *  when grouped together, count as "single"; too small.
     * @param int limit
     */
    public void removeSingles(int limit) {
        Type current = m.getTile(0, 0).getType();
        for (int x = 0; x < m.getWidth(); x++) {
            for (int y = 0; y < m.getHeight(); y++) {
                if (m.getTile(x, y).getType() != current) {
                    Tile[] surroundingTiles = adjacentTiles(x, y);
                    int similarTiles = 0;
                    // Checks the adjacent Tiles for Tiles of similar type.
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
                    // Checks if the adjacent similar Tiles are too few.
                    boolean changeTile = false;
                    if (similarTiles < limit && limit >= 4) {
                        if (similarTiles < limit - 1) {
                            changeTile = true;
                        } else if (similarTiles == limit - 1) {
                            if (rng.nextDouble() < 0.5) {
                                changeTile = true;
                            }
                        }
                    } else if (similarTiles < limit) {
                        changeTile = true;
                    }
                    Type thisType = m.getTile(x, y).getType();
                    if (changeTile && thisType != Type.LAND_BORDER
                        && thisType != Type.LAKE_BORDER) {
                        m.getTile(x, y).setType(current);
                    } else {
                        if (m.getTile(x, y).getType() == Type.LAND_BORDER) {
                            current = Type.LAND;
                        } else if (m.getTile(x, y).getType()
                            == Type.LAKE_BORDER) {
                            current = Type.LAKE;
                        } else {
                            current = m.getTile(x, y).getType();
                        }
                    }
                }
            }
        }
    }

    /**
     * Makes a list of entities and processes them:
     *  turning inland SEA-areas into LAKE,
     *  discarding some areas that are very small,
     *  creating borders for islands and lakes, 
     *  and possibly creating forest on islands.
     */
    public void processEntities() {
        double chanceToDiscardTiny = chanceToDiscardSmall + 0.3 < 0.95
            ? chanceToDiscardSmall + 0.3 : 0.95;

        int tiny_size = (int) (m.getWidth() * m.getHeight() * 0.0075);
        int small_size = (int) (m.getWidth() * m.getHeight() * 0.015);
        
        Tile[][] entities = entities();
        for (Tile[] entity: entities) {
            if (entity[0].getType() == Type.SEA) {
                if (!areaConnectedToBorder(entity)) {
                    for (Tile tile: entity) {
                        tile.setType(Type.LAKE);
                    }
                }
            }
            // Discarding some entities that are small to reduce archipelago
            if (entity.length <= tiny_size) {
                if (rng.nextDouble() < chanceToDiscardTiny) {
                    discardEntity(entity);
                }
            } else if (entity.length <= small_size) {
                if (rng.nextDouble() < chanceToDiscardSmall) {
                    discardEntity(entity);
                }
            }
        }

        // Process the entities that are left
        for (Tile[] entity: entities) {
            if (entity[0].getType() == Type.LAKE) {
                // If land around a lake was discarded,
                // the lake is also converted into sea. 
                if (!areaSurroundedByLand(entity)) {
                    for (Tile tile: entity) {
                        tile.setType(Type.SEA);
                    }
                } else {
                    // Borders given for lakes
                    for (Tile tile: entity) {
                        int lakeTiles = 0;
                        int nulls = 0;
                        for (Tile t: adjacentTiles(tile.getX(), tile.getY())) {
                            if (t == null) {
                                nulls++;
                            } else if (t.getType() == Type.LAKE
                                || t.getType() == Type.LAKE_BORDER) {
                                lakeTiles++;
                            }
                        }
                        if (lakeTiles < 8 - nulls) {
                            tile.setType(Type.LAKE_BORDER);
                        }
                    }
                }
            } else if (entity[0].getType() == Type.LAND) {
                // Borders given for islands
                for (Tile tile: entity) {
                    int seaTiles = 0;
                    for (Tile t: adjacentTiles(tile.getX(), tile.getY())) {
                        if (t == null) {
                            continue;
                        } else if (t.getType() == Type.SEA) {
                            seaTiles++;
                        }
                    }
                    if (seaTiles >= 2) {
                        tile.setType(Type.LAND_BORDER);
                    }
                }
                // Islands may generate forests
                if (rng.nextDouble() < forestChance) {
                    createForest(entity);
                }
            }
        }
    }

    /**
     * Generates a FOREST within a LAND-entity
     *  with a random coverage percentage.
     *  Works in a similar way to landforming().
     * @param Tile[] island
     */
    public void createForest(Tile[] island) {
        // Determines how much of the island the forest initially covers
        double coverage = ((double) rng.nextInt(13) / 20) + 0.1;
        int forestTilesGoal = (int) (island.length * coverage);
        if (forestTilesGoal < 1) {
            coverage += 0.3;
            forestTilesGoal = (int) (island.length * coverage);
            // May happen if the chosen land area is too small; then abort
            if (forestTilesGoal < 1) {
                return;
            }
        }
        int forestTiles = 0;
        // Random starting position for the forest
        Tile[] forest = new Tile[forestTilesGoal];
        Tile current = island[rng.nextInt(island.length)];
        forest[0] = current;
        current.setType(Type.FOREST);
        forestTiles++;
        
        int addInd = 1;
        int ind = 1;
        forestForming:
        while (forestTiles < forestTilesGoal) {
            Tile[] adjacentTiles
                = adjacentTiles(current.getX(), current.getY());
            int emptyAdjacentTileNum = 0;
            for (int i = 0; i < 8; i++) {
                if (adjacentTiles[i] != null) {
                    if (adjacentTiles[i].getType() == Type.LAND) {
                        emptyAdjacentTileNum++;
                    }
                }
            }
            if (emptyAdjacentTileNum > 0) {
                Tile[] emptyAdjacentTiles = new Tile[emptyAdjacentTileNum];
                emptyAdjacentTileNum = 0;
                for (int i = 0; i < 8; i++) {
                    if (adjacentTiles[i] != null) {
                        if (adjacentTiles[i].getType() == Type.LAND) {
                            emptyAdjacentTiles[emptyAdjacentTileNum++]
                                = adjacentTiles[i];
                        }
                    }
                }
                current = emptyAdjacentTiles[
                    rng.nextInt(emptyAdjacentTileNum)];
                current.setType(Type.FOREST);
                forest[addInd] = current;
                forestTiles++;
                addInd++;
                ind = addInd;
            } else {
                ind--;
                if (ind < 0) {
                    break forestForming;
                } else  {
                    current = forest[ind];
                }
            }
        }
    }

    /**
     * "Discards" the entity by choosing an entity type adjacent to it
     *  and turning all Tiles in the entity into that type.
     * @param entity
     */
    public void discardEntity(Tile[] entity) {
        Type newType = typeAdjacentToEntity(entity);
        for (Tile tile: entity) {
            tile.setType(newType);
        }
    }

    /**
     * Returns a type that is adjacent to some Tile of the entity.
     *  If no differing types, returns the entity's own type.
     * @param entity
     * @return Type
     */
    public Type typeAdjacentToEntity(Tile[] entity) {
        Type entityType = entity[0].getType();
        for (Tile tile: entity) {
            for (Tile t: adjacentTiles(tile.getX(), tile.getY())) {
                if (t != null) {
                    if (t.getType() != entityType) {
                        return t.getType();
                    }
                }
            }
        }
        return entityType;
    }

    /**
     * Checks if the given area is connected to the border by checking
     *  if any of the Tiles are in the borders.
     *  If there are enough such Tiles, the area is considered connected.
     *  The limit exists to create more realistic LAKE-areas;
     *  if the area is connected to the border by only a few Tiles but is
     *  otherwise surrounded by LAND, it looks better as a LAKE than as a SEA.
     * @param area
     * @return connected
     */
    public boolean areaConnectedToBorder(Tile[] area) {
        int connected = 0;
        int oceanRequirement = (int) ((m.getHeight() + m.getWidth()) / 2 * 0.1);
        for (Tile tile: area) {
            if (tile.getX() == 0 || tile.getX() == m.getWidth() - 1 
                || tile.getY() == 0 || tile.getY() == m.getHeight() - 1) {
                connected++;
                if (connected == oceanRequirement) {
                    break;
                }
            }
        }
        return connected >= oceanRequirement;
    }

    /**
     * Checks that the given LAKE-area is surrounded by LAND only.
     *  Used to ensure that a LAKE is discarded when its surrounding LAND is.
     * @param area
     * @return landlocked
     */
    public boolean areaSurroundedByLand(Tile[] area) {
        boolean notAdjacentToSea = true;
        areaChecking:
        for (Tile tile: area) {
            for (Tile t: adjacentTiles(tile.getX(), tile.getY())) {
                if (t != null) {
                    if (t.getType() == Type.SEA) {
                        notAdjacentToSea = false;
                        break areaChecking;
                    }
                }
            }
        }
        return notAdjacentToSea;
    }

    /**
     * Builds a list of all entities.
     *  Iterates through the map: if the given coordinate hasn't been
     *  processed, it means it has a new entity connected to it, and the entity
     *  will be assembled.
     * @return entities
     */
    public Tile[][] entities() {
        Tile[][] entities = new Tile[(int) (m.getWidth() * m.getHeight())][];
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
        // Assembles a list without null values in it
        Tile[][] final_entities = new Tile[ind][];
        for (int i = 0; i < ind; i++) {
            final_entities[i] = entities[i];
        }
        return final_entities;
    }

    /**
     * Assembles a Tile[] list of Tiles that are connected by being of the same
     *  type.
     * @param x x-coordinate where the entity begins
     * @param y y-coordinate where the entity begins
     * @param processed List of processed Tiles so far
     * @return Tile[] entity
     */
    public Tile[] entityAt(int x, int y, boolean[][] processed) {
        amt = 1;
        // Used to check the Tiles in the Map that are included in this entity.
        boolean[][] included = new boolean[m.getWidth()][m.getHeight()];
        // Used to flag to-be-processed Tiles so they do not get included
        // more than once; saves processing time.
        boolean[][] flagged = new boolean[m.getWidth()][m.getHeight()];
        tq.push(m.getTile(x, y));
        // Searches connected Tiles beginning from the first Tile.
        searchEntity(processed, included, flagged);
        // Creates the list by checking each Tile from the included-array.
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

    /**
     * Adjacent Tiles of the same type are pushed into a TileQueue.
     *  The loop processes all of the Tiles in the queue until it is empty.
     *  During the processing, included-array is filled.
     *  Also fills the processed-array to prevent reprocessing.
     * @param processed array of processed Tiles
     * @param included array of Tiles included in this entity
     * @param flagged array of Tiles that will be checked later
     */
    public void searchEntity(
        boolean[][] processed, boolean[][] included, boolean[][] flagged) {
        while (true) {
            Tile tile = tq.pop();
            included[tile.getX()][tile.getY()] = true;
            Tile[] surrounding
                = unprocessedSimilarAdjacent(tile.getX(), tile.getY(),
            processed, included, flagged);
            if (surrounding != null) {
                for (Tile t: surrounding) {
                    if (!(processed[t.getX()][t.getY()]
                        || included[t.getX()][t.getY()])) {
                        tq.push(t);
                    }
                }
            }
            processed[tile.getX()][tile.getY()] = true;
            if (tq.isEmpty()) {
                break;
            }
        }
    }

    /**
     * 
     * @param x x-coordinate of the Tile in process
     * @param y y-coordinate of the Tile in process
     * @param processed array of processed Tiles
     * @param included array of Tiles included in this entity
     * @param flagged array of Tiles that will be checked later
     * @return Tile[] adjacent Tiles that are of the same Type and unprocessed
     */
    public Tile[] unprocessedSimilarAdjacent(int x, int y,
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

    /**
     * Creates a list of Tiles adjacent to the Tile in given coordinates.
     *  If the adjacent Tile is out of bounds, it will be null.
     * @param x x-coordinate of the Tile in process
     * @param y y-coordinate of the Tile in process
     * @return Tile[] adjacent Tiles
     */
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
