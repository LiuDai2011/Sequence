package sequence.content

import arc.func.Prov
import arc.graphics.Color
import mindustry.content.Blocks
import mindustry.content.Fx
import mindustry.content.Items
import mindustry.content.Liquids
import mindustry.entities.bullet.BasicBulletType
import mindustry.entities.bullet.PointBulletType
import mindustry.entities.effect.MultiEffect
import mindustry.entities.part.DrawPart.PartProgress
import mindustry.entities.part.RegionPart
import mindustry.entities.pattern.ShootAlternate
import mindustry.entities.pattern.ShootBarrel
import mindustry.gen.Building
import mindustry.graphics.Layer
import mindustry.graphics.Pal
import mindustry.type.Category
import mindustry.type.ItemStack
import mindustry.type.LiquidStack
import mindustry.world.Block
import mindustry.world.blocks.defense.turrets.ItemTurret
import mindustry.world.blocks.environment.OreBlock
import mindustry.world.draw.DrawArcSmelt
import mindustry.world.draw.DrawDefault
import mindustry.world.draw.DrawMulti
import sequence.SeqMod
import sequence.graphic.SqColor
import sequence.util.IgnoredLocalName
import sequence.util.IgnoredSequenceElementImpl
import sequence.util.clearEffects
import sequence.util.register
import sequence.world.blocks.defense.*
import sequence.world.blocks.distribution.SqDuct
import sequence.world.blocks.distribution.SqStackConveyor
import sequence.world.blocks.drill.SqDrill
import sequence.world.blocks.imagine.ImagineCenter
import sequence.world.blocks.imagine.ImagineNode
import sequence.world.blocks.production.MultiCrafter
import sequence.world.blocks.turrets.SqItemTurret
import sequence.world.draw.DrawBottom
import sequence.world.draw.DrawTurretGlow
import sequence.world.draw.NoCheckDrawLiquidRegion
import sequence.world.entities.SpreadPointBulletType
import sequence.world.meta.Formula
import sequence.world.meta.MultiBlockSchematic
import sequence.world.meta.PlaceHolderDrawer

object SqBlocks {
    lateinit var siliconOre: Block

    lateinit var multiFluidMixer: Block
    lateinit var berylliumCrystallizer: Block
    lateinit var pureSiliconSmelter: Block

    lateinit var berylliumalAlloyWall: Block
    lateinit var berylliumalAlloyWallLarge: Block
    lateinit var grainBoundaryAlloyWall: Block
    lateinit var grainBoundaryAlloyWallLarge: Block
    lateinit var pureCapacitanceWall: Block
    lateinit var pureCapacitanceWallLarge: Block
    lateinit var intensifiedShieldedWall: Block
    lateinit var intensifiedShieldedWallLarge: Block

    lateinit var executor: Block
    lateinit var havoc: Block
    lateinit var eternalNight: Block

    lateinit var extractor: Block

    lateinit var grainBoundaryAlloyConveyor: Block
    lateinit var crystallizationDuct: Block

