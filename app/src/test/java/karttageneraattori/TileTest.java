package karttageneraattori;

import org.junit.Test;

import karttageneraattori.Logic.Tile;
import karttageneraattori.Logic.TileType;

import static org.junit.Assert.assertTrue;

public class TileTest {

    @Test public void testSetType() {
        Tile t = new Tile();
        t.setType(TileType.SEA);
        assertTrue(t.getType() == TileType.SEA);
    }
    
    @Test public void testConstructor1() {
        Tile t = new Tile();
        assertTrue(t.getType() == TileType.EMPTY);
    }

    @Test public void testConstructor2() {
        Tile t = new Tile(TileType.SEA);
        assertTrue(t.getType() == TileType.SEA);
    }
}
