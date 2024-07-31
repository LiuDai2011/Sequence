package Sequence;

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

    public V getValue() {
        return value;
    }

    public void setKey(K key1) {
        key = key1;
    }

    public void setValue(V value1) {
        value = value1;
    }
}
