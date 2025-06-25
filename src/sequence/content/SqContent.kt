package sequence.content

import mindustry.io.SaveVersion
import sequence.core.ClassPutter
import sequence.ui.SqFonts
import sequence.ui.SqUI
import sequence.util.EntityRegister
import sequence.world.meta.ImagineEnergyChunk

object SqContent {
    fun loadContent() {
        SqFonts.loadFonts()
        registerChunks()
        SqStatusEffects.load()
        SqItems.load()
        SqLiquids.load()
        SqBulletTypes.load()
        SqBlocks.load()
//        SqUnitTypes.load()
        EntityRegister.load()
        SqOverride.setup()
        SqUI.load()

        ClassPutter()
    }

    fun registerChunks() {
        SaveVersion.addCustomChunk("seq-imagine-energy-chunk", ImagineEnergyChunk)
    }
}
