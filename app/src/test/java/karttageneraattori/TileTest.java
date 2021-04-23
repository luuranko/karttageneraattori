package karttageneraattori;

import org.junit.Test;

import karttageneraattori.Logic.Tile;
import karttageneraattori.Logic.Type;

import static org.junit.Assert.assertTrue;

public class TileTest {

    @Test public void testSetType() {
        Tile t = new Tile(0, 0);
        t.setType(Type.SEA);
        assertTrue(t.getType() == Type.SEA);
    }
    
    @Test public void testConstructor() {
        Tile t = new Tile(1, 1);
        assertTrue(t.getType() == Type.EMPTY && t.getX() == 1 && t.getY() == 1);
    }

    @Test public void testToString() {
        Tile t = new Tile(1, 1);
        assertTrue(t.toString().equals("(1, 1): EMPTY"));
    }

    @Test public void testCompareTo() {
        Tile t1 = new Tile(1, 1);
        Tile t2 = new Tile(2, 2);
        Tile t3 = new Tile(1, 2);
        Tile t4 = new Tile(2, 1);
        assertTrue(t3.compareTo(t2) == -1);
        assertTrue(t4.compareTo(t2) == -1);
        assertTrue(t3.compareTo(t1) == 1);
        assertTrue(t2.compareTo(t3) == 1);
        assertTrue(t1.compareTo(t1) == 0);
    }
}
