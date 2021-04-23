package karttageneraattori.Logic;

public class TileQueue {
    private Tile[] list;
    private int head;
    private int tail;
    private int size;
    private int cap;

    public TileQueue(int q_size) {
        cap = q_size;
        list = new Tile[cap];
    }

    public TileQueue(TileQueue tq) {
        list = tq.getList();
        cap = list.length;
    }

    public int getSize() {
        return size;
    }

    public Tile[] getList() {
        return list;
    }

    public void push(Tile t) {
        if (isFull()) {
            return;
        } else {
            
            if (tail == cap) {
                tail = 0;
            }
            list[tail] = t;
            size++;
            tail++;
        }
    }

    public Tile pop() {
        Tile first = null;
        if (isEmpty()) {
            return null;
        }
        if (head == cap - 1) {
            first = list[head];
            list[head] = null;
            head = -1;
            size--;
        } else {
            first = list[head];
            size--;
            list[head] = null;
        }
        head++;
        return first;
    }

    public boolean isFull() {
        if (size == cap) {
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }
}
