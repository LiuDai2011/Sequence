package sequence.util;

import arc.func.Prov;
import arc.math.geom.Vec2;
import arc.struct.ObjectMap;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;

public class Util {
    public static <K, V> V checkKey(ObjectMap<K, V> map, K key, Prov<V> def) {
        if (!map.containsKey(key)) map.put(key, def.get());
        return map.get(key);
    }

    public static int item(ItemStack[] items, Item item) {
        for (ItemStack stack : items) {
            if (stack.item == item)
                return stack.amount;
        }
        return 0;
    }

    public static float liquid(LiquidStack[] liquids, Liquid liquid) {
        for (LiquidStack stack : liquids) {
            if (stack.liquid == liquid)
                return stack.amount;
        }
        return 0;
    }

    public static boolean inZone(Vec2 start, Vec2 size, Vec2 point) {
        return inZone(start.x, start.y, start.x + size.x, start.y + size.y, point.x, point.y);
    }

    public static <T extends Comparable<T>> boolean inZone(T x, T y, T x1, T y1, T px, T py) {
        return x.compareTo(px) < 0 && y.compareTo(py) < 0 && x1.compareTo(px) > 0 && y1.compareTo(py) > 0;
    }
}
