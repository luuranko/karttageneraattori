package karttageneraattori;

import org.junit.Before;
import org.junit.Test;

import karttageneraattori.Logic.Generator;
import karttageneraattori.Logic.Tile;
import karttageneraattori.Logic.Type;

import static org.junit.Assert.assertTrue;

import java.util.Random;

public class GeneratorTest {

    private Random rng;
    private Generator g;
    
    @Before public void init() {
        this.rng = new Random(1234567);
        this.g = new Generator(rng);
    }

    @Test public void testConstructor1() {
        g = new Generator(rng);
        assertTrue(g.getMap().getHeight() == 100
            && g.getMap().getWidth() == 100);
        assertTrue(g.getIslandNum() == 2 && g.getLandToSeaRatio() == 0.3);
    }

    @Test public void testConstructor2() {
        g = new Generator(1, 0.4, 0.5, 0.5, rng);
        assertTrue(g.getMap().getHeight() == 100
            && g.getMap().getWidth() == 100);
        assertTrue(g.getIslandNum() == 1 && g.getLandToSeaRatio() == 0.4);
    }

    @Test public void testNewValues() {
        g.newValues();
        assertTrue(g.getIslandNum() >= 1 && g.getIslandNum() <= 3);
        assertTrue(g.getLandToSeaRatio() >= 0.2
            && g.getLandToSeaRatio() <= 0.5);
    }

    @Test public void cannotHaveOver7StartingIslands() {
        assertTrue(!g.setIslandNum(7));
    }

    @Test public void testNewMap() {
        g.newMap(10, 10);
        assertTrue(g.getMap().getWidth() == 10 && g.getMap().getHeight() == 10);
    }

    @Test public void testFillInSea1() {
        g.newMap(4, 4);
        g.fillInSea();
        boolean ok = true;
        for (int x = 0; x < g.getMap().getWidth(); x++) {
            for (int y = 0; y < g.getMap().getHeight(); y++) {
                if (g.getMap().getMap()[x][y].getType() == Type.EMPTY) {
                    ok = false;
                }
            }
        }
        assertTrue(ok);
    }

    @Test public void testFillInSea2() {
        g.getMap().getMap()[1][1].setType(Type.LAND);
        g.fillInSea();
        int seaGoal = g.getMap().getWidth() * g.getMap().getHeight() - 1;
        int seaTiles = 0;
        for (int x = 0; x < g.getMap().getWidth(); x++) {
            for (int y = 0; y < g.getMap().getHeight(); y++) {
                if (g.getMap().getMap()[x][y].getType() == Type.SEA) {
                    seaTiles++;
                }
            }
        }
        assertTrue(seaTiles == seaGoal);
    }

