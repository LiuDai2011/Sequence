package sequence.ui

import mindustry.ui.dialogs.BaseDialog
import sequence.core.SqBundle

class MobileMainmenuDialog : BaseDialog(SqBundle("mainmenu.mobile.title")) {
    init {
        addCloseButton()
    }
}
