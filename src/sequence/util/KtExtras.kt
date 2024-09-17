package sequence.util

import kotlin.reflect.KClass

infix fun KClass<*>.eq(other: KClass<*>): Boolean {
    println("${javaObjectType.name}, ${other.javaObjectType.name}")
    if (javaObjectType.name.contains("$") ||
        other.javaObjectType.name.contains("$"))
        return javaObjectType.isNestmateOf(other.javaObjectType) ||
                other.javaObjectType.isAssignableFrom(javaObjectType)
    return javaObjectType.name.split("$")[0] == other.javaObjectType.name.split("$")[0]
}
infix fun Any.classEq(other: Any) = this::class eq other::class
infix fun Any.classEq(other: KClass<*>) = this::class eq other