    @Test public void testAdjacentTiles1() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                g.getMap().getMap()[i][j].setType(Type.SEA);
            }
        }
        g.getMap().getMap()[1][1].setType(Type.LAND);
        Tile[] adjacents = g.adjacentTiles(0, 0);
        int nulls = 0;
        int seas = 0;
        int lands = 0;
        for (Tile t: adjacents) {
            if (t == null) {
                nulls++;
            } else if (t.getType() == Type.SEA) {
                seas++;
            } else if (t.getType() == Type.LAND) {
                lands++;
            }
        }
        assertTrue(adjacents.length == 8);
        assertTrue(nulls == 5);
        assertTrue(seas == 2);
        assertTrue(lands == 1);
    }

    @Test public void testAdjacentTiles2() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                g.getMap().getMap()[i][j].setType(Type.SEA);
            }
        }
        g.getMap().getMap()[1][1].setType(Type.LAND);
        Tile[] adjacents = g.adjacentTiles(1, 1);
        int nulls = 0;
        int seas = 0;
        int lands = 0;
        for (Tile t: adjacents) {
            if (t == null) {
                nulls++;
            } else if (t.getType() == Type.SEA) {
                seas++;
            } else if (t.getType() == Type.LAND) {
                lands++;
            }
        }
        assertTrue(adjacents.length == 8);
        assertTrue(nulls == 0);
        assertTrue(seas == 8);
        assertTrue(lands == 0);
    }

    @Test public void testAdjacentTiles3() {
        g.getMap().getMap()[g.getMap().getWidth() - 2]
            [g.getMap().getHeight() - 2].setType(Type.LAND);
        Tile[] adjacents = g.adjacentTiles(g.getMap().getWidth() - 1,
            g.getMap().getHeight() - 1);
        int nulls = 0;
        int emptys = 0;
        int lands = 0;
        for (Tile t: adjacents) {
            if (t == null) {
                nulls++;
            } else if (t.getType() == Type.EMPTY) {
                emptys++;
            } else if (t.getType() == Type.LAND) {
                lands++;
            }
        }
        assertTrue(adjacents.length == 8);
        assertTrue(nulls == 5);
        assertTrue(emptys == 2);
        assertTrue(lands == 1);
    }

    @Test public void testTypeAdjacentToEntity1() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                g.getMap().getMap()[i][j].setType(Type.SEA);
            }
        }
        g.getMap().getMap()[1][1].setType(Type.LAND);
        Tile[] area = {g.getMap().getMap()[1][1]};
        assertTrue(g.typeAdjacentToEntity(area) == Type.SEA);
    }

    @Test public void testTypeAdjacentToEntity2() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                g.getMap().getMap()[i][j].setType(Type.SEA);
            }
        }
        Tile[] area = {g.getMap().getMap()[0][1]};
        assertTrue(g.typeAdjacentToEntity(area) == Type.SEA);
    }

    @Test public void discardEntity() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                g.getMap().getMap()[i][j].setType(Type.SEA);
            }
        }
        g.getMap().getMap()[1][1].setType(Type.LAND);
        Tile[] area = {g.getMap().getMap()[1][1]};
        g.discardEntity(area);

        boolean ok = true;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (g.getMap().getMap()[i][j].getType() != Type.SEA) {
                    ok = false;
                }
            }
        }
        assertTrue(ok);
    }

    @Test public void testAreaSurroundedByLand1() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                g.getMap().getMap()[i][j].setType(Type.LAND);
            }
        }
        g.getMap().getMap()[1][1].setType(Type.SEA);
        Tile[] area = {g.getMap().getMap()[1][1]};
        assertTrue(g.areaSurroundedByLand(area));
    }

    @Test public void testAreaSurroundedByLand2() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                g.getMap().getMap()[i][j].setType(Type.LAND);
            }
        }
        g.getMap().getMap()[0][1].setType(Type.SEA);
        Tile[] area = {g.getMap().getMap()[0][1]};
        assertTrue(g.areaSurroundedByLand(area));
    }

    @Test public void testAreaSurroundedByLand3() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                g.getMap().getMap()[i][j].setType(Type.LAND);
            }
        }
        g.getMap().getMap()[1][1].setType(Type.SEA);
        g.getMap().getMap()[0][1].setType(Type.SEA);
        Tile[] area = {g.getMap().getMap()[0][1]};
        assertTrue(!g.areaSurroundedByLand(area));
    }

    @Test public void islandsStartPosTest1() {
        Tile[][] startPos = g.islandsStartPos((int) (g.getMap().getWidth()
            * g.getMap().getHeight() * g.getLandToSeaRatio()));
        assertTrue(startPos[0][0].getX() == 26 && startPos[0][0].getY() == 32);
        assertTrue(startPos[1][0].getX() == 56 && startPos[1][0].getY() == 66);
    }

    @Test public void islandsStartPosTest2() {
        g.setIslandNum(3);
        Tile[][] startPos = g.islandsStartPos((int) (g.getMap().getWidth()
            * g.getMap().getHeight() * g.getLandToSeaRatio()));
        assertTrue(startPos[0][0].getX() == 76 && startPos[0][0].getY() == 68);
        assertTrue(startPos[1][0].getX() == 47 && startPos[1][0].getY() == 23);
        assertTrue(startPos[2][0].getX() == 26 && startPos[2][0].getY() == 46);
    }

    @Test public void landFormingTest1() {
        int landTiles = g.landforming();
        int realLandTiles = 0;
        for (int x = 0; x < g.getMap().getWidth(); x++) {
            for (int y = 0; y < g.getMap().getHeight(); y++) {
                if (g.getMap().getMap()[x][y].getType() == Type.LAND) {
                    realLandTiles++;
                }
            }
        }
        assertTrue(landTiles == 3000);
        assertTrue(landTiles == realLandTiles);
    }

    @Test public void landFormingTest2() {
        g.setIslandNum(4);
        g.setLandToSeaRatio(0.99);
        int landTiles = g.landforming();
        int realLandTiles = 0;
        for (int x = 0; x < g.getMap().getWidth(); x++) {
            for (int y = 0; y < g.getMap().getHeight(); y++) {
                if (g.getMap().getMap()[x][y].getType() == Type.LAND) {
                    realLandTiles++;
                }
            }
        }
        assertTrue(landTiles == 9738);
        assertTrue(landTiles == realLandTiles);
    }

    @Test public void removeSinglesTest1() {
        int landTiles = g.landforming();
        assertTrue(g.getMap().getTile(45, 48).getType() == Type.EMPTY);
        assertTrue(g.getMap().getTile(33, 52).getType() == Type.EMPTY);
        g.removeSingles(1);
        int laterLandTiles = 0;
        for (int x = 0; x < g.getMap().getWidth(); x++) {
            for (int y = 0; y < g.getMap().getHeight(); y++) {
                if (g.getMap().getMap()[x][y].getType() == Type.LAND) {
                    laterLandTiles++;
                }
            }
        }

        assertTrue(landTiles != laterLandTiles);
        assertTrue(g.getMap().getTile(45, 48).getType() == Type.LAND);
        assertTrue(g.getMap().getTile(33, 52).getType() == Type.LAND);
    }

    @Test public void removeSinglesTest2() {
        int landTiles = g.landforming();
        assertTrue(g.getMap().getTile(9, 67).getType() == Type.EMPTY);
        assertTrue(g.getMap().getTile(8, 68).getType() == Type.EMPTY);
        assertTrue(g.getMap().getTile(41, 88).getType() == Type.EMPTY);
        assertTrue(g.getMap().getTile(41, 88).getType() == Type.EMPTY);
        g.removeSingles(2);
        int laterLandTiles = 0;
        for (int x = 0; x < g.getMap().getWidth(); x++) {
            for (int y = 0; y < g.getMap().getHeight(); y++) {
                if (g.getMap().getMap()[x][y].getType() == Type.LAND) {
                    laterLandTiles++;
                }
            }
        }

        assertTrue(landTiles != laterLandTiles);
        assertTrue(g.getMap().getTile(9, 67).getType() == Type.LAND);
        assertTrue(g.getMap().getTile(8, 68).getType() == Type.LAND);
        assertTrue(g.getMap().getTile(41, 88).getType() == Type.LAND);
        assertTrue(g.getMap().getTile(41, 88).getType() == Type.LAND);
    }

    @Test public void removeSinglesTest3() {
        int landTiles = g.landforming();
        assertTrue(g.getMap().getTile(17, 31).getType() == Type.EMPTY);
        assertTrue(g.getMap().getTile(41, 88).getType() == Type.EMPTY);
        assertTrue(g.getMap().getTile(41, 88).getType() == Type.EMPTY);
        g.removeSingles(4);
        int laterLandTiles = 0;
        for (int x = 0; x < g.getMap().getWidth(); x++) {
            for (int y = 0; y < g.getMap().getHeight(); y++) {
                if (g.getMap().getMap()[x][y].getType() == Type.LAND) {
                    laterLandTiles++;
                }
            }
        }

        assertTrue(landTiles != laterLandTiles);
        assertTrue(g.getMap().getTile(17, 31).getType() == Type.LAND);
        assertTrue(g.getMap().getTile(41, 88).getType() == Type.LAND);
        assertTrue(g.getMap().getTile(41, 88).getType() == Type.LAND);
    }

    @Test public void removeSinglesTest4() {
        int landTiles = g.landforming();
        assertTrue(g.getMap().getTile(17, 31).getType() == Type.EMPTY);
        assertTrue(g.getMap().getTile(41, 88).getType() == Type.EMPTY);
        assertTrue(g.getMap().getTile(41, 88).getType() == Type.EMPTY);
        assertTrue(g.getMap().getTile(47, 41).getType() == Type.EMPTY);
        g.removeSingles(5);
        int laterLandTiles = 0;
        for (int x = 0; x < g.getMap().getWidth(); x++) {
            for (int y = 0; y < g.getMap().getHeight(); y++) {
                if (g.getMap().getMap()[x][y].getType() == Type.LAND) {
                    laterLandTiles++;
                }
            }
        }

        assertTrue(landTiles != laterLandTiles);
        assertTrue(g.getMap().getTile(17, 31).getType() == Type.LAND);
        assertTrue(g.getMap().getTile(41, 88).getType() == Type.LAND);
        assertTrue(g.getMap().getTile(41, 88).getType() == Type.LAND);
        assertTrue(g.getMap().getTile(47, 41).getType() == Type.LAND);
    }

    @Test public void removeSinglesTest5() {
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        g.processEntities();
        int forestTiles = 0;
        for (int x = 0; x < g.getMap().getWidth(); x++) {
            for (int y = 0; y < g.getMap().getHeight(); y++) {
                if (g.getMap().getMap()[x][y].getType() == Type.LAND) {
                    forestTiles++;
                }
            }
        }
        g.removeSingles(3);
        int laterForestTiles = 0;
        for (int x = 0; x < g.getMap().getWidth(); x++) {
            for (int y = 0; y < g.getMap().getHeight(); y++) {
                if (g.getMap().getMap()[x][y].getType() == Type.LAND) {
                    laterForestTiles++;
                }
            }
        }
        assertTrue(forestTiles != laterForestTiles);
    }

    @Test public void entityAtTest() {
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        boolean[][] processed = new boolean[100][100];
        assertTrue(g.entityAt(33, 4, processed).length == 4);
        assertTrue(g.entityAt(0, 99, processed).length == 176);
        assertTrue(g.entityAt(0, 0, processed).length == 6618);
        assertTrue(g.entityAt(28, 20, processed).length == 1284);
    }

    @Test public void areaConnectedToBorderTest() {
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        for (int i = 1; i <= 10; i++) {
            g.getMap().getMap()[99][i].setType(Type.LAND);
        }
        for (int i = 1; i <= 10; i++) {
            g.getMap().getMap()[50 + i][0].setType(Type.LAND);
        }
        g.getMap().getMap()[75][99].setType(Type.LAND);
        g.getMap().getMap()[76][99].setType(Type.LAND);
        g.getMap().getMap()[0][60].setType(Type.EMPTY);
        boolean[][] processed = new boolean[100][100];
        assertTrue(g.areaConnectedToBorder(g.entityAt(0, 99, processed)));
        assertTrue(g.areaConnectedToBorder(g.entityAt(0, 0, processed)));
        assertTrue(!g.areaConnectedToBorder(g.entityAt(33, 4, processed)));
        assertTrue(g.areaConnectedToBorder(g.entityAt(99, 99, processed)));
        assertTrue(g.areaConnectedToBorder(g.entityAt(99, 1, processed)));
        assertTrue(g.areaConnectedToBorder(g.entityAt(80, 99, processed)));
        assertTrue(g.areaConnectedToBorder(g.entityAt(51, 0, processed)));
        assertTrue(!g.areaConnectedToBorder(g.entityAt(0, 60, processed)));
    }

    @Test public void entitiesTest() {
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        Tile[][] entities = g.entities();
        assertTrue(entities.length == 19);
    }

    @Test public void createForestTest1() {
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        boolean[][] processed = new boolean[100][100];
        int forestTiles = g.createForest(g.entityAt(3, 51, processed));
        assertTrue(forestTiles == 609);
    }

    @Test public void createForestTest2() {
        g.setForestChance(0.05);
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        boolean[][] processed = new boolean[100][100];
        int forestTiles = g.createForest(g.entityAt(3, 51, processed));
        assertTrue(forestTiles == 277);
    }

    @Test public void createForestTest3() {
        g.setForestChance(0.9);
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        rng.nextDouble();
        rng.nextDouble();
        rng.nextDouble();
        boolean[][] processed = new boolean[100][100];
        int forestTiles = g.createForest(g.entityAt(3, 51, processed));
        assertTrue(forestTiles == 941);
    }

    @Test public void createForestTest4() {
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        boolean[][] processed = new boolean[100][100];
        int forestTiles = g.createForest(g.entityAt(60, 47, processed));
        assertTrue(forestTiles == 0);
    }

    @Test public void createForestTest5() {
        g.setForestChance(0.9);
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        boolean[][] processed = new boolean[100][100];
        Tile[] area = g.entityAt(30, 0, processed);
        g.drawBorders(area);
        int forestTiles = g.createForest(area);
        assertTrue(forestTiles == 94);
    }

    @Test public void createForestTest6() {
        g.setForestChance(0);
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        boolean[][] processed = new boolean[100][100];
        Tile[] area = g.entityAt(73, 75, processed);
        for (int i = 0; i < 5; i++) {
            area[i].setType(Type.SEA);
        }
        processed = new boolean[100][100];
        area = g.entityAt(78, 76, processed);
        int forest = g.createForest(area);
        assertTrue(forest == 10);
    }

    @Test public void drawBordersTest1() {
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        boolean[][] processed = new boolean[100][100];
        Tile[] area = g.entityAt(3, 51, processed);
        int borders = g.drawBorders(area);
        assertTrue(borders == 161);
    }
    @Test public void drawBordersTest2() {
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        boolean[][] processed = new boolean[100][100];
        Tile[] area = g.entityAt(30, 40, processed);
        for (Tile tile: area) {
            tile.setType(Type.LAKE);
        }
        int borders = g.drawBorders(area);
        assertTrue(borders == 68);
    }

    @Test public void drawBordersTest3() {
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        boolean[][] processed = new boolean[100][100];
        Tile[] area = g.entityAt(0, 0, processed);
        int borders = g.drawBorders(area);
        assertTrue(borders == 0);
    }

    @Test public void drawBordersTest4() {
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        boolean[][] processed = new boolean[100][100];
        Tile[] area = g.entityAt(0, 99, processed);
        for (Tile tile: area) {
            tile.setType(Type.LAKE);
        }
        int borders = g.drawBorders(area);
        assertTrue(borders == 52);
    }

    @Test public void processEntitiesTest1() {
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        g.processEntities();
        boolean[][] processed = new boolean[100][100];
        Tile[] area = g.entityAt(0, 0, processed);
        assertTrue(area.length == 6620);
    }

    @Test public void processEntitiesTest2() {
        g.setChanceToDiscardSmall(0.9);
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        g.processEntities();
        boolean[][] processed = new boolean[100][100];
        Tile[] area = g.entityAt(0, 0, processed);
        assertTrue(area.length == 6668);
    }

    @Test public void processEntitiesTest3() {
        g.setChanceToDiscardSmall(0);
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        g.processEntities();
        boolean[][] processed = new boolean[100][100];
        Tile[] area = g.entityAt(0, 0, processed);
        assertTrue(area.length == 6620);
    }

    @Test public void processEntitiesTest4() {
        g.setForestChance(0.3);
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        g.processEntities();
        boolean[][] processed = new boolean[100][100];
        Tile[] area = g.entityAt(63, 63, processed);
        assertTrue(area.length == 36);
    }

    @Test public void processEntitiesTest5() {
        g.newMap(500, 500);
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        g.processEntities();
        boolean[][] processed = new boolean[500][500];
        Tile[] area = g.entityAt(0, 130, processed);
        assertTrue(area[0].getType() == Type.LAND);
        assertTrue(area.length == 56144);
    }

    @Test public void processEntitiesTest6() {
        g.setForestChance(0.45);
        g.newMap(600, 600);
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        g.processEntities();
        Tile[][] areas = g.entities();
        int forests = 0;
        for (Tile[] area: areas) {
            if (area[0].getType() == Type.FOREST) {
                forests++;
            }
        }
        assertTrue(forests == 9);
    }

    @Test public void directlyAdjacentTilesTest1() {
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        g.processEntities();
        Tile[] tiles = g.directlyAdjacentTiles(50, 50);
        assertTrue(tiles[0].getType() == Type.FOREST);
        assertTrue(tiles[1].getType() == Type.LAND);
        assertTrue(tiles[2].getType() == Type.FOREST);
        assertTrue(tiles[3].getType() == Type.FOREST);
    }

    @Test public void directlyAdjacentTilesTest2() {
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        g.processEntities();
        Tile[] tiles = g.directlyAdjacentTiles(0, 0);
        assertTrue(tiles[0] == null);
        assertTrue(tiles[1] == null);
        assertTrue(tiles[2].getType() == Type.SEA);
        assertTrue(tiles[3].getType() == Type.SEA);
    }

    @Test public void directlyAdjacentTilesTest3() {
        g.landforming();
        g.removeSingles(5);
        g.removeSingles(2);
        g.fillInSea();
        g.processEntities();
        Tile[] tiles = g.directlyAdjacentTiles(99, 99);
        assertTrue(tiles[0].getType() == Type.LAND);
        assertTrue(tiles[1].getType() == Type.LAND);
        assertTrue(tiles[2] == null);
        assertTrue(tiles[3] == null);
    }

    @Test public void initMapTest() {
        g.initMap();
        int seaTiles = 0;
        for (int x = 0; x < g.getMap().getWidth(); x++) {
            for (int y = 0; y < g.getMap().getHeight(); y++) {
                if (g.getMap().getMap()[x][y].getType() == Type.SEA) {
                    seaTiles++;
                }
            }
        }
        assertTrue(seaTiles == 6832);
    }

}
