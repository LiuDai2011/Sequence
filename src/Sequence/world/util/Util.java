package Sequence.world.util;

import arc.func.Prov;
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
}
