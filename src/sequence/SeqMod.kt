package sequence

import sequence.content.SqContent
import sequence.core.SqBundle
import sequence.core.SqLog
import arc.util.Log
import mindustry.Vars
import mindustry.content.Blocks
import mindustry.mod.Mod
import mindustry.mod.Mods.LoadedMod
import mindustry.world.blocks.defense.turrets.ItemTurret
import sequence.util.classEq

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
        MOD?.apply {
            meta.version = SqBundle.modCat("meta", "version")
            meta.author = SqBundle.modCat("meta", "author")
            meta.displayName = SqBundle.modCat("meta", "display-name")
            meta.description = SqBundle.modCat("meta", "description")
            repo = Companion.repo
            MOD_PREFIX = "${meta.name}-"
        }
    }

    override fun loadContent() {
        SqLog.info("Loading seq content.")
        println(Blocks.duo::class)
        println(Blocks.duo classEq ItemTurret::class)
        loadMeta()
        SqContent.loadContent()
    }

    companion object {
        const val dev = true
        const val repo = "LiuDai2011/Sequence"
        var MOD: LoadedMod? = null
        var MOD_PREFIX = ""
    }
}
