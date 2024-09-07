package sequence.content

import sequence.core.ClassPutter
import sequence.ui.SqFonts
import sequence.ui.SqUI

object SqContent {
    fun loadContent() {
        SqFonts.loadFonts()
        SqItems.load()
        SqLiquids.load()
        SqBlocks.load()
        SqOverride.setup()
        SqUI.load()

        ClassPutter()
    }
}
