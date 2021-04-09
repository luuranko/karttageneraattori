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
        Tile t = new Tile(0, 0);
        assertTrue(t.getType() == Type.EMPTY && t.getX() == 0 && t.getY() == 0);
    }
}
