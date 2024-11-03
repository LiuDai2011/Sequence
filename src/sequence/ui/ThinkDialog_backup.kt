//package sequence.ui
//
//import arc.Core
//import arc.graphics.g2d.Draw
//import arc.graphics.g2d.TextureRegion
//import arc.math.Mathf
//import arc.scene.Element
//import arc.struct.PQueue
//import mindustry.content.Blocks
//import mindustry.ui.dialogs.BaseDialog
//import sequence.SeqMod
//import sequence.core.SqBundle
//import java.util.PriorityQueue
//
//class ThinkDialog_backup : BaseDialog(SqBundle("ui.think.title")) {
//    val canvas = ThinkCanvas()
//
//    init {
//        stack(canvas, table {
//
//        }.get())
//        canvas.figure.apply {
//            tilemap[3][4].block = Blocks.impulsePump.fullIcon
//            tilemap[3][3].block = Blocks.pulseConduit.fullIcon
//            tilemap[3][3].rotation = 270f
//            tilemap[3][0].block = Blocks.liquidTank.fullIcon
//        }
//        addCloseListener()
//    }
//
//    data class RenderRequest(val action: () -> Unit, val z: Int = 0) {
//        operator fun compareTo(other: RenderRequest) = z.compareTo(other.z)
//    }
//
//    data class ThinkTile(
//        var floor: TextureRegion = Blocks.air.fullIcon,
//        var overlay: TextureRegion = Blocks.air.fullIcon,
//        var block: TextureRegion = Blocks.air.fullIcon,
//        var rotation: Float = 0f
//    )
//
//    class ThinkFigure(val width: Int = 9, val height: Int = 9) {
//        val tilemap: Array<Array<ThinkTile>> = Array(width) { i ->
//            Array(height) { j ->
//                if (width % 3 == 0 && height % 3 == 0)
//                    ThinkTile(floor = defaultFloor[((i / 3 + j / 3) % 2 + (if (i % 3 == 1 && j % 3 == 1) 1 else 0)) % 2])
//                else
//                    ThinkTile(floor = defaultFloor[(i + j) % 2])
//            }
//        }
//    }
//
//    class ThinkCanvas : Element() {
//        companion object {
//            const val sqrt2 = 1.4142135f
//        }
//
//        val renderQueue: PriorityQueue<RenderRequest> = PriorityQueue()
//
//        var figure: ThinkFigure = ThinkFigure()
//
//        override fun draw() {
//            val color = Draw.getColor()
//            Draw.color()
//
//            val tilemap = figure.tilemap
//            val width = figure.width
//            val height = figure.height
//            x = Core.graphics.width / 2f - 16 * width
//            y = Core.graphics.height / 2f - 16 * height
//            for (row in tilemap.indices) {
//                for (idx in tilemap[row].indices) {
//                    val tile = tilemap[row][idx]
//                    drawIcon(tile.floor, 32 * row, 32 * idx, tile.rotation)
//                }
//            }
//            for (row in tilemap.indices) {
//                for (idx in tilemap[row].indices) {
//                    val tile = tilemap[row][idx]
//                    drawIcon(tile.overlay, 32 * row, 32 * idx, tile.rotation)
//                }
//            }
//            for (row in tilemap.indices) {
//                for (idx in tilemap[row].indices) {
//                    val tile = tilemap[row][idx]
//                    drawIcon(tile.block, 32 * row, 32 * idx, tile.rotation)
//                }
//            }
//            Draw.color(color)
//        }
//
//        fun drawIcon(icon: TextureRegion, offsetX: Int = 0, offsetY: Int = 0, rotation: Float = 0f) =
//            Draw.rect(
//                icon,
//                x + offsetX + (icon.width.toFloat() * (2 + (Mathf.sinDeg(-rotation - 135) * sqrt2)) - 32) / 2,
//                y + offsetY + (icon.height.toFloat() * (2 + (Mathf.cosDeg(-rotation - 135) * sqrt2)) - 32) / 2,
//                icon.width.toFloat(), icon.height.toFloat(),
//                0f, 0f, rotation
//            )
//    }
//
//    companion object {
//        var defaultFloor: Array<TextureRegion> = Array(2) {
//            Core.atlas.find(SeqMod.name("def-floor-0${it + 1}"))
//        }
//    }
//}
