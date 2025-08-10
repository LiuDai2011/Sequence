package sequence.core

import arc.Events
import arc.func.Cons
import arc.struct.IntMap
import arc.struct.ObjectMap
import sequence.util.*

object SqEvents {
    val events = ObjectMap<Any, IntMap<Cons<Any>>>()
    private var _uuid = 0
    val uuid get() = _uuid++

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> on(type: Class<T>, listener: Cons<T>): Int {
        addTrigger(type)
        val id = uuid
        events[type, { IntMap() }][id] = listener as Cons<Any>
        return id
    }

    fun remove(id: Int) {
        for ((_, v) in events) {
            if (v.containsKey(id)) {
                v.remove(id)
                break
            }
        }
    }

    fun <T : Any> fire(e: T) = fire(e::class.java, e)

    fun <T : Any> fire(type: Class<*>, e: T) {
        val listeners = events[type, IntMap()]

        for ((_, v) in listeners) {
            v.get(e)
        }
    }

    private val added = ObjectMap<Any, Boolean>()
    private fun <T : Any> addTrigger(type: Class<T>) {
        if (!added[type, false]) {
            added[type] = true
            Events.on(type) { fire(it::class.java, it) }
            if (!events.containsKey(type)) events[type] = IntMap()
        }
    }
}
