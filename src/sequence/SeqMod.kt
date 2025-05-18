@file:JvmName("SeqMod")

package sequence

import arc.util.Log
import mindustry.Vars
import mindustry.mod.Mod
import mindustry.mod.Mods.LoadedMod
import sequence.content.SqContent
import sequence.core.SqBundle
import sequence.core.SqLog
import sequence.graphic.SqColor

class SeqMod : Mod() {
    init {
        SqLog.info("Loaded SeqMod constructor.")
        if (dev) {
            Log.level = Log.LogLevel.debug
            SqLog.warn("In DEBUG mode!")
        }
    }

    private fun loadMeta() {
        MOD = Vars.mods.getMod(javaClass)
        MOD.apply {
            meta.version = SqBundle.mod("meta.version")
            meta.author = SqBundle.modFormat("meta.author", SqColor.NedKelly, SqColor.LiuDai)
            meta.displayName = SqBundle.mod("meta.display-name")
            meta.description = SqBundle.mod("meta.description")
            repo = Companion.repo
        }
    }

    override fun loadContent() {
        SqLog.info("Loading seq content.")
        loadMeta()
        SqContent.loadContent()
    }

    companion object {
        const val dev = true
        const val skipChk = false
        const val repo = "LiuDai2011/Sequence"
        lateinit var MOD: LoadedMod
        val MOD_PREFIX: String by lazy { "${MOD.meta.name}-" }
        val MOD_PREFIX_POINT: String by lazy { "${MOD.meta.name}." }

        fun name(s: String) = MOD_PREFIX + s
    }
}
