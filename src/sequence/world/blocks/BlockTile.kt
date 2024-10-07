package sequence.world.blocks

import arc.Core
import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Font
import arc.graphics.g2d.GlyphLayout
import arc.graphics.g2d.Lines
import arc.math.geom.Vec2
import arc.scene.ui.layout.Scl
import arc.util.pooling.Pools
import mindustry.Vars
import mindustry.graphics.Pal
import mindustry.ui.Fonts
import mindustry.world.Block
import sequence.util.Util.inZone

class BlockTile(var block: Block, var x: Int, var y: Int, var alpha: Float, var rotation: Float) {
    @JvmOverloads
    constructor(block: Block, x: Int, y: Int, rotation: Float = 0f) : this(block, x, y, 0.4f, rotation)

    @get:JvmName("valid")
    val valid: Boolean
        get() {
            for (x in 0 until block.size) {
                for (y in 0 until block.size) {
                    val tile = Vars.world.tile(this.x + x, this.y + y)
                    if (tile == null || tile.block() !== block) {
                        return false
                    }
                }
            }
            return true
        }

    fun draw() {
        mouse.set(Core.input.mouseWorld()).scl(1f / Vars.tilesize)
        min[x - 0.5f] = y - 0.5f
        center.set(min).add(block.size / 2f, block.size / 2f).scl(Vars.tilesize.toFloat())
        Draw.color(Color.white, alpha)
        Draw.rect(block.fullIcon, center.x, center.y, rotation)
        Draw.flush()
        if (inZone(min, Vec2(block.size.toFloat(), block.size.toFloat()), mouse)) {
            drawTip(center.x, center.y)
        }
    }

    fun drawTip(offsetX: Float, offsetY: Float) {
        val layout = Pools.obtain(GlyphLayout::class.java) { GlyphLayout() }
        val ints = tipFont.usesIntegerPositions()
        tipFont.setUseIntegerPositions(false)
        tipFont.data.setScale(0.25f / Scl.scl(1f))
        layout.setText(tipFont, block.localizedName)
        val width = layout.width
        tipFont.color = tipColor
        var dy = offsetY + block.size * Vars.tilesize / 2f + 3
        tipFont.draw(block.localizedName, offsetX, dy + layout.height + 1, 1)
        --dy
        Lines.stroke(2f, Color.darkGray)
        Lines.line(offsetX - width / 2 - 2, dy, offsetX + width / 2 + 1.5f, dy)
        Lines.stroke(1f, tipColor)
        Lines.line(offsetX - width / 2 - 2, dy, offsetX + width / 2 + 1.5f, dy)
        tipFont.setUseIntegerPositions(ints)
        tipFont.color = Color.white
        tipFont.data.setScale(1f)
        Draw.reset()
        Pools.free(layout)
        Draw.flush()
    }

    companion object {
        private val mouse = Vec2()
        private val min = Vec2()
        private val center = Vec2()
        var tipColor: Color = Pal.accent
        var tipFont: Font = Fonts.outline
    }
}
