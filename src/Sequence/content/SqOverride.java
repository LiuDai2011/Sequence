package Sequence.content;

import Sequence.*;
import arc.Events;
import arc.struct.ObjectMap;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.bullet.BulletType;
import mindustry.game.EventType;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.Turret;

import static mindustry.Vars.content;
import static mindustry.Vars.ui;

public class SqOverride {
    public static void setup() {
        Events.on(EventType.ClientLoadEvent.class, ignore -> {
            ui.content = new SqContentInfoDialog();

            for (UnlockableContent content : content.blocks()) {
                if (content instanceof Turret turret) {
                    if (turret instanceof ItemTurret itemTurret) {
                        itemTurret.buildType = () -> itemTurret.new ItemTurretBuild() {
                            @Override
                            protected void shoot(BulletType type) {
                                if (liquids.get(SqLiquids.vectorizedFluid) > 5f) {
                                    liquids.remove(SqLiquids.vectorizedFluid, 5f);
                                    float multi = ((SqLiquids.VectorizedFluid) SqLiquids.vectorizedFluid).damageMulti;
                                    float knockbackMulti = ((SqLiquids.VectorizedFluid) SqLiquids.vectorizedFluid).knockbackMulti;
                                    if (!SqTmp.damageMultiMap.containsKey(type)) {
                                        SqTmp.damageMultiMap.put(type, new ObjectMap<>());
                                    }
                                    if (!SqTmp.damageMultiMap.get(type).containsKey(multi)) {
                                        SqTmp.damageMultiMap.get(type).put(multi, Change.knockbackMulti(
                                                Change.damageMulti(type.copy(), multi), knockbackMulti));
                                    }
                                    super.shoot(SqTmp.damageMultiMap.get(type).get(multi));
                                    return;
                                }
                                super.shoot(type);
                            }
                        };
                    }
                }
            }

            Events.on(SqEventType.ContentStatInitEvent.class, e -> {
                if (e.content instanceof SeqElem elem) {
                    e.content.stats.useCategories = true;
                    e.content.stats.add(SqStat.sequenceOrder, elem.order() == -1 ? SqBundle.modCat("seq-null") : String.valueOf(elem.order()));
                    if (elem.order() != -1 && elem.statValue() != null) {
                        e.content.stats.add(SqStat.sequenceEffect, elem.statValue());
                    }
                }
            });
        });
    }
}
