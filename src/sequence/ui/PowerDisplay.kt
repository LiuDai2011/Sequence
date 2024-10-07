package sequence.ui

import arc.scene.ui.Image
import arc.scene.ui.layout.Stack
import arc.scene.ui.layout.Table
import arc.util.Scaling
import arc.util.Strings
import mindustry.Vars
import mindustry.core.UI
import mindustry.gen.Icon
import mindustry.ui.Styles

class PowerDisplay(amount: Float) : Stack() {
    init {
        add(Table { o: Table ->
            o.left()
            o.add(Image(Icon.power)).size(Vars.iconMed).scaling(Scaling.fit)
        })
        add(Table { t: Table ->
            t.left().bottom()
            t.add(if (amount >= 1000) UI.formatAmount(amount.toLong()) else Strings.autoFixed(amount, 2))
                .style(Styles.outlineLabel)
            t.pack()
        })
    }
}
