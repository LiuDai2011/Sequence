package Sequence.ui;

import arc.scene.ui.Image;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.util.Scaling;
import arc.util.Strings;
import mindustry.core.UI;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

import static mindustry.Vars.iconMed;

public class PowerDisplay extends Stack {
    public PowerDisplay(float amount) {
        add(new Table(o -> {
            o.left();
            o.add(new Image(Icon.power)).size(iconMed).scaling(Scaling.fit);
        }));

        add(new Table(t -> {
            t.left().bottom();
            t.add(amount >= 1000 ? UI.formatAmount((long) amount) : Strings.autoFixed(amount, 2))
                    .style(Styles.outlineLabel);
            t.pack();
        }));
    }
}
