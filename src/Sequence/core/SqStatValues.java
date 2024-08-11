package Sequence.core;

import Sequence.ui.SqUI;
import Sequence.world.meta.Formula;
import arc.scene.ui.Image;
import arc.util.Strings;
import mindustry.gen.Icon;
import mindustry.type.LiquidStack;
import mindustry.ui.Styles;
import mindustry.world.meta.StatValue;

public class SqStatValues {
    public static StatValue formulaStat(Formula form) {
        return table -> {
            table.row();
            table.table(Styles.grayPanel, t -> {
                t.table(bt -> {
                    bt.left().top().defaults().padRight(3).left();
                    SqUI.uiILPFormula(form.inputItem, form.inputLiquid, form.inputPower * form.time, form.inputImagine, bt, true);

                    bt.add();
                    bt.table(ct -> ct.add(new Image(Icon.rightSmall)).grow().fill()).padLeft(10).padRight(10);
                    bt.add();

                    if (form.liquidSecond) {
                        LiquidStack[] liquidStacks = new LiquidStack[form.outputLiquid.length];
                        for (int i = 0; i < form.outputLiquid.length; i++) {
                            liquidStacks[i] = new LiquidStack(form.outputLiquid[i].liquid, form.outputLiquid[i].amount * form.time);
                        }
                        SqUI.uiILPFormula(form.outputItem, liquidStacks, form.outputPower, form.outputImagine, bt, false);
                    } else
                        SqUI.uiILPFormula(form.outputItem, form.outputLiquid, form.outputPower * form.time, form.outputImagine, bt, true);
                }).growX().left().row();
                t.add(SqBundle.format(SqBundle.cat("stat", "crafttime"),
                        Strings.autoFixed(form.time / 60f, 2))).left();
            }).padLeft(0).padTop(5).padBottom(5).growX().margin(10);
        };
    }
}
