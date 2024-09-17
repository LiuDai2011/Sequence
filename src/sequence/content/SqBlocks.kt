package sequence.content

import arc.func.Prov
import arc.graphics.Color
import arc.math.Mathf
import arc.math.geom.Geometry
import mindustry.Vars
import mindustry.content.Fx
import mindustry.content.Items
import mindustry.content.Liquids
import mindustry.entities.bullet.BasicBulletType
import mindustry.entities.bullet.PointBulletType
import mindustry.entities.effect.MultiEffect
import mindustry.entities.pattern.ShootAlternate
import mindustry.graphics.Pal
import mindustry.type.Category
import mindustry.type.ItemStack
import mindustry.type.LiquidStack
import mindustry.world.Block
import mindustry.world.blocks.defense.Wall
import mindustry.world.blocks.defense.turrets.ItemTurret
import mindustry.world.draw.DrawArcSmelt
import mindustry.world.draw.DrawDefault
import mindustry.world.draw.DrawMulti
import mindustry.world.meta.StatValue
import sequence.SeqMod
import sequence.core.SeqElem
import sequence.graphic.SqColor
import sequence.world.blocks.defense.BatteryWall
import sequence.world.blocks.defense.SqShieldWall
import sequence.world.blocks.defense.SqWall
import sequence.world.blocks.defense.UnionWall
import sequence.world.blocks.production.MultiCrafter
import sequence.world.drawer.DrawBottom
import sequence.world.drawer.NoCheckDrawLiquidRegion
import sequence.world.entities.SpreadPointBulletType
import sequence.world.meta.Formula
import sequence.world.meta.imagine.BuildingIEc
import sequence.world.meta.imagine.ImagineEnergyRecord

object SqBlocks {
    lateinit var multiFluidMixer: Block
    lateinit var berylliumCrystallizer: Block
    lateinit var berylliumalAlloyWall: Block
    lateinit var berylliumalAlloyWallLarge: Block
    lateinit var grainBoundaryAlloyWall: Block
    lateinit var grainBoundaryAlloyWallLarge: Block
    lateinit var pureCapacitanceWall: Block
    lateinit var pureCapacitanceWallLarge: Block
    lateinit var intensifiedShieldedWall: Block
    lateinit var intensifiedShieldedWallLarge: Block

