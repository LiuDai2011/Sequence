package sequence.world.blocks.imagine

import arc.func.Prov
import arc.math.geom.Geometry
import arc.math.geom.Point2
import arc.struct.Seq
import mindustry.Vars
import mindustry.Vars.tilesize
import mindustry.Vars.world
import mindustry.entities.TargetPriority
import mindustry.gen.Building
import mindustry.graphics.Drawf
import mindustry.graphics.Pal
import mindustry.input.Placement
import mindustry.world.blocks.power.PowerNode
import mindustry.world.meta.Env
import sequence.graphic.dashRectBuild
import sequence.util.component1
import sequence.util.component2
import sequence.world.blocks.imagine.ImagineCenter.ImagineCenterBuild
import sequence.world.meta.imagine.ImagineLink
import sequence.world.meta.imagine.toImagineLink
import kotlin.math.abs

class ImagineNode(name: String) : ImagineBlock(name) {
    var range: Int = 5

    init {
        buildType = Prov { ImagineNodeBuild() }
        envEnabled = envEnabled or Env.space
        allowDiagonal = false
        underBullets = true
        priority = TargetPriority.transport
    }

    override fun drawPlace(x: Int, y: Int, rotation: Int, valid: Boolean) {
        for (i in 0..<4) {
            var maxLen = range + size / 2
            var dest: Building? = null
            val dir = Geometry.d4[i]
            val dx = dir.x
            val dy = dir.y
            val offset = size / 2
            for (j in 1 + offset..range + offset) {
                val other = world.build(x + j * dir.x, y + j * dir.y)

                if (other != null && other.isInsulated) {
                    break
                }
                if (other != null && other.block.hasPower && other.team === Vars.player.team() && other.block !is PowerNode) {
                    maxLen = j
                    dest = other
                    break
                }
            }
            Drawf.dashLine(
                Pal.placing,
                x * tilesize + dx * (tilesize * size / 2f + 2),
                y * tilesize + dy * (tilesize * size / 2f + 2),
                (x * tilesize + dx * maxLen * tilesize).toFloat(),
                (y * tilesize + dy * maxLen * tilesize).toFloat()
            )
            if (dest != null) {
                dashRectBuild(Pal.accent, dest)
            }
        }
    }

    override fun changePlacementPath(points: Seq<Point2?>?, rotation: Int, diagonal: Boolean) {
        if (!diagonal) {
            Placement.calculateNodes(
                points, this, rotation
            ) { point: Point2, other: Point2 ->
                abs(point.x - other.x).coerceAtLeast(abs(point.y - other.y)) <= range
            }
        }
    }

    inner class ImagineNodeBuild : Building(), ImagineLink {
        override var center: ImagineCenterBuild? = null

        override fun placed() {
            super.placed()
            checkLink()
        }

        private fun checkLink() {
            for (i in 1..range) {
                for (j in 0..4) {
                    val (dx, dy) = Geometry.d4(j)
                    val tx = tile.x.toInt() + dx * i + (size - 1) / 2
                    val ty = tile.y.toInt() + dy * i + (size - 1) / 2
                    val build = world.build(tx, ty) ?: continue
                    if (build is ImagineCenterBuild) {
                        if (center == null)
                            center = build
                    } else if (build is ImagineLink) {
                        if (center == null)
                            center = build.center
                        else
                            center!!.tryAdd(build.toImagineLink())
                    }
                }
            }
        }

        override fun updateTile() {
            super.updateTile()
            if (center?.tile?.build !== center) center = null
            if (center == null) checkLink()
            center?.links?.add(this.toImagineLink())
        }

        override fun onRemoved() {
            super.onRemoved()
            center?.links?.remove(this.toImagineLink())
        }

        override fun drawSelect() {
            super.drawSelect()
            dashRectBuild(Pal.accent, center ?: return)
        }
    }
}