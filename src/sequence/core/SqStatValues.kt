package sequence.core

import arc.scene.ui.Image
import arc.scene.ui.layout.Table
import arc.util.Strings
import mindustry.gen.Icon
import mindustry.type.LiquidStack
import mindustry.ui.Styles
import mindustry.world.meta.StatValue
import sequence.ui.SqUI.uiILPFormula
import sequence.world.meta.Formula

object SqStatValues {
    fun formulaStat(form: Formula): StatValue {
        return StatValue { table: Table ->
            table.row()
            table.table(Styles.grayPanel) { t: Table ->
                t.table { bt: Table ->
                    bt.left().top().defaults().padRight(3f).left()
                    uiILPFormula(
                        form.inputItem,
                        form.inputLiquid,
                        form.inputPower * form.time,
                        form.inputImagine,
                        bt,
                        true
                    )
                    bt.add()
                    bt.table { ct: Table -> ct.add(Image(Icon.rightSmall)).grow().fill() }
                        .padLeft(10f).padRight(10f)
                    bt.add()
                    if (form.liquidSecond) {
                        val liquidStacks = arrayOfNulls<LiquidStack>(form.outputLiquid.size)
                        for (i in form.outputLiquid.indices) {
                            liquidStacks[i] =
                                LiquidStack(form.outputLiquid[i].liquid, form.outputLiquid[i].amount * form.time)
                        }
                        uiILPFormula(form.outputItem, liquidStacks, form.outputPower, form.outputImagine, bt, false)
                    } else uiILPFormula(
                        form.outputItem,
                        form.outputLiquid,
                        form.outputPower * form.time,
                        form.outputImagine,
                        bt,
                        true
                    )
                }.growX().left().row()
                t.add(
                    SqBundle.format(
                        SqBundle.cat("stat", "crafttime"),
                        Strings.autoFixed(form.time / 60f, 2)
                    )
                ).left()
            }.padLeft(0f).padTop(5f).padBottom(5f).growX().margin(10f)
        }
    }
}
