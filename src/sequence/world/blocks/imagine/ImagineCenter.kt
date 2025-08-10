package sequence.world.blocks.imagine

import arc.func.Prov
import arc.scene.ui.layout.Table
import arc.struct.ObjectSet
import mindustry.gen.Building
import mindustry.ui.Styles
import sequence.content.SqFx
import sequence.world.meta.imagine.ImagineLink
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
        private var checkLinkTimer = 0

        val links: ObjectSet<ImagineLink> = ObjectSet()
        val requestQueue = RequestQueue()

        var imagineEnergy: Float = 0f

        fun putRequest(request: Request) {}

        fun tryAdd(building: ImagineLink) {
            links.add(building)
        }

        fun checkLinks() {
            for (link in links) {
                link.center = null
            }
            links.clear()
        }

        override fun updateTile() {
            super.updateTile()
            if (checkLinkTimer++ > links.size + 2) {
                checkLinks()
                checkLinkTimer = 0
            }
        }

        override fun onRemoved() {
            super.onRemoved()
            for (build in links) {
                build.center = null
            }
        }

        override fun buildConfiguration(table: Table?) {
            if (table == null) return
            table.table(Styles.grayPanel) {
                it.row()
                for (n in links) {
                    val build = n.toBuild()
                    it.add("${build.x}, ${build.y}").row()
                    SqFx.egbaHit.at(build)
                }
            }
        }
    }
}