    fun load() {
        // region environment
        siliconOre = object : OreBlock(Items.silicon), IgnoredSequenceElementImpl, IgnoredLocalName {}.register {
            oreThreshold = 0.6812f
            oreScale = 15.75345f
            itemDrop.hardness = 4
        }
        // endregion
        // region defense
        berylliumalAlloyWall = SqWall("berylliumal-alloy-wall").register {
            requirements(Category.defense, ItemStack.with(SqItems.berylliumalAlloy, 6))
            health = 230
            absorbLasers = true
        }
        berylliumalAlloyWallLarge = SqWall("berylliumal-alloy-wall-large").register {
            requirements(Category.defense, ItemStack.with(SqItems.berylliumalAlloy, 24))
            size = 2
            health = 230 * 4
            absorbLasers = true
        }
        grainBoundaryAlloyWall = UnionWall("grain-boundary-alloy-wall").register {
            requirements(Category.defense, ItemStack.with(SqItems.grainBoundaryAlloy, 6))
            health = 280
        }
        grainBoundaryAlloyWallLarge = UnionWall("grain-boundary-alloy-wall-large").register {
            requirements(Category.defense, ItemStack.with(SqItems.grainBoundaryAlloy, 24))
            size = 2
            health = 280 * 4
            apportionedSpeed = 0.017f
        }
        pureCapacitanceWall = BatteryWall("pure-capacitance-wall").register {
            requirements(
                Category.defense, ItemStack.with(
                    SqItems.crystallizedBeryllium, 3,
                    SqItems.pureSilicon, 6
                )
            )
            health = 110
            absorbLasers = true
            consumePowerBuffered(800f)
        }
        pureCapacitanceWallLarge = BatteryWall("pure-capacitance-wall-large").register {
            requirements(
                Category.defense, ItemStack.with(
                    SqItems.crystallizedBeryllium, 12,
                    SqItems.pureSilicon, 24
                )
            )
            size = 2
            health = 110 * 4
            absorbLasers = true
            consumePowerBuffered(3200f)
        }
        intensifiedShieldedWall = SqShieldWall("intensified-shielded-wall").register {
            requirements(
                Category.defense, ItemStack.with(
                    SqItems.phaseCore, 2,
                    SqItems.grainBoundaryAlloy, 4,
                    Items.surgeAlloy, 4
                )
            )
            health = 300
            armor = 15f
            consumePower(3f / 60f)
            chanceDeflect = 16f
            breakCooldown = 60f * 11f
        }
        intensifiedShieldedWallLarge = SqShieldWall("intensified-shielded-wall-large").register {
            requirements(
                Category.defense, ItemStack.with(
                    SqItems.phaseCore, 2,
                    SqItems.grainBoundaryAlloy, 12,
                    Items.surgeAlloy, 12
                )
            )
            size = 2
            health = 300 * 4
            armor = 60f
            consumePower(6f / 60f)
            chanceDeflect = 16f
            regenSpeed = 4f
            shieldHealth = 3600f
            breakCooldown = 60f * 17f
        }
        // endregion
        // region turret
        havoc = SqItemTurret("havoc").register {
            requirements(Category.turret, ItemStack.empty)
            ammo(
                SqItems.crystallizedBeryllium,
                SqBulletTypes.havocCB,
                SqItems.phaseCore,
                SqBulletTypes.havocPC
            )
            shoot = ShootBarrel().register {
                barrels = floatArrayOf(
                    -4f, 4f, 0f,
                    4f, 4f, 0f
                )
                shots = 2
                shotDelay = 0f
            }
            maxAmmo = 20
            ammoPerShot = 4
            size = 4
            scaledHealth = 277f
            reload = 66f
            inaccuracy = 2f
            shootCone = 0.0001f
            rotateSpeed = 7f
            range = 345f
            shootY = 11.5f

            coolant = consumeCoolant(0.1f)

            itemCapacity = 50
            drawer = DrawTurretGlow("seq-e0-", SqColor.imagineEnergy, 0.45f)
        }
        executor = SqItemTurret("executor").register {
            requirements(
                Category.turret, ItemStack.with(
                    SqItems.crystallizedBeryllium, 300,
                    SqItems.berylliumalAlloy, 380,
                    SqItems.pureSilicon, 300,
                    SqItems.phaseCore, 25,
                    SqItems.grainBoundaryAlloy, 80,
                    SqItems.vectorizedChip, 8
                )
            )
            ammo(
                SqItems.grainBoundaryAlloy,
                SqBulletTypes.executor
            )
            maxAmmo = 36
            ammoPerShot = 6
            size = 4
            scaledHealth = 280f
            reload = 85.7f
            inaccuracy = 0f
            shootCone = 0.0001f
            rotateSpeed = 1.5f
            range = 675f
            shootY = 11.5f

            coolant = consumeCoolant(0.1f)
            coolantMultiplier = 4f

            itemCapacity = 90
            drawer = DrawTurretGlow("seq-e0-", SqColor.imagineEnergy, 0.45f)
        }
        eternalNight = SqItemTurret("eternal-night").register {
            ord = 19
            requirements(
                Category.turret, ItemStack.with(
                    SqItems.berylliumalAlloy, 480,
                    SqItems.pureSilicon, 750,
                    SqItems.phaseCore, 45,
                    SqItems.grainBoundaryAlloy, 120,
                    SqItems.vectorizedChip, 8
                )
            )
            ammo(
                SqItems.encapsulatedImagineEnergy,
                SqBulletTypes.ImagineEnergyPointDrawBulletType().register {
                    clearEffects()

                    damage = 80f
                    speed = 6f
                    pierce = true
                    ammoMultiplier = 1f
                    lifetime = 120f

                    trailColor = SqColor.imagineEnergy
                    trailLength = 12
                    trailEffect = Fx.none

                    homingPower = 0.09f
                    homingRange = 500f

                    intervalDelay = 12f
                    intervalBullets = 1
                    intervalBullet =
                        SqBulletTypes.imagineEnergyPointSmall.copy().register {
                            damage = 120f / 60f
                            lifetime = 100f
                        }

                    fragBullets = 8
                    fragBullet = SqBulletTypes.ImagineEnergyPointDrawBulletType().register {
                        clearEffects()

                        damage = 200f
                        speed = 6f
                        pierce = true
                        ammoMultiplier = 8f

                        trailColor = SqColor.imagineEnergy
                        trailLength = 12
                        trailEffect = Fx.none

                        homingPower = 0.06f
                        homingRange = 100f

                        intervalDelay = 12f
                        intervalBullets = 1
                        intervalBullet =
                            SqBulletTypes.imagineEnergyPointSmall.copy().register {
                                damage = 98f / 60f
                                lifetime = 100f
                            }

                        fragBullets = 1
                        fragBullet =
                            SqBulletTypes.imagineEnergyPointSmall.copy().register {
                                damage = 112f / 60f
                                lifetime = 150f
                            }
                    }
                }
            )
            size = 4
            scaledHealth = 320f
            reload = 80f
            inaccuracy = 2f
            rotateSpeed = 2f
            shootCone = 2f
            range = 640f

            coolant = consumeCoolant(0.1f)
            coolantMultiplier = 3.8f

            itemCapacity = 25
            drawer = DrawTurretGlow("seq-e0-", SqColor.imagineEnergy, 0.55f).register {
                parts.addAll(
                    RegionPart("-glow").register {
                        progress = PartProgress.warmup
                        color = Color.clear
                        colorTo = SqColor.imagineEnergy
                        outline = false
                        mirror = false
                        layer = Layer.effect
                    }
                )
            }
        }
        // endregion
        // region factory
        multiFluidMixer = MultiCrafter("multi-fluid-mixer").register {
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
        berylliumCrystallizer = MultiCrafter("beryllium-crystallizer").register {
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
                DrawBottom(),
                DrawArcSmelt(),
                DrawDefault()
            )
            scaledHealth = 73f
            size = 3
            itemCapacity = 30
        }
        pureSiliconSmelter = MultiCrafter("pure-silicon-smelter").register {
            addFormula(
                Formula(
                    ItemStack.with(Items.silicon, 2),
                    LiquidStack.with(Liquids.water, 6f),
                    2f,
                    ItemStack.with(SqItems.pureSilicon, 1),
                    LiquidStack.empty,
                    0f,
                    0.9f * 60f
                )
            )
            onlyOneFormula = true
            requirements(
                Category.crafting, ItemStack.with(
                    Items.graphite, 80,
                    Items.silicon, 100,
                    Items.tungsten, 100,
                    SqItems.crystallizedBeryllium, 80
                )
            )
            drawer = DrawMulti(
                DrawBottom(),
                DrawArcSmelt(),
                DrawDefault()
            )
            scaledHealth = 62f
            size = 3
            itemCapacity = 40
        }
        // endregion
        // region drill
        extractor = SqDrill("extractor").register {
            requirements(
                Category.production,
                ItemStack.empty
            )
            hardnessDrillMultiplier = -4f
            drillTime = 75f
            size = 5
            drawRim = true
            hasPower = true
            tier = Int.MAX_VALUE
            updateEffect = Fx.pulverizeRed
            updateEffectChance = 0.03f
            drillEffect = Fx.mineHuge
            rotateSpeed = 6f
            warmupSpeed = 0.005f
            itemCapacity = 200

            liquidBoostIntensity = 1.95f

            consumePower(750f / 60f)
            consumeLiquid(Liquids.water, 0.4f).boost()
        }
        // endregion
        // region distribution
        crystallizationDuct = SqDuct("crystallization-duct").register {
            requirements(Category.distribution, ItemStack.with(Items.beryllium, 1, SqItems.crystallizedBeryllium, 1))
            health = 148
            speed = 2.8f
        }
        grainBoundaryAlloyConveyor = SqStackConveyor("grain-boundary-alloy-conveyor").register {
            requirements(
                Category.distribution,
                ItemStack.with(Items.surgeAlloy, 1, Items.tungsten, 1, SqItems.grainBoundaryAlloy, 1)
            )
            health = 280
            speed = 11f / 60f
            itemCapacity = 35

            underBullets = true
            baseEfficiency = 1f
        }
        // endregion
        if (!SeqMod.dev) return
        // region dev
        object : ItemTurret("acacacacac"), IgnoredLocalName, IgnoredSequenceElementImpl {
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
        }
        object : MultiCrafter("test-multi-crafter"), IgnoredLocalName {
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
//                    Formula(
//                        ItemStack.empty,
//                        LiquidStack.empty,
//                        3f,
//                        ImagineEnergyRecord(5f, 10f, 0f, true),
//                        ItemStack.empty,
//                        LiquidStack.empty,
//                        5f,
//                        ImagineEnergyRecord(3f, 1f, 2f, false),
//                        8f, false
//                    )
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
        object : MultiCrafter("test-one-formula-crafter"), IgnoredLocalName {
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
//                    Formula(
//                        ItemStack.empty,
//                        LiquidStack.empty,
//                        3f,
//                        ImagineEnergyRecord(5f, 10f, 0f, true),
//                        ItemStack.empty,
//                        LiquidStack.empty,
//                        5f,
//                        ImagineEnergyRecord(3f, 1f, 2f, false),
//                        8f, false
//                    )
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
//        new ImagineNode("imagine-node") {{
//            requirements(Category.effect, ItemStack.empty);
//        }};
//        new ImagineCenter("imagine-center") {{
//            requirements(Category.effect, ItemStack.empty);
//        }};
        object : ImagineNode("imagine-node"), IgnoredLocalName, IgnoredSequenceElementImpl {
            init {
                requirements(Category.effect, ItemStack.empty)
            }
        }
        object : ImagineCenter("imagine-center"), IgnoredLocalName, IgnoredSequenceElementImpl {
            init {
                requirements(Category.effect, ItemStack.empty)
                size = 4
            }
        }
        object : BulletEnhancer("b-e"), IgnoredLocalName, IgnoredSequenceElementImpl {
            init {
                requirements(Category.effect, ItemStack.empty)
                size = 4
            }
        }
        object : Block("test-multi-block"), IgnoredLocalName, IgnoredSequenceElementImpl {
            init {
                val mbs = MultiBlockSchematic.of(
                    0, 0, Blocks.arc,
                    1, 0, Blocks.battery,
                    0, 1, Blocks.powerNode,
                    1, 1, Blocks.copperWall,
                    3, 3, Blocks.airFactory
                )
//                val mbs =
//                    MultiBlockSchematic.readBase64("bWJzaHicY2AAAxYGroL88tQi3bz8lFQG7uT8ggIgpzwxJ4eBObEomYE9KbGkJLWoEqwUAhihJCOUxcQAA8wQEQAo+AzJ")
                requirements(Category.effect, ItemStack.empty)
                update = true
                drawDisabled = false
                buildType = Prov {
                    object : Building() {
                        override fun updateTile() {
                            super.updateTile()
                            PlaceHolderDrawer(mbs, tileX() - 20, tileY() - 20)
                        }
                    }
                }
            }
        }
        // endregion
    }
}
