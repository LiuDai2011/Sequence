package sequence.ui;

import sequence.content.SqIcon;
import sequence.graphic.SqColor;
import sequence.world.meta.imagine.ImagineEnergyRecord;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.util.Scaling;
import arc.util.Strings;
import mindustry.core.UI;
import mindustry.ui.Styles;

import static mindustry.Vars.iconMed;

public class ImagineEnergyDisplay extends Stack {
    public ImagineEnergyDisplay(ImagineEnergyRecord record) {
        float amount = record.amount, act = record.activity;

        add(new Table(o -> {
            o.left();
            o.add(new Image(SqIcon.INSTANCE.getImagineEnergy())).size(iconMed).scaling(Scaling.fit);
        }));

        add(new Table(t -> {
            t.left().bottom();
            t.add(amount * 60f >= 1000 ? UI.formatAmount((long) (amount * 60f)) : Strings.autoFixed(amount * 60f, 2))
                    .style(Styles.outlineLabel);
            t.pack();
        }));

        if (record.active)
            add(new Table(t -> {
                t.right().top().marginBottom(16);
                t.add(act >= 1000 ? UI.formatAmount((long) act) : Strings.autoFixed(act, 2))
                        .style(Styles.outlineLabel)
                        .color(SqColor.INSTANCE.getImagineEnergy());
                t.pack();
            }));
    }
}
