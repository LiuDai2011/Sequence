package sequence.util

import arc.func.Prov
import arc.math.geom.Vec2
import arc.struct.ObjectMap
import mindustry.type.Item
import mindustry.type.ItemStack
import mindustry.type.Liquid
import mindustry.type.LiquidStack

object Util {
    fun <K, V> checkKey(map: ObjectMap<K, V>, key: K, def: Prov<V>): V {
        if (!map.containsKey(key)) map.put(key, def.get())
        return map[key]
    }

    fun item(items: Array<ItemStack>, item: Item): Int {
        for (stack in items) {
            if (stack.item === item) return stack.amount
        }
        return 0
    }

    fun liquid(liquids: Array<LiquidStack>, liquid: Liquid): Float {
        for (stack in liquids) {
            if (stack.liquid === liquid) return stack.amount
        }
        return 0f
    }

    fun inZone(start: Vec2, size: Vec2, point: Vec2): Boolean {
        return inZone(start.x, start.y, start.x + size.x, start.y + size.y, point.x, point.y)
    }

    fun <T : Comparable<T>?> inZone(x: T, y: T, x1: T, y1: T, px: T, py: T): Boolean {
        return x!! < px && y!! < py && x1!! > px && y1!! > py
    }
}
