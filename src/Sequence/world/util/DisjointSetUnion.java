package Sequence.world.util;

/**
 * 并查集awa
 * @author LiuDai
 */
public class DisjointSetUnion {
    public final int size;
    protected final int[] father;
    protected final int[] _size;
    public DisjointSetUnion(int size) {
        this.size = size;
        father = new int[size];
        _size = new int[size];
        for (int i = 0; i < size; ++i) {
            father[i] = i;
            _size[i] = 1;
        }
    }
    public int find(int id) {
        return father[id] == id ? id : (father[id] = find(father[id]));
    }
    public void unite(int a, int b) {
        int x = find(a), y = find(b);
        if (a == b) return;
        if (_size[x] < _size[y]) {
            int tmp = x;
            x = y;
            y = tmp;
        }
        father[y] = x;
        _size[x] += _size[y];
    }
    public int sizeOf(int id) {
        return _size[id];
    }
    public boolean erase(int id) {
        if (_size[id] > 1) return false;
        _size[find(id)]--;
        father[id] = id;
        return true;
    }
    public void move(int a, int b) {
        int x = find(a), y = find(b);
        if (x == y) return;
        father[a] = y;
        _size[x]--;
        _size[y]++;
    }
    public boolean alone(int id) {
        return find(id) == id;
    }
}
