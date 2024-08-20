package Sequence.world.util.struct;

public class IntTable<T> extends ObjectTable<Integer, T> {
    @Override
    public String toString() {
        return "IntTable{" + map12 +
                ", size=" + size() +
                '}';
    }
}
