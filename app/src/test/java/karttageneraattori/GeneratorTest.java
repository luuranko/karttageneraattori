package karttageneraattori;

import org.junit.Before;
import org.junit.Test;

import karttageneraattori.Logic.Generator;
import karttageneraattori.Logic.Type;

import static org.junit.Assert.assertTrue;

import java.util.Random;

public class GeneratorTest {

    private Random rng;
    private Generator g;
    
    @Before public void init() {
        // this.rng = new Random(4266642);
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

    @Test public void testFillInSea() {
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

}
