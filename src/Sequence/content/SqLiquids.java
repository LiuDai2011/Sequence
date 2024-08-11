package Sequence.content;

import Sequence.core.SeqElem;
import Sequence.core.SqBundle;
import arc.graphics.Color;
import arc.scene.ui.Button;
import arc.scene.ui.Image;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Scaling;
import arc.util.Strings;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.bullet.BulletType;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.ui.Styles;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.meta.StatValue;

import static mindustry.Vars.*;

public class SqLiquids {
    public static SqLiquid crystallizedFluid, vectorizedFluid;

    public static void load() {
        crystallizedFluid = new SqLiquid("crystallized-fluid") {{
            color = Color.valueOf("d1eeee");
            heatCapacity = 1.2f;
            boilPoint = 2.3f;
            temperature = 0.3f;
        }};
        vectorizedFluid = new VectorizedFluid("vectorized-fluid") {{
            heatCapacity = 1f;
            boilPoint = 2f;
            temperature = 0.4f;
            damageMulti = 1.183f;
            knockbackMulti = 1.173f;
            consumeAmount = 8f;
            color = Color.valueOf("c5d6f2");
            ord = 3;
        }};
        new VectorizedFluid("aac") {{
            heatCapacity = 100f;
            boilPoint = 100f;
            temperature = -50f;
            damageMulti = 10000f;
            knockbackMulti = 10000f;
            consumeAmount = 1f;
            color = Color.valueOf("000000");
            ord = 3;
        }};
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
        public static final Seq<VectorizedFluid> all = new Seq<>();
        private static final StringBuilder builder = new StringBuilder();
        public float damageMulti = 0;
        public float knockbackMulti = 0;
        public float consumeAmount = 0;

        public VectorizedFluid(String name) {
            super(name);
            all.add(this);
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
                            }).size(iconMed);
                            button.margin(5);
                            button.clicked(() -> ui.content.show(turret));
                            bt.add(button);
                            bt.add(getDesc(turret));
                            bt.row();
                        }).padLeft(0).padTop(5).padBottom(5).growX().margin(10);
                    }
                }
                table.row();
            };
        }

        private String getDesc(ItemTurret it) {
            builder.setLength(0);
            builder.append(SqBundle.format(SqBundle.cat("stat", "bullet-damage-multi"),
                    Strings.autoFixed(damageMulti * 100f - 100f, 2)));

            boolean frag = false;
            for (ObjectMap.Entry<Item, BulletType> entry : it.ammoTypes)
                if (entry.value.knockback > 0) {
                    frag = true;
                    break;
                }
            if (frag) {
                builder.append("  ");
                builder.append(SqBundle.format(SqBundle.cat("stat", "bullet-knockback-multi"),
                        Strings.autoFixed(knockbackMulti * 100f - 100f, 2)));
            }
            return builder.toString();
        }
    }
}
