package Sequence.ui;

import Sequence.content.SqIcon;
import Sequence.core.SqBundle;
import Sequence.core.SqLog;
import Sequence.world.meta.Formula;
import Sequence.world.meta.imagine.ImagineEnergyRecord;
import arc.Events;
import arc.math.Mathf;
import arc.scene.ui.Button;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.ItemDisplay;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.fragments.MenuFragment;

import static mindustry.Vars.iconMed;
import static mindustry.Vars.ui;

public class SqUI {
    public static BaseDialog dialog;

    public static Button formula(Formula form) {
        Button button = new Button();
        button.setStyle(Styles.underlineb);
        button.table(t -> t.table(bt -> {
            bt.left();
            uiILPFormula(form.inputItem, form.inputLiquid, form.inputPower, form.inputImagine, bt, false);
            bt.left();

            bt.add();
            bt.table(ct -> ct.add(new Image(Icon.rightSmall)).grow().fill()).padLeft(10).padRight(10);
            bt.add();

            uiILPFormula(form.outputItem, form.outputLiquid, form.outputPower, form.outputImagine, bt, false);
        }).left().top()).padLeft(5).margin(0).growX().left().top();
        return button;
    }

    public static void uiILPFormula(ItemStack[] items, LiquidStack[] liquids, float power, ImagineEnergyRecord record, Table bt, boolean butt) {
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
            display = new SqLiquidDisplay(stack.liquid, stack.amount, false, false);
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

        if (!Mathf.zero(power))
            bt.add(new PowerDisplay(power));

        if (!record.zero()) {
            bt.add(new ImagineEnergyDisplay(record));
        }
    }

    public static void load() {
        Events.on(EventType.ClientLoadEvent.class, e -> {
            dialog = new MobileMainmenuDialog();
            ui.menufrag.addButton(
                    new MenuFragment.MenuButton(
                            SqBundle.modCat("mainmenu", "text"),
                            SqIcon.mainMenu,
                            dialog::show,
                            new MenuFragment.MenuButton(
                                    SqBundle.modCat("mainmenu", "wiki", "text"),
                                    Icon.book,
                                    () -> SqLog.info("aaaa")
                            )
                    )
            );
        });
    }
}
