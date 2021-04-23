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
        this.rng = new Random();
        this.g = new Generator(rng);
    }

    @Test public void testConstructor1() {
        g = new Generator(rng);
        assertTrue(g.getMap().getHeight() == 100
            && g.getMap().getWidth() == 100);
        assertTrue(g.getIslandNum() == 2 && g.getLandToSeaRatio() == 0.3);
    }

    @Test public void testConstructor2() {
        g = new Generator(1, 0.2, rng);
        assertTrue(g.getMap().getHeight() == 100
            && g.getMap().getWidth() == 100);
        assertTrue(g.getIslandNum() == 1 && g.getLandToSeaRatio() == 0.2);
    }

    @Test public void testNewValues() {
        g.newValues();
        assertTrue(g.getIslandNum() >= 1 && g.getIslandNum() <= 3);
        assertTrue(g.getLandToSeaRatio() >= 0.2
            && g.getLandToSeaRatio() <= 0.5);
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

    @Test public void testAreaConnectedToBorder1() {
        Tile[] area = {g.getMap().getMap()[0][1]};
        assertTrue(g.areaConnectedToBorder(area));
    }

    @Test public void testAreaConnectedToBorder2() {
        Tile[] area = {g.getMap().getMap()[1][0]};
        assertTrue(g.areaConnectedToBorder(area));
    }

    @Test public void testAreaConnectedToBorder3() {
        Tile[] area = {g.getMap().getMap()[g.getMap().getWidth() - 1][1]};
        assertTrue(g.areaConnectedToBorder(area));
    }

    @Test public void testAreaConnectedToBorder4() {
        Tile[] area = {g.getMap().getMap()[1][g.getMap().getHeight() - 1]};
        assertTrue(g.areaConnectedToBorder(area));
    }

}
