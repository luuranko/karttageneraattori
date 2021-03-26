package karttageneraattori;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import karttageneraattori.Logic.Map;
import karttageneraattori.Logic.TileType;



public class MapTest {
    @Test public void testConstructor1() {
        Map m = new Map();
        assertTrue(m.getWidth() == 100);
        assertTrue(m.getHeight() == 100);
    }

    @Test public void testConstructor2() {
        Map m = new Map(55);
        assertTrue(m.getWidth() == 55);
        assertTrue(m.getHeight() == 55);
    }

    @Test public void testConstructor3() {
        Map m = new Map(55, 70);
        assertTrue(m.getWidth() == 55);
        assertTrue(m.getHeight() == 70);
    }

    @Test public void testInit() {
        Map m = new Map(10, 15);
        assertTrue(m.getMap()[0][0].getType() == TileType.EMPTY);
        assertTrue(m.getMap()[9][14].getType() == TileType.EMPTY);
    }

    @Test public void testInsert() {
        Map m = new Map(10, 15);
        m.insert(TileType.SEA, 6, 8);
        assertTrue(m.getMap()[6][8].getType() == TileType.SEA);
    }
}
