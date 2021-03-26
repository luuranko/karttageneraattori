package karttageneraattori;

import org.junit.Before;
import org.junit.Test;

import karttageneraattori.Logic.Generator;
import karttageneraattori.Logic.Map;
import karttageneraattori.Logic.TileType;

import static org.junit.Assert.assertTrue;

import java.util.Random;

public class GeneratorTest {

    private Random rng;
    private Generator g;
    
    @Before public void init() {
        this.rng = new Random(4266642);
        this.g = new Generator(2, rng);
    }

    @Test public void testDetermineTile1() {
        Map m = new Map(50);
        TileType tt = g.determineTile(m, 0, 0);
        assertTrue(tt == TileType.SEA);
    }

    @Test public void testInitMap1() {
        Map m = new Map(50);
        g.initMap(m);
        boolean bordersAreSea = true;
        for (int x = 0; x < m.getWidth(); x++) {
            if (m.getMap()[x][0].getType() != TileType.SEA) {
                bordersAreSea = false;
            }
            if (m.getMap()[x][m.getHeight() - 1].getType() != TileType.SEA) {
                bordersAreSea = false;
            }
        }
        for (int y = 0; y < m.getHeight(); y++) {
            if (m.getMap()[0][y].getType() != TileType.SEA) {
                bordersAreSea = false;
            }
            if (m.getMap()[m.getWidth() - 1][y].getType() != TileType.SEA) {
                bordersAreSea = false;
            }
        }
        assertTrue(bordersAreSea);
    }

}
