package sequence.ui

import arc.scene.style.TextureRegionDrawable
import arc.util.Tmp
import mindustry.gen.Tex
import mindustry.graphics.Pal

object SqAssets {
    private val white by lazy { Tex.whiteui as TextureRegionDrawable }
    val darkUIAlpha by lazy { white.tint(Tmp.c1.set(Pal.darkestGray).a(0.7f)) }

    fun load() {
    }
}