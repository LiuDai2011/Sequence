package sequence.util

import arc.func.*
import arc.math.Interp
import arc.math.Mathf
import arc.math.geom.Point2
import arc.struct.IntMap
import arc.struct.ObjectFloatMap
import arc.struct.ObjectIntMap
import arc.struct.ObjectMap
import mindustry.content.Fx
import mindustry.entities.bullet.BulletType
import mindustry.world.Tiles
import kotlin.reflect.KClass

infix fun KClass<*>.eq(other: KClass<*>): Boolean {
    if (javaObjectType.name.contains("$") ||
        other.javaObjectType.name.contains("$")
    )
        return javaObjectType.isNestmateOf(other.javaObjectType) ||
                other.javaObjectType.isAssignableFrom(javaObjectType)
    return javaObjectType.name.split("$")[0] == other.javaObjectType.name.split("$")[0]
}

infix fun Any.classEq(other: Any) = this::class eq other::class
infix fun Any.classEq(other: KClass<*>) = this::class eq other

inline fun <reified T : SignBase> Any.hasSign() = this is T

operator fun Interp.invoke(a: Float) = apply(a)

operator fun <K> ObjectIntMap<K>.set(key: K, value: Int) = put(key, value)
operator fun <K> ObjectFloatMap<K>.set(key: K, value: Float) = put(key, value)
operator fun <V> IntMap<V>.set(key: Int, value: V): V = put(key, value)
operator fun <K, V> ObjectMap<K, V>.set(key: K, value: V): V = put(key, value)

operator fun <P> Cons<P>.invoke(p: P) = get(p)
operator fun <P1, P2> Cons2<P1, P2>.invoke(p1: P1, p2: P2) = get(p1, p2)
operator fun <P1, P2, P3> Cons3<P1, P2, P3>.invoke(p1: P1, p2: P2, p3: P3) = get(p1, p2, p3)
operator fun <P1, P2, P3, P4> Cons4<P1, P2, P3, P4>.invoke(p1: P1, p2: P2, p3: P3, p4: P4) = get(p1, p2, p3, p4)
operator fun <P, R> Func<P, R>.invoke(p: P): R = get(p)
operator fun <P1, P2, R> Func2<P1, P2, R>.invoke(p1: P1, p2: P2): R = get(p1, p2)
operator fun <P1, P2, P3, R> Func3<P1, P2, P3, R>.invoke(p1: P1, p2: P2, p3: P3): R = get(p1, p2, p3)
operator fun <R> Prov<R>.invoke(): R = get()
operator fun <P, T : Throwable> ConsT<P, T>.invoke(p: P) = get(p)

operator fun Point2.component1(): Int = x
operator fun Point2.component2(): Int = y

operator fun <V> IntMap.Entry<V>.component1(): Int = key
operator fun <V> IntMap.Entry<V>.component2(): V = value

operator fun Tiles.get(idx: Int) = getp(idx)

fun Float.notZero() = !Mathf.zero(this)

fun BulletType.clearEffects() {
    despawnEffect = Fx.none
    hitEffect = Fx.none
    trailEffect = Fx.none
    shootEffect = Fx.none
    smokeEffect = Fx.none
    chargeEffect = Fx.none
    healEffect = Fx.none
}

typealias MUnit = mindustry.gen.Unit
