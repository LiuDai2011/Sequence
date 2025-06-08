package sequence.ui

import arc.scene.ui.Image
import arc.scene.ui.layout.Stack
import arc.scene.ui.layout.Table
import arc.util.Scaling
import arc.util.Strings
import mindustry.Vars
import mindustry.core.UI
import mindustry.ui.Styles
import sequence.content.SqIcon
import sequence.world.meta.imagine.ImagineEnergyRecord

class ImagineEnergyDisplay(record: ImagineEnergyRecord) : Stack() {
    init {
        val amount = record.amount
//        val act: Float = record.activity
        add(Table { o: Table ->
            o.left()
            o.add(Image(SqIcon.imagineEnergy)).size(Vars.iconMed).scaling(Scaling.fit)
        })
        add(Table { t: Table ->
            t.left().bottom()
            t.add(
                if (amount * 60f >= 1000) UI.formatAmount((amount * 60f).toLong()) else Strings.autoFixed(
                    amount * 60f,
                    2
                )
            ).style(Styles.outlineLabel)
            t.pack()
        })
//        if (record.active) add(Table { t: Table ->
//            t.right().top().marginBottom(16f)
//            t.add(if (act >= 1000) UI.formatAmount(act.toLong()) else Strings.autoFixed(act, 2))
//                .style(Styles.outlineLabel)
//                .color(SqColor.imagineEnergy)
//            t.pack()
//        })
    }
}
