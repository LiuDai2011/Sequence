package sequence.core

import arc.Core
import sequence.SeqMod

object SqBundle {
    private val builder = StringBuilder()
    fun format(key: String, vararg args: Any?): String {
        return Core.bundle.format(key, *args)
    }

    operator fun get(key: String, def: String = "???$key???"): String = Core.bundle[key, def]

    operator fun invoke(key: String, def: String = "???${SeqMod.MOD_PREFIX}$key???"): String = get(key, def)

    fun cat(vararg args: String): String {
        builder.setLength(0)
        for (s in args) {
            builder.append(s).append('.')
        }
        builder.deleteCharAt(builder.length - 1)
        return builder.toString()
    }

    fun modCat(vararg args: String): String {
        return SqBundle[SeqMod.MOD!!.meta.name + "." + cat(*args)]
    }

    fun catGet(vararg args: String): String {
        return SqBundle[cat(*args)]
    }
}
