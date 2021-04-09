package karttageneraattori;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import karttageneraattori.Logic.Map;

public class MapTest {
    @Test public void testConstructor1() {
        Map m = new Map();
        assertTrue(m.getWidth() == 100);
        assertTrue(m.getHeight() == 100);
    }

    @Test public void testConstructor2() {
        Map m = new Map(10);
        assertTrue(m.getWidth() == 10);
        assertTrue(m.getHeight() == 10);
    }

    @Test public void testConstructor3() {
        Map m = new Map(5, 7);
        assertTrue(m.getWidth() == 5);
        assertTrue(m.getHeight() == 7);
    }

    @Test public void testRecycleConstructor1() {
        Map m1 = new Map(10);
        Map m2 = new Map(m1, 15, 15);
        assertTrue(m2.getWidth() == 15 && m2.getHeight() == 15);
    }

    @Test public void testRecycleConstructor2() {
        Map m1 = new Map(10, 15);
        Map m2 = new Map(m1, 15, 15);
        assertTrue(m2.getWidth() == 15 && m2.getHeight() == 15);
    }

    @Test public void testRecycleConstructor3() {
        Map m1 = new Map(16, 15);
        Map m2 = new Map(m1, 15, 15);
        assertTrue(m2.getWidth() == 15 && m2.getHeight() == 15);
    }

    @Test public void testRecycleConstructor4() {
        Map m1 = new Map(16, 16);
        Map m2 = new Map(m1, 15, 15);
        assertTrue(m2.getWidth() == 15 && m2.getHeight() == 15);
    }
}
