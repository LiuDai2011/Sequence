package sequence.ui.wiki

import arc.Core
import arc.math.geom.Vec2
import arc.scene.ui.Button
import arc.scene.ui.Image
import arc.scene.ui.ImageButton
import arc.scene.ui.layout.Table
import arc.struct.Seq
import mindustry.Vars
import mindustry.Vars.ui
import mindustry.core.UI
import mindustry.ui.Styles
import mindustry.ui.dialogs.BaseDialog
import sequence.content.SqContentMap
import sequence.core.SqBundle

class PcWikiDialog : BaseDialog(SqBundle("mainmenu.wiki.text")) {
    val contentTable: Table by lazy { Table() }
    var page: Int = 1

    init {
        shouldPause = true
        cont.add(contentTable).expand()
        shown { rebuild() }
        onResize { rebuild() }
        addCloseButton()
    }

    fun rebuild() {
        contentTable.clear()
        // range: (page - 1) * ps --> page * ps - 1
        contentTable.table { base ->
            for (idx in (page - 1) * pageSize..<page * pageSize) {
                val i = idx + 1
                base.table(Styles.grayPanel) { table ->
                    table.table (Styles.grayPanel) {
                        it.add("$i")
                    }
                    table.table (Styles.grayPanel) {
                        for (content in SqContentMap.seqMap.get(i) { Seq() })
                            it.add(Button(Styles.grayPanel).apply {
                                add(Image(content.uiIcon))
                                setSize(32f)
                                clicked {
                                    isChecked = false
                                    ui.content.show(content)
                                }
                            })
                    }.right()
                }.apply {
                    fillX()
                    minWidth(Core.graphics.width * 0.9f)
                    maxHeight(32f)
                    minHeight(32f)
                    row()
                }
                base.table(Styles.none)  {
                    it.height = 7f
                }.apply {
                    fillX()
                    row()
                }
            }
        }
    }

    companion object {
        const val pageSize = 20
    }
}
