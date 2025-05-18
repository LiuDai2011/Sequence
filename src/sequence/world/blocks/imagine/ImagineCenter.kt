package sequence.world.blocks.imagine

import arc.func.Prov
import arc.scene.Element
import arc.scene.ui.layout.Table
import arc.struct.ObjectSet
import mindustry.gen.Building
import mindustry.graphics.Pal
import mindustry.ui.Styles
import sequence.graphic.dashRectBuild
import sequence.world.blocks.imagine.ImagineNode.ImagineNodeBuild
import sequence.world.meta.imagine.Request
import sequence.world.meta.imagine.RequestQueue

class ImagineCenter(name: String) : ImagineBlock(name) {
    init {
        buildType = Prov { ImagineCenterBuild() }
        configurable = true
        update = true
        underBullets = true
        allowDiagonal = false
    }

    inner class ImagineCenterBuild : Building() {
        val nodes: ObjectSet<ImagineNodeBuild> = ObjectSet()
        val requestQueue = RequestQueue()

        var imagineEnergy: Float = 0f

        fun putRequest(request: Request) {}

        override fun buildConfiguration(table: Table?) {
            if (table == null) return
            table.add(object : Element() {
                override fun draw() {
                    for (n in nodes) {
                        dashRectBuild(Pal.accent, n)
                    }
                }
            })
            table.table(Styles.grayPanel) {
                it.row()
                for (n in nodes) {
                    it.add("${n.x}, ${n.y}").row()
                }
            }
        }

        override fun onRemoved() {
            super.onRemoved()
            for (build in nodes) {
                build.kill()
            }
        }
    }
}