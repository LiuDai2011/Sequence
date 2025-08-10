package sequence.ui

import arc.scene.ui.Image
import arc.scene.ui.layout.Table
import arc.util.Time
import mindustry.Vars
import mindustry.ctype.UnlockableContent

object SqFragments {
    var unlockImportantVisiable: Float = 0.0f
    val unlockImportant by lazy {
        var table: Table? = null
        Vars.ui.hudGroup.fill { t ->
            t.name = "seq-unlock-frag-important"
            t.visible { true }
            t.update {
                unlockImportantVisiable *= 0.99f
                t.color.a = unlockImportantVisiable
            }
            t.top()

            table = t
        }
        table!!
    }

    fun unlock(content: UnlockableContent, important: Boolean) {
        if (Vars.state.isMenu) return

        if (important) {
            unlockImportantVisiable = 1.0f
            unlockImportant.table(SqAssets.darkUIAlpha) {
                it.add(Image(content.uiIcon)).margin(15f)
            }.also {
                Time.run(1.5f * 60f) {
                    it.get().remove()
                }
            }
        }
    }
}