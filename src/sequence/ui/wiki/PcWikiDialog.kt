package sequence.ui.wiki

import arc.scene.ui.layout.Table
import mindustry.ui.Styles
import mindustry.ui.dialogs.BaseDialog
import sequence.core.SqBundle

class PcWikiDialog : BaseDialog(SqBundle("mainmenu.wiki.text")) {
    val contentTable: Table by lazy { Table() }
    var page: Int = 1

    init {
        shouldPause = true
        stack(contentTable)
        shown { rebuild() }
        onResize { rebuild() }
        addCloseButton()
    }

    fun rebuild() {
        contentTable.clear()
        // range: (page - 1) * ps --> page * ps - 1
        for (i in (page - 1) * pageSize..<page * pageSize) {
            contentTable.table(Styles.grayPanel) { table ->
                table.table {
                    it.add("$i")
                }.left()
                table.add()
            }.row()
        }
    }

    companion object {
        const val pageSize = 20
    }
}