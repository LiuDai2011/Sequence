package sequence.core

import arc.Events
import arc.func.Cons
import arc.struct.IntMap
import arc.struct.ObjectMap
import arc.struct.Seq
import sequence.util.set

object SqEvents {
    val events = ObjectMap<Any, IntMap<Cons<in Any>>>()
    private val added = ObjectMap<Any, Boolean>()

    @Suppress("UNCHECKED_CAST")
    fun addTrigger(type: Any) {
        if (added.findKey(type, false) == null) {
            added[type] = true
            Events::class.java.getField("events").apply {
                (get(null)!! as ObjectMap<Any, Seq<Cons<*>>>)[type, { Seq<Cons<*>>(Cons::class.java) }].add {
                    fire(it)
                }
            }
        }
    }

    fun <T : Any> fire(e: T) = fire(e::class.java, e)

    fun <T : Any> fire(type: Any, e: T) {
        if (events[e] == null) events[e] = IntMap()
        for (entry in events[type]) {
            entry.value.get(e)
        }
    }
}
