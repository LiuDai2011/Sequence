package Sequence.world.util.struct;

import arc.func.Prov;
import arc.struct.ObjectMap;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class ObjectTable<T1, T2> implements Iterable<ObjectTable.Entry<T1, T2>> {
    protected final ObjectMap<T1, T2> map12 = new ObjectMap<>();
    protected final ObjectMap<T2, T1> map21 = new ObjectMap<>();

    public int size() {
        return map12.size;
    }

    public T1 getKey(T2 key) {
        return map21.get(key);
    }

    public T1 getKey(T2 key, Prov<Entry<T1, T2>> supplier) {
        T1 res = getKey(key);
        if (res == null) {
            put(supplier.get());
            return getKey(key);
        }
        return res;
    }

    public T2 getValue(T1 key) {
        return map12.get(key);
    }

    public T2 getValue(T1 key, Prov<Entry<T1, T2>> supplier) {
        T2 res = getValue(key);
        if (res == null) {
            put(supplier.get());
            return getValue(key);
        }
        return res;
    }

    public void put(T1 a1, T2 a2) {
        if (map12.containsKey(a1)) return;
        map12.put(a1, a2);
        map21.put(a2, a1);
    }

    public void put(Entry<T1, T2> entry) {
        put(entry.a1, entry.a2);
    }

    public void removeByKey(T1 arg) {
        if (!map12.containsKey(arg)) return;
        map12.remove(arg);
        map21.remove(getValue(arg));
    }

    public void removeByValue(T2 arg) {
        if (!map21.containsKey(arg)) return;
        map21.remove(arg);
        map12.remove(getKey(arg));
    }

    @SuppressWarnings("unchecked")
    public Object get(Object key, Prov<Entry<T1, T2>> supplier, boolean argT1) {
        return argT1 ? getValue((T1) key, supplier) : getKey((T2) key, supplier);
    }

    public boolean containsKey(T1 arg) {
        return map12.containsKey(arg);
    }

    public boolean containsValue(T2 arg) {
        return map21.containsKey(arg);
    }

    @NotNull
    @Override
    public Iterator<Entry<T1, T2>> iterator() {
        return new TableIterator<>(map12);
    }

    @Override
    public String toString() {
        return "ObjectTable{" + map12 +
                ", size=" + size() +
                '}';
    }

    public void clear() {
        map12.clear();
        map21.clear();
    }

    public static class Entry<T1, T2> {
        public final T1 a1;
        public final T2 a2;

        public Entry(T1 a1, T2 a2) {
            this.a1 = a1;
            this.a2 = a2;
        }
    }

    public static class TableIterator<T1, T2> implements Iterator<Entry<T1, T2>> {
        protected final ObjectMap.Entries<T1, T2> iterator;

        public TableIterator(ObjectMap<T1, T2> map) {
            iterator = map.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext;
        }

        @Override
        public Entry<T1, T2> next() {
            ObjectMap.Entry<T1, T2> next = iterator.next();
            return new Entry<>(next.key, next.value);
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }
}
