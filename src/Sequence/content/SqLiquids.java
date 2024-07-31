package Sequence.content;

import Sequence.SeqElem;
import Sequence.SqBundle;
import arc.Core;
import arc.graphics.Color;
import arc.scene.event.ClickListener;
import arc.scene.ui.Button;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.util.Scaling;
import arc.util.Strings;
import mindustry.content.Blocks;
import mindustry.ctype.UnlockableContent;
import mindustry.type.Liquid;
import mindustry.ui.Styles;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.meta.StatValue;
import mindustry.world.meta.StatValues;

import static mindustry.Vars.content;
import static mindustry.Vars.ui;

public class SqLiquids {
    public static SqLiquid crystallizedFluid, vectorizedFluid;

    public static void load() {
        crystallizedFluid = new SqLiquid("crystallized-fluid") {{
            color = Color.valueOf("d1eeee");
            heatCapacity = 1.2f;
            boilPoint = 2.3f;
            temperature = 0.3f;
        }};
        vectorizedFluid = new VectorizedFluid("vectorized-fluid");
    }

    public static class SqLiquid extends Liquid implements SeqElem {
        public int ord = -1;

        public SqLiquid(String name) {
            super(name);
        }

        @Override
        public int order() {
            return ord;
        }

        @Override
        public StatValue statValue() {
            return null;
        }
    }

    public static class VectorizedFluid extends SqLiquid {
        public final float damageMulti = 1.183f;
        public final float knockbackMulti = 1.173f;

        public VectorizedFluid(String name) {
            super(name);
            color = Color.valueOf("c5d6f2");
            ord = 3;
        }

        @Override
        public StatValue statValue() {
            return table -> {
                for (UnlockableContent content : content.blocks()) {
                    if (content instanceof ItemTurret turret && turret.consumesLiquid(this)) {
                        table.row().table(Styles.grayPanel, bt -> {
                            bt.left().top().defaults().padRight(3).left();
                            Button button = new Button(Styles.cleari);
                            button.table(st -> {
                                st.row();
                                st.add(new Image(turret.fullIcon).setScaling(Scaling.fit));
                            }).size(32);
                            button.margin(5);
                            button.clicked(() -> ui.content.show(turret));
                            bt.add(button);
                            bt.add(SqBundle.format(SqBundle.cat("stat", "bullet-damage-multi"),
                                    Strings.autoFixed(damageMulti * 100f - 100f, 2),
                                    Strings.autoFixed(knockbackMulti * 100f - 100f, 2)));
                            bt.row();
                        }).padLeft(0).padTop(5).padBottom(5).growX().margin(10);
                    }
                }
                table.row();
            };
        }
    }
}
