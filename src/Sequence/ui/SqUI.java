package Sequence.ui;

import Sequence.SeqMod;
import Sequence.content.SqIcon;
import Sequence.graphic.SqColor;
import Sequence.world.meta.Formula;
import Sequence.world.meta.imagine.ImagineEnergyModule;
import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.Button;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.util.Scaling;
import arc.util.Strings;
import mindustry.core.UI;
import mindustry.gen.Icon;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.ItemDisplay;
import mindustry.ui.LiquidDisplay;
import mindustry.ui.Styles;
import mindustry.world.meta.StatUnit;

import static mindustry.Vars.*;

public class SqUI {
    public static Button formula(Formula form) {
        Button button = new Button();
        button.setStyle(Styles.underlineb);
        button.table(t -> t.table(bt -> {
            bt.left();
            uiILPFormula(form.inputItem, form.inputLiquid, form.inputPower, form.inputImagineEnergy, bt, false);
            bt.left();

            bt.add();
            bt.table(ct -> ct.add(new Image(Icon.rightSmall)).grow().fill()).padLeft(10).padRight(10);
            bt.add();

            uiILPFormula(form.outputItem, form.outputLiquid, form.outputPower, form.outputImagineEnergy, bt, false);
        }).left().top()).padLeft(5).margin(0).growX().left().top();
        return button;
    }

    public static void uiILPFormula(ItemStack[] items, LiquidStack[] liquids, float power, ImagineEnergyModule.ImagineEnergyRecord record, Table bt, boolean butt) {
        Table display;
        Button button;

        for (ItemStack stack : items) {
            display = new ItemDisplay(stack.item, stack.amount, false);
            if (butt) {
                button = new Button(Styles.cleari);
                button.add(display).size(iconMed);
                button.clicked(() -> ui.content.show(stack.item));
                bt.add(button).size(iconMed).left().padLeft(2).padRight(2);
            } else {
                bt.add(display).size(iconMed).left().padLeft(2).padRight(2);
            }
            bt.add();
        }

        for (LiquidStack stack : liquids) {
            display = new LiquidDisplay(stack.liquid, stack.amount, false) { // fuck
                public LiquidDisplay upd() {
                    clear();

                    add(new Stack() {{
                        add(new Image(liquid.uiIcon).setScaling(Scaling.fit));

                        if (amount != 0) {
                            Table t = new Table().left().bottom();
                            t.add(Strings.autoFixed(amount, 2)).style(Styles.outlineLabel);
                            add(t);
                        }
                    }}).size(iconMed).padRight(3 + (amount != 0 && Strings.autoFixed(amount, 2).length() > 2 ? 8 : 0));

                    if (perSecond) {
                        add(StatUnit.perSecond.localized()).padLeft(2).padRight(5).color(Color.lightGray).style(Styles.outlineLabel);
                    }

                    return this;
                }
            }.upd();
            if (butt) {
                button = new Button(Styles.cleari);
                button.add(display).size(iconMed);
                button.clicked(() -> ui.content.show(stack.liquid));
                bt.add(button).size(iconMed).left().padLeft(2).padRight(2);
            } else {
                bt.add(display).size(iconMed).left().padLeft(2).padRight(2);
            }
            bt.add();
        }

        if (!Mathf.zero(power)) {
            bt.add(new Stack() { // fuck
                public Stack upd(float amount) {
                    add(new Table(o -> {
                        o.left();
                        o.add(new Image(Icon.power)).size(iconMed).scaling(Scaling.fit);
                    }));

                    add(new Table(t -> {
                        t.left().bottom();
                        t.add(amount * 60f >= 1000 ? UI.formatAmount((long) (amount * 60f)) : Strings.autoFixed(amount * 60f, 2))
                                .style(Styles.outlineLabel);
                        t.pack();
                    }));
                    return this;
                }
            }.upd(power));
        }

        if (!record.zero()) {
            bt.add(new Stack() { // fuck
                public Stack upd(ImagineEnergyModule.ImagineEnergyRecord ier) {
                    float amount = ier.amount, act = ier.activity;

                    add(new Table(o -> {
                        o.left();
                        o.add(new Image(SqIcon.imagineEnergy)).size(iconMed).scaling(Scaling.fit);
                    }));

                    add(new Table(t -> {
                        t.left().bottom();
                        t.add(amount * 60f >= 1000 ? UI.formatAmount((long) (amount * 60f)) : Strings.autoFixed(amount * 60f, 2))
                                .style(Styles.outlineLabel);
                        t.pack();
                    }));

                    if (ier.active)
                        add(new Table(t -> {
                            t.right().top().marginBottom(16);
                            t.add(act >= 1000 ? UI.formatAmount((long) act) : Strings.autoFixed(act, 2))
                                    .style(Styles.outlineLabel)
                                    .color(SqColor.imagineEnergy);
                            t.pack();
                        }));
                    return this;
                }
            }.upd(record));
        }
    }
}
