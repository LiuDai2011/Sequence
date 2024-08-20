package Sequence.content;

import Sequence.core.SeqElem;
import Sequence.core.SqBundle;
import Sequence.core.SqEventType;
import Sequence.core.SqTmp;
import Sequence.ui.SqContentInfoDialog;
import Sequence.world.meta.SqStat;
import Sequence.world.util.Change;
import Sequence.world.util.Util;
import arc.Events;
import arc.struct.ObjectMap;
import mindustry.content.Blocks;
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
                            public boolean consumeLiquidAndMultiShoot(SqLiquids.VectorizedFluid liq, float amount, BulletType type) {
                                if (liquids.get(liq) > amount) {
                                    liquids.remove(liq, amount);
                                    float multi = liq.damageMulti;
                                    float knockbackMulti = liq.knockbackMulti;
                                    Util.checkKey(
                                            Util.checkKey(
                                                    Util.checkKey(
                                                            SqTmp.damageMultiMap,
                                                            type,
                                                            ObjectMap::new
                                                    ),
                                                    multi,
                                                    ObjectMap::new
                                            ),
                                            knockbackMulti,
                                            () -> Change.bulletType(type.copy(), bt -> {
                                                bt.damage *= multi;
                                                bt.splashDamage *= multi;
                                                bt.lightningDamage *= multi;
                                                bt.knockback *= knockbackMulti;
                                            })
                                    );
                                    super.shoot(SqTmp.damageMultiMap.get(type).get(multi).get(knockbackMulti));
                                    return true;
                                }
                                return false;
                            }

                            @Override
                            protected void shoot(BulletType type) {
                                for (SqLiquids.VectorizedFluid fluid : SqLiquids.VectorizedFluid.all)
                                    if (consumeLiquidAndMultiShoot(fluid, fluid.consumeAmount, type))
                                        return;
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

            Blocks.itemSource.health = Integer.MAX_VALUE;
            Blocks.liquidSource.health = Integer.MAX_VALUE;
            Blocks.powerSource.health = Integer.MAX_VALUE;
            Blocks.payloadSource.health = Integer.MAX_VALUE;
            Blocks.heatSource.health = Integer.MAX_VALUE;
            Blocks.itemVoid.health = Integer.MAX_VALUE;
            Blocks.liquidVoid.health = Integer.MAX_VALUE;
            Blocks.powerVoid.health = Integer.MAX_VALUE;
            Blocks.payloadVoid.health = Integer.MAX_VALUE;
        });
    }
}
