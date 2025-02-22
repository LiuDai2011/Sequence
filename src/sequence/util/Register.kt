package sequence.util

import arc.func.Prov
import arc.struct.ObjectMap
import mindustry.Vars
import mindustry.gen.EntityMapping
import mindustry.gen.Entityc
import mindustry.type.UnitType
import sequence.SeqMod.Companion.dev
import sequence.core.SqLog

inline fun <reified T> T.register(builder: T.() -> Unit): T {
    builder(this)
    return this
}

fun <T : UnitType, R : Entityc> T.build(constructor: Prov<R>) {
    EntityMapping.nameMap.put(name, constructor)
    EntityRegister.put(constructor.get().javaClass, constructor)
}

object EntityRegister {
    val needIdClasses = ObjectMap<Class<*>, ProvSet>()
    private val classIdMap = ObjectMap<Class<*>, Int>()

    fun <T : Entityc> put(c: Class<T>, p: ProvSet) {
        needIdClasses.put(c, p)
    }

    fun <T : Entityc> put(c: Class<T>, prov: Prov<T>) {
        put(c, ProvSet(prov))
    }

    fun <T : Entityc> getID(c: Class<T>): Int = classIdMap[c]

    fun load() {
        val key = needIdClasses.keys().toSeq().sortComparing { it.toString().hashCode() }
        for (c in key) classIdMap.put(c, EntityMapping.register(c.toString(), needIdClasses[c].prov))
        if (dev || Vars.headless) {
            SqLog.info("//=============================================\\\\")
            classIdMap.each { c, i ->
                SqLog.info(i.toString() + "|" + c.simpleName)
            }
            SqLog.info("\\\\=============================================//")
        }
    }

    data class ProvSet(val prov: Prov<*>, val name: String = prov.get().javaClass.toString())
}
