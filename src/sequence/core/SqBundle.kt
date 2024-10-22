package sequence.core

import arc.Core
import sequence.SeqMod

object SqBundle {
    private val builder = StringBuilder()
    fun format(key: String, vararg args: Any?): String {
        return Core.bundle.format(key, *args)
    }

    operator fun get(key: String, def: String): String = Core.bundle[key, def]

    operator fun invoke(key: String, def: String): String = get(key, def)

    operator fun get(key: String): String = Core.bundle[key]

    operator fun invoke(key: String): String = get(key)

    fun cat(vararg args: String): String {
        builder.setLength(0)
        for (s in args) {
            builder.append(s).append('.')
        }
        builder.deleteCharAt(builder.length - 1)
        return builder.toString()
    }

    fun mod(arg: String): String {
        return SqBundle[SeqMod.MOD.meta.name + "." + arg]
    }

    fun catGet(vararg args: String): String {
        return SqBundle[cat(*args)]
    }
}
