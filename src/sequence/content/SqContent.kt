package sequence.content

import mindustry.io.SaveVersion
import sequence.core.ClassPutter
import sequence.io.ImagineEnergyChunk
import sequence.ui.SqFonts
import sequence.ui.SqUI

object SqContent {
    fun loadContent() {
        SqFonts.loadFonts()
        registerChunks()
        SqStatusEffects.load()
        SqItems.load()
        SqLiquids.load()
        SqBulletTypes.load()
        SqBlocks.load()
        SqOverride.setup()
        SqUI.load()

        ClassPutter()
    }

    fun registerChunks() {
        SaveVersion.addCustomChunk("seq-imagine-energy-chunk", ImagineEnergyChunk)
    }
}
