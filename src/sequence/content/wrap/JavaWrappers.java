package sequence.content.wrap;

import arc.func.Prov;
import arc.struct.ObjectMap;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Building;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import sequence.content.SqLiquids;
import sequence.core.SqTmp;
import sequence.util.Change;
import sequence.util.Util;

public class JavaWrappers {
    public static Prov<Building> wrapItemTurretBuild(ItemTurret itemTurret) {
        return () -> itemTurret.new ItemTurretBuild() {
            public boolean consumeLiquidAndMultiShoot(SqLiquids.VectorizedFluid liq, float amount, BulletType type) {
                if (liquids == null) return false;
                if (liquids.get(liq) > amount) {
                    liquids.remove(liq, amount);
                    float multi = liq.getDamageMulti();
                    float knockbackMulti = liq.getKnockbackMulti();
                    Util.INSTANCE.checkKey(
                            Util.INSTANCE.checkKey(
                                    Util.INSTANCE.checkKey(
                                            SqTmp.damageMultiMap,
                                            type,
                                            ObjectMap::new
                                    ),
                                    multi,
                                    ObjectMap::new
                            ),
                            knockbackMulti,
                            () -> Change.INSTANCE.bulletType(type.copy(), bt -> {
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
                for (SqLiquids.VectorizedFluid fluid : SqLiquids.VectorizedFluid.Companion.getAll())
                    if (consumeLiquidAndMultiShoot(fluid, fluid.getConsumeAmount(), type))
                        return;
                super.shoot(type);
            }
        };
    }
}
