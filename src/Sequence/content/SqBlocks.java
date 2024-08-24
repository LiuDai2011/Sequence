package Sequence.content;

import Sequence.SeqMod;
import Sequence.graphic.SqColor;
import Sequence.world.blocks.imagine.ImagineCenter;
import Sequence.world.blocks.imagine.ImagineNode;
import Sequence.world.blocks.production.MultiCrafter;
import Sequence.world.drawer.DrawBottom;
import Sequence.world.drawer.NoCheckDrawLiquidRegion;
import Sequence.world.entities.SpreadPointBulletType;
import Sequence.world.meta.Formula;
import Sequence.world.meta.imagine.BuildingIEc;
import Sequence.world.meta.imagine.ImagineEnergyRecord;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.PointBulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.draw.DrawArcSmelt;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawMulti;

public class SqBlocks {
    public static Block multiFluidMixer, berylliumCrystallizer, test, test1, test2;

    public static void load() {
        multiFluidMixer = new MultiCrafter("multi-fluid-mixer") {{
            addFormula(
                    new Formula(
                            ItemStack.with(SqItems.crystallizedBeryllium, 1),
                            LiquidStack.with(Liquids.water, 6),
                            1.5f,
                            ItemStack.empty,
                            LiquidStack.with(SqLiquids.crystallizedFluid, 5),
                            0,
                            0.5f * 60f,
                            false),
                    new Formula(
                            ItemStack.with(SqItems.grainBoundaryAlloy, 1),
                            LiquidStack.with(SqLiquids.crystallizedFluid, 6),
                            2,
                            ItemStack.empty,
                            LiquidStack.with(SqLiquids.vectorizedFluid, 4.5),
                            0,
                            1.2f * 60f,
                            false)
            );

            requirements(Category.crafting, ItemStack.with(
                    Items.graphite, 60,
                    SqItems.pureSilicon, 30,
                    SqItems.crystallizedBeryllium, 120,
                    Items.tungsten, 80));

            drawer = new DrawMulti(
                    new DrawBottom(),
                    new NoCheckDrawLiquidRegion(),
                    new DrawDefault()
            );

            scaledHealth = 65;

            craftEffect = Fx.pulverizeMedium;
            size = 2;

            itemCapacity = 20;
            liquidCapacity = 60;

            ord = 34;
        }};

        berylliumCrystallizer = new MultiCrafter("beryllium-crystallizer") {{
            addFormula(new Formula(ItemStack.with(Items.beryllium, 4),
                    LiquidStack.empty,
                    1,
                    ItemStack.with(SqItems.crystallizedBeryllium, 3),
                    LiquidStack.empty,
                    0,
                    1.57f * 60f));
            onlyOneFormula = true;

            requirements(Category.crafting, ItemStack.with(Items.graphite, 80,
                    Items.tungsten, 20,
                    Items.beryllium, 60));

            drawer = new DrawMulti(
                    new DrawBottom(),
//                    new DrawEfficiency(),
                    new DrawArcSmelt(),
                    new DrawDefault()
            );

            scaledHealth = 73;
            size = 3;

            itemCapacity = 30;
        }};

        new ItemTurret("acacac") {{
            requirements(Category.turret, ItemStack.empty);
            ammo(
                    Items.copper, new SpreadPointBulletType() {{
                        shootEffect = Fx.instShoot;
                        hitEffect = Fx.instHit;
                        smokeEffect = Fx.smokeCloud;
                        trailEffect = Fx.instTrail;
                        despawnEffect = Fx.instBomb;
                        trailSpacing = 20f;
                        splashDamage = 1350;
                        splashDamageRadius = 18f;
                        buildingDamageMultiplier = 0.2f;
                        speed = 600;
                        hitShake = 6f;
                        ammoMultiplier = 1f;

                        fragBullet = new SpreadPointBulletType() {{
                            shootEffect = Fx.instShoot;
                            hitEffect = Fx.instHit;
                            smokeEffect = Fx.smokeCloud;
                            trailEffect = Fx.instTrail;
                            despawnEffect = Fx.instBomb;
                            trailSpacing = 20f;
                            splashDamage = 1350;
                            splashDamageRadius = 18f;
                            buildingDamageMultiplier = 0.2f;
                            speed = 300;
                            hitShake = 6f;
                            ammoMultiplier = 1f;
                            lifetime = 1;

                            fragBullet = new PointBulletType() {{
                                shootEffect = Fx.instShoot;
                                hitEffect = Fx.instHit;
                                smokeEffect = Fx.smokeCloud;
                                trailEffect = Fx.instTrail;
                                despawnEffect = Fx.instBomb;
                                trailSpacing = 20f;
                                splashDamage = 1350;
                                splashDamageRadius = 18f;
                                buildingDamageMultiplier = 0.2f;
                                speed = 300;
                                hitShake = 6f;
                                ammoMultiplier = 1f;
                                lifetime = 1;

                                fragBullets = 3;
                                fragBullet = new BasicBulletType(16, 0, "shell") {{
                                    lifetime = 999;
                                    splashDamage = 1350;
                                    splashDamageRadius = 27;
                                    homingPower = 0.3f;
                                    homingRange = 8000;
                                    width = 13f;
                                    height = 19f;
                                    hitSize = 27f;
                                    shootEffect = new MultiEffect(Fx.shootBigColor, Fx.colorSparkBig);
                                    smokeEffect = Fx.shootBigSmoke;
                                    ammoMultiplier = 1;
                                    pierce = true;
                                    pierceBuilding = true;
                                    hitColor = backColor = trailColor = Pal.tungstenShot;
                                    frontColor = Color.white;
                                    trailWidth = 2.2f;
                                    trailLength = 11;
                                    hitEffect = despawnEffect = Fx.hitBulletColor;
                                    rangeChange = 40f;
                                    buildingDamageMultiplier = 0.3f;
                                }};
                            }};
                            fragBullets = 5;

                            radius = 300;
                            minRadius = 80;
                        }};

                        fragBullets = 5;

                        radius = 300;
                        minRadius = 80;
                    }},
                    Items.lead, new SpreadPointBulletType() {{
                        shootEffect = Fx.instShoot;
                        hitEffect = Fx.instHit;
                        smokeEffect = Fx.smokeCloud;
                        trailEffect = Fx.instTrail;
                        despawnEffect = Fx.instBomb;
                        trailSpacing = 20f;
                        splashDamage = 1350;
                        splashDamageRadius = 18f;
                        buildingDamageMultiplier = 0.2f;
                        speed = 600;
                        hitShake = 6f;
                        ammoMultiplier = 1f;

                        fragBullet = new SpreadPointBulletType() {{
                            shootEffect = Fx.instShoot;
                            hitEffect = Fx.instHit;
                            smokeEffect = Fx.smokeCloud;
                            trailEffect = Fx.instTrail;
                            despawnEffect = Fx.instBomb;
                            trailSpacing = 20f;
                            splashDamage = 1350;
                            splashDamageRadius = 18f;
                            buildingDamageMultiplier = 0.2f;
                            speed = 300;
                            hitShake = 6f;
                            ammoMultiplier = 1f;
                            lifetime = 1;

                            fragBullet = new PointBulletType() {{
                                shootEffect = Fx.instShoot;
                                hitEffect = Fx.instHit;
                                smokeEffect = Fx.smokeCloud;
                                trailEffect = Fx.instTrail;
                                despawnEffect = Fx.instBomb;
                                trailSpacing = 20f;
                                splashDamage = 1350;
                                splashDamageRadius = 18f;
                                buildingDamageMultiplier = 0.2f;
                                speed = 300;
                                hitShake = 6f;
                                ammoMultiplier = 1f;
                                lifetime = 1;
                            }};
                            fragBullets = 5;

                            radius = 300;
                            minRadius = 80;
                        }};

                        fragBullets = 5;

                        radius = 300;
                        minRadius = 80;
                    }},
                    Items.metaglass, new SpreadPointBulletType() {{
                        shootEffect = Fx.instShoot;
                        hitEffect = Fx.instHit;
                        smokeEffect = Fx.smokeCloud;
                        trailEffect = Fx.instTrail;
                        despawnEffect = Fx.instBomb;
                        trailSpacing = 20f;
                        splashDamage = 1350;
                        splashDamageRadius = 18f;
                        buildingDamageMultiplier = 0.2f;
                        speed = 600;
                        hitShake = 6f;
                        ammoMultiplier = 1f;

                        fragBullet = new PointBulletType() {{
                            shootEffect = Fx.instShoot;
                            hitEffect = Fx.instHit;
                            smokeEffect = Fx.smokeCloud;
                            trailEffect = Fx.instTrail;
                            despawnEffect = Fx.instBomb;
                            trailSpacing = 20f;
                            splashDamage = 1350;
                            splashDamageRadius = 18f;
                            buildingDamageMultiplier = 0.2f;
                            speed = 300;
                            hitShake = 6f;
                            ammoMultiplier = 1f;
                            lifetime = 1;
                        }};

                        fragBullets = 5;

                        radius = 300;
                        minRadius = 80;
                    }},
                    Items.graphite, new PointBulletType() {{
                        shootEffect = Fx.instShoot;
                        hitEffect = Fx.instHit;
                        smokeEffect = Fx.smokeCloud;
                        trailEffect = Fx.instTrail;
                        despawnEffect = Fx.instBomb;
                        trailSpacing = 20f;
                        splashDamage = 1350;
                        splashDamageRadius = 18f;
                        buildingDamageMultiplier = 0.2f;
                        speed = 600;
                        hitShake = 6f;
                        ammoMultiplier = 1f;
                    }}
            );

            shoot = new ShootAlternate(3.5f);

            recoils = 2;

            recoil = 0.5f;
            shootY = 3f;
            reload = 20f;
            range = 600;
            shootCone = 15f;
            ammoUseEffect = Fx.casing1;
            health = Integer.MAX_VALUE;
            inaccuracy = 2f;
            rotateSpeed = 10f;
            coolant = consumeCoolant(0.1f);
            researchCostMultiplier = 0.05f;

            limitRange();
        }};

        if (!SeqMod.dev) return;
        test = new MultiCrafter("test-multi-crafter") {{
            addFormula(
                    new Formula(
                            ItemStack.with(Items.thorium, 2),
                            LiquidStack.with(Liquids.water, 5),
                            114, ItemStack.with(SqItems.grainBoundaryAlloy, 11),
                            LiquidStack.with(Liquids.cryofluid, 7),
                            6,
                            90, false),
                    new Formula(
                            ItemStack.with(SqItems.berylliumalAlloy, 2, Items.surgeAlloy, 3),
                            LiquidStack.with(SqLiquids.crystallizedFluid, 3, Liquids.water, 5),
                            3,
                            ItemStack.with(SqItems.phaseCore, 5, SqItems.grainBoundaryAlloy, 10),
                            LiquidStack.with(SqLiquids.vectorizedFluid, 2, Liquids.cryofluid, 7),
                            5,
                            8, false),
                    new Formula(
                            ItemStack.empty,
                            LiquidStack.empty,
                            3,
                            new ImagineEnergyRecord(5, 10, 0, true),
                            ItemStack.empty,
                            LiquidStack.empty,
                            5,
                            new ImagineEnergyRecord(3, 1, 2, false),
                            8, false)
            );

            requirements(Category.crafting, ItemStack.empty);

            craftEffect = Fx.pulverizeMedium;
            size = 2;
            hasItems = true;
            hasLiquids = true;
            hasPower = true;

            itemCapacity = 100;
            liquidCapacity = 100;

            ord = 11;
        }};
        test1 = new MultiCrafter("test-one-formula-crafter") {{
            addFormula(
                    new Formula(
                            ItemStack.with(Items.thorium, 2),
                            LiquidStack.with(Liquids.water, 5),
                            114,
                            ItemStack.with(SqItems.grainBoundaryAlloy, 11),
                            LiquidStack.with(Liquids.cryofluid, 7),
                            6,
                            90, false),
                    new Formula(
                            ItemStack.with(SqItems.berylliumalAlloy, 2, Items.surgeAlloy, 3),
                            LiquidStack.with(SqLiquids.crystallizedFluid, 3, Liquids.water, 5),
                            3,
                            ItemStack.with(SqItems.phaseCore, 5, SqItems.grainBoundaryAlloy, 10),
                            LiquidStack.with(SqLiquids.vectorizedFluid, 2, Liquids.cryofluid, 7),
                            5,
                            8, false),
                    new Formula(
                            ItemStack.empty,
                            LiquidStack.empty,
                            3,
                            new ImagineEnergyRecord(5, 10, 0, true),
                            ItemStack.empty,
                            LiquidStack.empty,
                            5,
                            new ImagineEnergyRecord(3, 1, 2, false),
                            8, false)
            );
            onlyOneFormula = true;

            requirements(Category.crafting, ItemStack.empty);

            craftEffect = Fx.pulverizeMedium;
            size = 2;
            hasItems = true;
            hasLiquids = true;
            hasPower = true;

            itemCapacity = 100;
            liquidCapacity = 100;
        }};
        test2 = new Wall("test2") {{
            requirements(Category.effect, ItemStack.empty);
            health = 100;
            update = true;
            buildType = () -> new WallBuild() {
                @Override
                public void updateTile() {
                    for (int len = 1; len < 15; ++len) {
                        for (int i = 0; i < 4; ++i) {
                            Tile tile1 = Vars.world.tile(Geometry.d4x(i) * len + tileX(), Geometry.d4y(i) * len + tileY());
                            if (tile1 != null && tile1.build != null && tile1.build instanceof BuildingIEc icc) {
//                                icc.getIEM().add(Time.delta);
                                if (Mathf.chanceDelta(tile1.block().size * 0.1f / len))
                                    Fx.healBlockFull.at(tile1.build.x, tile1.build.y, 0, SqColor.LiuDai.cpy().a(0.3f), tile1.build.block);
                            }
                        }
                    }
                    super.updateTile();
                }
            };
        }};
        new ImagineNode("imagine-node") {{
            requirements(Category.effect, ItemStack.empty);
        }};
        new ImagineCenter("imagine-center") {{
            requirements(Category.effect, ItemStack.empty);
        }};
    }
}