    fun load() {
        // region defense
        berylliumalAlloyWall = object : SqWall("berylliumal-alloy-wall") {
            init {
                requirements(Category.defense, ItemStack.with(SqItems.berylliumalAlloy, 6))
                health = 230
            }

            override fun statValue() = null
        }
        berylliumalAlloyWallLarge = object : SqWall("berylliumal-alloy-wall-large") {
            init {
                requirements(Category.defense, ItemStack.with(SqItems.berylliumalAlloy, 24))
                size = 2
                health = 230 * 4
            }

            override fun statValue() = null
        }
        grainBoundaryAlloyWall = object : UnionWall("grain-boundary-alloy-wall") {
            init {
                requirements(Category.defense, ItemStack.with(SqItems.grainBoundaryAlloy, 6))
                health = 280
            }
        }
        grainBoundaryAlloyWallLarge = object : UnionWall("grain-boundary-alloy-wall-large") {
            init {
                requirements(Category.defense, ItemStack.with(SqItems.grainBoundaryAlloy, 24))
                size = 2
                health = 280 * 4
                apportionedSpeed = 0.0017f
            }
        }
        pureCapacitanceWall = object : BatteryWall("pure-capacitance-wall") {
            init {
                requirements(Category.defense, ItemStack.with(
                    SqItems.crystallizedBeryllium, 3,
                    SqItems.pureSilicon, 6
                ))
                health = 110
                consumePowerBuffered(800f)
            }
        }
        pureCapacitanceWallLarge = object : BatteryWall("pure-capacitance-wall-large") {
            init {
                requirements(Category.defense, ItemStack.with(
                    SqItems.crystallizedBeryllium, 12,
                    SqItems.pureSilicon, 24
                ))
                size = 2
                health = 110 * 4
                consumePowerBuffered(3200f)
            }
        }
        intensifiedShieldedWall = object : SqShieldWall("intensified-shielded-wall") {
            init {
                requirements(Category.defense, ItemStack.with(
                    SqItems.phaseCore, 2,
                    SqItems.grainBoundaryAlloy, 4,
                    Items.surgeAlloy, 4
                ))
                health = 300
                armor = 15f
                consumePower(3f / 60f)
                chanceDeflect = 16f
                breakCooldown = 60f * 11f
            }
        }
        intensifiedShieldedWallLarge = object : SqShieldWall("intensified-shielded-wall-large") {
            init {
                requirements(Category.defense, ItemStack.with(
                    SqItems.phaseCore, 2,
                    SqItems.grainBoundaryAlloy, 12,
                    Items.surgeAlloy, 12
                ))
                size = 2
                health = 300 * 4
                armor = 60f
                consumePower(6f / 60f)
                chanceDeflect = 16f
                regenSpeed = 4f
                shieldHealth = 3600f
                breakCooldown = 60f * 17f
            }
        }
        // endregion

        multiFluidMixer = object : MultiCrafter("multi-fluid-mixer") {
            init {
                addFormula(
                    Formula(
                        ItemStack.with(SqItems.crystallizedBeryllium, 1),
                        LiquidStack.with(Liquids.water, 6),
                        1.5f,
                        ItemStack.empty,
                        LiquidStack.with(SqLiquids.crystallizedFluid, 5),
                        0f,
                        0.5f * 60f,
                        false
                    ),
                    Formula(
                        ItemStack.with(SqItems.grainBoundaryAlloy, 1),
                        LiquidStack.with(SqLiquids.crystallizedFluid, 6),
                        2f,
                        ItemStack.empty,
                        LiquidStack.with(SqLiquids.vectorizedFluid, 4.5),
                        0f,
                        1.2f * 60f,
                        false
                    )
                )
                requirements(
                    Category.crafting, ItemStack.with(
                        Items.graphite, 60,
                        SqItems.pureSilicon, 30,
                        SqItems.crystallizedBeryllium, 120,
                        Items.tungsten, 80
                    )
                )
                drawer = DrawMulti(
                    DrawBottom(),
                    NoCheckDrawLiquidRegion(),
                    DrawDefault()
                )
                scaledHealth = 65f
                craftEffect = Fx.pulverizeMedium
                size = 2
                itemCapacity = 20
                liquidCapacity = 60f
                ord = 34
            }
        }
        berylliumCrystallizer = object : MultiCrafter("beryllium-crystallizer") {
            init {
                addFormula(
                    Formula(
                        ItemStack.with(Items.beryllium, 4),
                        LiquidStack.empty,
                        1f,
                        ItemStack.with(SqItems.crystallizedBeryllium, 3),
                        LiquidStack.empty,
                        0f,
                        1.57f * 60f
                    )
                )
                onlyOneFormula = true
                requirements(
                    Category.crafting, ItemStack.with(
                        Items.graphite, 80,
                        Items.tungsten, 20,
                        Items.beryllium, 60
                    )
                )
                drawer = DrawMulti(
                    DrawBottom(),  // DrawEfficiency(),
                    DrawArcSmelt(),
                    DrawDefault()
                )
                scaledHealth = 73f
                size = 3
                itemCapacity = 30
            }
        }
        object : ItemTurret("acacac"), SeqElem {
            init {
                requirements(Category.turret, ItemStack.empty)
                ammo(
                    Items.copper, object : SpreadPointBulletType() {
                        init {
                            shootEffect = Fx.instShoot
                            hitEffect = Fx.instHit
                            smokeEffect = Fx.smokeCloud
                            trailEffect = Fx.instTrail
                            despawnEffect = Fx.instBomb
                            trailSpacing = 20f
                            splashDamage = 1350f
                            splashDamageRadius = 18f
                            buildingDamageMultiplier = 0.2f
                            speed = 600f
                            hitShake = 6f
                            ammoMultiplier = 1f
                            fragBullet = object : SpreadPointBulletType() {
                                init {
                                    shootEffect = Fx.instShoot
                                    hitEffect = Fx.instHit
                                    smokeEffect = Fx.smokeCloud
                                    trailEffect = Fx.instTrail
                                    despawnEffect = Fx.instBomb
                                    trailSpacing = 20f
                                    splashDamage = 1350f
                                    splashDamageRadius = 18f
                                    buildingDamageMultiplier = 0.2f
                                    speed = 300f
                                    hitShake = 6f
                                    ammoMultiplier = 1f
                                    lifetime = 1f
                                    fragBullet = object : PointBulletType() {
                                        init {
                                            shootEffect = Fx.instShoot
                                            hitEffect = Fx.instHit
                                            smokeEffect = Fx.smokeCloud
                                            trailEffect = Fx.instTrail
                                            despawnEffect = Fx.instBomb
                                            trailSpacing = 20f
                                            splashDamage = 1350f
                                            splashDamageRadius = 18f
                                            buildingDamageMultiplier = 0.2f
                                            speed = 300f
                                            hitShake = 6f
                                            ammoMultiplier = 1f
                                            lifetime = 1f
                                            fragBullets = 3
                                            fragBullet = object : BasicBulletType(16f, 0f, "shell") {
                                                init {
                                                    lifetime = 999f
                                                    splashDamage = 1350f
                                                    splashDamageRadius = 27f
                                                    homingPower = 0.3f
                                                    homingRange = 8000f
                                                    width = 13f
                                                    height = 19f
                                                    hitSize = 27f
                                                    shootEffect = MultiEffect(Fx.shootBigColor, Fx.colorSparkBig)
                                                    smokeEffect = Fx.shootBigSmoke
                                                    ammoMultiplier = 1f
                                                    pierce = true
                                                    pierceBuilding = true
                                                    trailColor = Pal.tungstenShot
                                                    backColor = trailColor
                                                    hitColor = backColor
                                                    frontColor = Color.white
                                                    trailWidth = 2.2f
                                                    trailLength = 11
                                                    despawnEffect = Fx.hitBulletColor
                                                    hitEffect = despawnEffect
                                                    rangeChange = 40f
                                                    buildingDamageMultiplier = 0.3f
                                                }
                                            }
                                        }
                                    }
                                    fragBullets = 5
                                    radius = 300f
                                    minRadius = 80f
                                }
                            }
                            fragBullets = 5
                            radius = 300f
                            minRadius = 80f
                        }
                    },
                    Items.lead, object : SpreadPointBulletType() {
                        init {
                            shootEffect = Fx.instShoot
                            hitEffect = Fx.instHit
                            smokeEffect = Fx.smokeCloud
                            trailEffect = Fx.instTrail
                            despawnEffect = Fx.instBomb
                            trailSpacing = 20f
                            splashDamage = 1350f
                            splashDamageRadius = 18f
                            buildingDamageMultiplier = 0.2f
                            speed = 600f
                            hitShake = 6f
                            ammoMultiplier = 1f
                            fragBullet = object : SpreadPointBulletType() {
                                init {
                                    shootEffect = Fx.instShoot
                                    hitEffect = Fx.instHit
                                    smokeEffect = Fx.smokeCloud
                                    trailEffect = Fx.instTrail
                                    despawnEffect = Fx.instBomb
                                    trailSpacing = 20f
                                    splashDamage = 1350f
                                    splashDamageRadius = 18f
                                    buildingDamageMultiplier = 0.2f
                                    speed = 300f
                                    hitShake = 6f
                                    ammoMultiplier = 1f
                                    lifetime = 1f
                                    fragBullet = object : PointBulletType() {
                                        init {
                                            shootEffect = Fx.instShoot
                                            hitEffect = Fx.instHit
                                            smokeEffect = Fx.smokeCloud
                                            trailEffect = Fx.instTrail
                                            despawnEffect = Fx.instBomb
                                            trailSpacing = 20f
                                            splashDamage = 1350f
                                            splashDamageRadius = 18f
                                            buildingDamageMultiplier = 0.2f
                                            speed = 300f
                                            hitShake = 6f
                                            ammoMultiplier = 1f
                                            lifetime = 1f
                                        }
                                    }
                                    fragBullets = 5
                                    radius = 300f
                                    minRadius = 80f
                                }
                            }
                            fragBullets = 5
                            radius = 300f
                            minRadius = 80f
                        }
                    },
                    Items.metaglass, object : SpreadPointBulletType() {
                        init {
                            shootEffect = Fx.instShoot
                            hitEffect = Fx.instHit
                            smokeEffect = Fx.smokeCloud
                            trailEffect = Fx.instTrail
                            despawnEffect = Fx.instBomb
                            trailSpacing = 20f
                            splashDamage = 1350f
                            splashDamageRadius = 18f
                            buildingDamageMultiplier = 0.2f
                            speed = 600f
                            hitShake = 6f
                            ammoMultiplier = 1f
                            fragBullet = object : PointBulletType() {
                                init {
                                    shootEffect = Fx.instShoot
                                    hitEffect = Fx.instHit
                                    smokeEffect = Fx.smokeCloud
                                    trailEffect = Fx.instTrail
                                    despawnEffect = Fx.instBomb
                                    trailSpacing = 20f
                                    splashDamage = 1350f
                                    splashDamageRadius = 18f
                                    buildingDamageMultiplier = 0.2f
                                    speed = 300f
                                    hitShake = 6f
                                    ammoMultiplier = 1f
                                    lifetime = 1f
                                }
                            }
                            fragBullets = 5
                            radius = 300f
                            minRadius = 80f
                        }
                    },
                    Items.graphite, object : PointBulletType() {
                        init {
                            shootEffect = Fx.instShoot
                            hitEffect = Fx.instHit
                            smokeEffect = Fx.smokeCloud
                            trailEffect = Fx.instTrail
                            despawnEffect = Fx.instBomb
                            trailSpacing = 20f
                            splashDamage = 1350f
                            splashDamageRadius = 18f
                            buildingDamageMultiplier = 0.2f
                            speed = 600f
                            hitShake = 6f
                            ammoMultiplier = 1f
                        }
                    }
                )
                shoot = ShootAlternate(3.5f)
                recoils = 2
                recoil = 0.5f
                shootY = 3f
                reload = 20f
                range = 600f
                shootCone = 15f
                ammoUseEffect = Fx.casing1
                health = Int.MAX_VALUE
                inaccuracy = 2f
                rotateSpeed = 10f
                coolant = consumeCoolant(0.1f)
                researchCostMultiplier = 0.05f
                limitRange()
            }

            override fun statValue() = null
        }
        if (!SeqMod.dev) return
        object : MultiCrafter("test-multi-crafter") {
            init {
                addFormula(
                    Formula(
                        ItemStack.with(Items.thorium, 2),
                        LiquidStack.with(Liquids.water, 5),
                        114f, ItemStack.with(SqItems.grainBoundaryAlloy, 11),
                        LiquidStack.with(Liquids.cryofluid, 7),
                        6f,
                        90f, false
                    ),
                    Formula(
                        ItemStack.with(SqItems.berylliumalAlloy, 2, Items.surgeAlloy, 3),
                        LiquidStack.with(SqLiquids.crystallizedFluid, 3, Liquids.water, 5),
                        3f,
                        ItemStack.with(SqItems.phaseCore, 5, SqItems.grainBoundaryAlloy, 10),
                        LiquidStack.with(SqLiquids.vectorizedFluid, 2, Liquids.cryofluid, 7),
                        5f,
                        8f, false
                    ),
                    Formula(
                        ItemStack.empty,
                        LiquidStack.empty,
                        3f,
                        ImagineEnergyRecord(5f, 10f, 0f, true),
                        ItemStack.empty,
                        LiquidStack.empty,
                        5f,
                        ImagineEnergyRecord(3f, 1f, 2f, false),
                        8f, false
                    )
                )
                requirements(Category.crafting, ItemStack.empty)
                craftEffect = Fx.pulverizeMedium
                size = 2
                hasItems = true
                hasLiquids = true
                hasPower = true
                itemCapacity = 100
                liquidCapacity = 100f
                ord = 11
            }
        }
        object : MultiCrafter("test-one-formula-crafter") {
            init {
                addFormula(
                    Formula(
                        ItemStack.with(Items.thorium, 2),
                        LiquidStack.with(Liquids.water, 5),
                        114f,
                        ItemStack.with(SqItems.grainBoundaryAlloy, 11),
                        LiquidStack.with(Liquids.cryofluid, 7),
                        6f,
                        90f, false
                    ),
                    Formula(
                        ItemStack.with(SqItems.berylliumalAlloy, 2, Items.surgeAlloy, 3),
                        LiquidStack.with(SqLiquids.crystallizedFluid, 3, Liquids.water, 5),
                        3f,
                        ItemStack.with(SqItems.phaseCore, 5, SqItems.grainBoundaryAlloy, 10),
                        LiquidStack.with(SqLiquids.vectorizedFluid, 2, Liquids.cryofluid, 7),
                        5f,
                        8f, false
                    ),
                    Formula(
                        ItemStack.empty,
                        LiquidStack.empty,
                        3f,
                        ImagineEnergyRecord(5f, 10f, 0f, true),
                        ItemStack.empty,
                        LiquidStack.empty,
                        5f,
                        ImagineEnergyRecord(3f, 1f, 2f, false),
                        8f, false
                    )
                )
                onlyOneFormula = true
                requirements(Category.crafting, ItemStack.empty)
                craftEffect = Fx.pulverizeMedium
                size = 2
                hasItems = true
                hasLiquids = true
                hasPower = true
                itemCapacity = 100
                liquidCapacity = 100f
            }
        }
        object : Wall("test2") {
            init {
                requirements(Category.effect, ItemStack.empty)
                health = 100
                update = true
                buildType = Prov {
                    object : WallBuild() {
                        override fun updateTile() {
                            for (len in 1..14) {
                                for (i in 0..3) {
                                    val tile1 = Vars.world.tile(
                                        Geometry.d4x(i) * len + tileX(),
                                        Geometry.d4y(i) * len + tileY()
                                    )
                                    if (tile1?.build != null && tile1.build is BuildingIEc) {
                                        if (Mathf.chanceDelta((tile1.block().size * 0.1f / len).toDouble())) Fx.healBlockFull.at(
                                            tile1.build.x,
                                            tile1.build.y,
                                            0f,
                                            SqColor.LiuDai.cpy().a(0.3f),
                                            tile1.build.block
                                        )
                                    }
                                }
                            }
                            super.updateTile()
                        }
                    }
                }
            }
        }

//        new ImagineNode("imagine-node") {{
//            requirements(Category.effect, ItemStack.empty);
//        }};
//        new ImagineCenter("imagine-center") {{
//            requirements(Category.effect, ItemStack.empty);
//        }};
    }
}
