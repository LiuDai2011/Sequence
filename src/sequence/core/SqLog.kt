package sequence.core

import arc.util.Log

object SqLog {
    const val modPrefix = "[seq] "
    fun info(text: String, vararg args: Any?) = Log.info(modPrefix + text, *args)
    fun info(obj: Any) = info(obj.toString(), *SqVars.emptyObjArr)
    fun err(text: String, vararg args: Any?) = Log.err(modPrefix + text, *args)
    fun err(obj: Any) = info(obj.toString(), *SqVars.emptyObjArr)
    fun warn(text: String, vararg args: Any?) = Log.warn(modPrefix + text, *args)
    fun warn(obj: Any) = info(obj.toString(), *SqVars.emptyObjArr)
    fun debug(text: String, vararg args: Any?) = Log.debug(modPrefix + text, *args)
    fun debug(obj: Any) = debug(obj.toString(), *SqVars.emptyObjArr)
}
