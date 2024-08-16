package Sequence.world.util;

public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public Pair() {
        this(null, null);
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key1) {
        key = key1;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value1) {
        value = value1;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
