package sequence.ui

import arc.Core
import arc.graphics.g2d.Draw
import arc.graphics.g2d.TextureRegion
import arc.scene.Element
import mindustry.ui.dialogs.BaseDialog
import sequence.content.SqBlocks
import sequence.core.SqBundle
import sequence.core.SqLog

class ThinkDialog : BaseDialog(SqBundle("ui.think.title")) {
    init {
        add(ThinkCanvas())
        SqLog.info(SqBlocks.multiFluidMixer.fullIcon.height)
        SqLog.info(SqBlocks.berylliumCrystallizer.fullIcon.height)
    }

    data class ThinkTile(
        val floor: TextureRegion = Core.atlas.white(),
        val overlay: TextureRegion = Core.atlas.white(),
        val block: TextureRegion = Core.atlas.white()
    )

    class ThinkCanvas : Element() {
        companion object {
            const val width = 20
            const val height = 20
        }

        val tilemap: Array<Array<ThinkTile?>> = Array(Companion.width) { Array(Companion.height) { null } }

        override fun draw() {
            val color = Draw.getColor()
            Draw.color()

            x = Core.graphics.width / 2f
            y = Core.graphics.height / 2f
            drawIcon(SqBlocks.multiFluidMixer.fullIcon, 0, 0)
            drawIcon(SqBlocks.berylliumCrystallizer.fullIcon, 0, 0)
            for (row in tilemap) {
                for (tile in row) {
                    if (tile == null) continue
                    drawIcon(tile.floor)
                }
            }

            Draw.color(color)
        }

        fun drawIcon(icon: TextureRegion, offsetX: Int = 0, offsetY: Int = 0, rotation: Float = 0f) =
            Draw.rect(icon,
                x + offsetX + icon.width.toFloat() / 2,
                y + offsetY + icon.height.toFloat() / 2,
                icon.width.toFloat(), icon.height.toFloat(),
                0f, 0f, rotation)
    }
}
