package sequence.ui

import arc.Core
import arc.graphics.g2d.Draw
import arc.graphics.g2d.TextureRegion
import arc.math.Mathf
import arc.scene.Element
import mindustry.content.Blocks
import mindustry.ui.dialogs.BaseDialog
import sequence.SeqMod
import sequence.core.SqBundle
import java.util.*

class PonderDialog : BaseDialog(SqBundle("ui.ponder.title")) {
    val canvas = PonderCanvas()

    init {
        stack(canvas, table {

        }.get())
        canvas.figure.apply {
            tilemap[3][4].block = Blocks.impulsePump.fullIcon
            tilemap[3][3].block = Blocks.pulseConduit.fullIcon
            tilemap[3][3].rotation = 270f
            tilemap[3][0].block = Blocks.liquidTank.fullIcon
        }
        addCloseListener()
    }

    interface Base {
        var parent: Base?
        fun draw()
        fun update()
    }

    data class RenderRequest(val z: Float = 0f, val action: () -> Unit) : Comparable<RenderRequest> {
        override operator fun compareTo(other: RenderRequest) = z.compareTo(other.z)
    }

    class PonderTile(
        var floor: TextureRegion = Blocks.air.fullIcon,
        var overlay: TextureRegion = Blocks.air.fullIcon,
        var block: TextureRegion = Blocks.air.fullIcon,
        var rotation: Float = 0f,
        var x: Int = 0,
        var y: Int = 0
    ) : Base {
        override var parent: Base? = null

        override fun draw() {
            (parent as? PonderFigure)?.apply {
                renderQueue.add(RenderRequest(0f) {
                    drawIcon(floor, 32 * this@PonderTile.x, 32 * this@PonderTile.y, rotation)
                })
                renderQueue.add(RenderRequest(1f) {
                    drawIcon(overlay, 32 * this@PonderTile.x, 32 * this@PonderTile.y, rotation)
                })
                renderQueue.add(RenderRequest(2f) {
                    drawIcon(block, 32 * this@PonderTile.x, 32 * this@PonderTile.y, rotation)
                })
            }
        }

        override fun update() {
        }
    }

    class PonderFigure(val width: Int = 9, val height: Int = 9) : Base {
        val tilemap: Array<Array<PonderTile>> = Array(width) { i ->
            Array(height) { j ->
                (if (width % 3 == 0 && height % 3 == 0)
                    PonderTile(floor = defaultFloor[((i / 3 + j / 3) % 2 + (if (i % 3 == 1 && j % 3 == 1) 1 else 0)) % 2])
                else
                    PonderTile(floor = defaultFloor[(i + j) % 2])
                        )
                    .apply {
                        parent = this@PonderFigure
                        x = i
                        y = j
                    }
            }
        }

        val renderQueue: PriorityQueue<RenderRequest> = PriorityQueue()
        var x: Float = 0f
        var y: Float = 0f

        override fun update() {
            x = Core.graphics.width / 2f - 16 * width + 16
//            x = Core.graphics.width * (Interp.fade(Time.time / 30f % 1f) * 1.3f - 0.2f)
            y = Core.graphics.height / 2f - 16 * height + 16
            for (row in tilemap) {
                for (tile in row) {
                    tile.update()
                }
            }
        }

        override var parent: Base? = null

        override fun draw() {
            for (row in tilemap) {
                for (tile in row) {
                    tile.draw()
                }
            }
            while (renderQueue.isNotEmpty()) {
                val (_, action) = renderQueue.remove()
                action()
            }
        }

        fun drawIcon(icon: TextureRegion, offsetX: Int = 0, offsetY: Int = 0, rotation: Float = 0f) =
            Draw.rect(
                icon,
                x + offsetX + (icon.width.toFloat() * (2 + (Mathf.sinDeg(-rotation - 135) * sqrt2)) - 32) / 2,
                y + offsetY + (icon.height.toFloat() * (2 + (Mathf.cosDeg(-rotation - 135) * sqrt2)) - 32) / 2,
                icon.width.toFloat(), icon.height.toFloat(),
                0f, 0f, rotation
            )
    }

    class PonderCanvas : Element(), Base {
        var figure: PonderFigure = PonderFigure()
        override var parent: Base? = null

        init {
            update {
                figure.update()
                figure.parent = this
            }
        }

        override fun draw() {
            val color = Draw.getColor()
            Draw.color()

            figure.draw()

            Draw.color(color)
        }

        override fun update() {}
    }

    companion object {
        const val sqrt2 = 1.4142135f
        var defaultFloor: Array<TextureRegion> = Array(2) {
            Core.atlas.find(SeqMod.name("def-floor-0${it + 1}"))
        }
    }
}
