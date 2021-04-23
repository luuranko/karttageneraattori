package karttageneraattori;


import org.junit.Test;

import karttageneraattori.Logic.Tile;
import karttageneraattori.Logic.TileQueue;

import static org.junit.Assert.assertTrue;

public class TileQueueTest {
    @Test public void testConstructor() {
        TileQueue tq = new TileQueue(3);
        assertTrue(tq.getSize() == 0 && tq.getCap() == 3);
    }

    @Test public void testPush() {
        TileQueue tq = new TileQueue(3);
        tq.push(new Tile(0, 0));
        assertTrue(tq.getSize() == 1);
    }

    @Test public void testPop() {
        TileQueue tq = new TileQueue(3);
        tq.push(new Tile(1, 1));
        assertTrue(tq.pop().getX() == 1);
    }

    @Test public void testPushPop() {
        TileQueue tq = new TileQueue(3);
        tq.push(new Tile(1, 1));
        tq.push(new Tile(2, 2));
        tq.push(new Tile(3, 3));
        tq.pop();
        assertTrue(tq.pop().getX() == 2);
    }

    @Test public void testIsEmpty() {
        TileQueue tq = new TileQueue(3);
        assertTrue(tq.isEmpty());
    }

    @Test public void testIsFull() {
        TileQueue tq = new TileQueue(3);
        tq.push(new Tile(1, 1));
        tq.push(new Tile(2, 2));
        tq.push(new Tile(3, 3));
        assertTrue(tq.isFull());
    }

    @Test public void testPush2() {
        TileQueue tq = new TileQueue(3);
        tq.push(new Tile(1, 1));
        tq.push(new Tile(2, 2));
        tq.push(new Tile(3, 3));
        assertTrue(tq.push(new Tile(4, 4)) == false);
    }

    @Test public void testPushPop2() {
        TileQueue tq = new TileQueue(3);
        tq.push(new Tile(3, 3));
        tq.pop();
        assertTrue(tq.pop() == null);
    }

    @Test public void testPushPop3() {
        TileQueue tq = new TileQueue(3);
        tq.push(new Tile(1, 1));
        tq.push(new Tile(2, 2));
        tq.push(new Tile(3, 3));
        tq.pop();
        tq.push(new Tile(4, 4));
        assertTrue(tq.pop().getX() == 2);
    }

    @Test public void testPushPop4() {
        TileQueue tq = new TileQueue(3);
        tq.push(new Tile(1, 1));
        tq.push(new Tile(2, 2));
        tq.push(new Tile(3, 3));
        tq.pop();
        tq.push(new Tile(4, 4));
        tq.pop();
        tq.pop();
        assertTrue(tq.pop().getX() == 4);
    }
}
