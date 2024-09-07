package sequence.content

import arc.scene.style.TextureRegionDrawable
import mindustry.gen.Icon
import mindustry.ui.Fonts
import sequence.ui.SqFonts

object SqIcon {
    const val all = "\ue800\ue801"
    var imagineEnergy: TextureRegionDrawable? = null
    var mainMenu: TextureRegionDrawable? = null

    init {
        mainMenu = Fonts.getGlyph(SqFonts.seqIcon, '\ue800')
        Icon.icons.put("seqMainMenu", mainMenu)
        imagineEnergy = Fonts.getGlyph(SqFonts.seqIcon, '\ue801')
        Icon.icons.put("imagineEnergy", imagineEnergy)
    }
}
