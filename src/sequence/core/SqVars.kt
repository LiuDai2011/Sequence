package sequence.core

import arc.Events
import arc.files.Fi
import arc.func.Cons
import arc.math.Interp
import arc.struct.IntMap
import arc.struct.Seq
import mindustry.Vars
import mindustry.game.EventType

object SqVars {
    //    val sha3_512 = MessageDigest.getInstance("SHA3-512")
    val pow7: Interp = Interp.Pow(7)
    val emptyObjArr = arrayOf<Any>()
    const val revTimerUnit = 20f
    val processors = IntMap<Seq<Processor>>()
    private const val MIN_PRIORITY = Int.MAX_VALUE
    val unzipDirectory: Fi by lazy { Vars.dataDirectory.child("unzip/") }

    init {
        Events.run(EventType.Trigger.update) { allProcessors { it.run() } }
    }

    @JvmOverloads
    fun addProcessor(runnable: Runnable, priority: Int = MIN_PRIORITY) {
        val processor = Processor(runnable, priority)
        processors[priority, { Seq() }].add(processor)
    }

    fun allProcessors(action: Cons<Processor>) {
        for (entry in processors) {
            for (p in entry.value) {
                action[p]
            }
        }
    }

    class Processor @JvmOverloads constructor(var runnable: Runnable, var priority: Int = 0) : Comparable<Processor> {
        fun run() {
            runnable.run()
        }

        override fun compareTo(other: Processor): Int {
            return priority.compareTo(other.priority)
        }
    }
}
