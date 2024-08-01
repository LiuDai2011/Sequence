package Sequence.world.util;

import arc.func.Prov;
import arc.struct.ObjectMap;

public class Util {
    public static <K, V> void checkKey(ObjectMap<K, V> map, K key, Prov<V> def) {
        if (!map.containsKey(key)) map.put(key, def.get());
    }
}
