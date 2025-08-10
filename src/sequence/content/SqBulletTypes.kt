package sequence.content

import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.math.Rand
import arc.math.geom.Vec2
import arc.scene.ui.ImageButton
import arc.scene.ui.layout.Collapser
import arc.scene.ui.layout.Table
import arc.struct.ObjectMap
import arc.util.Time
import mindustry.content.Fx
import mindustry.entities.Damage
import mindustry.entities.bullet.*
import mindustry.gen.Bullet
import mindustry.gen.Healthc
import mindustry.gen.Icon
import mindustry.gen.Teamc
import mindustry.graphics.Drawf
import mindustry.graphics.Layer
import mindustry.ui.Styles
import mindustry.world.meta.StatValue
import sequence.core.SqBundle
import sequence.core.SqStatValues
import sequence.graphic.SqColor
import sequence.util.IgnoredFragBullet
import sequence.util.SqDamage.trueDamage
import sequence.util.clearEffects
import sequence.util.modf
import sequence.util.register
import sequence.world.blocks.turret.WarmupItemTurret
import sequence.world.entities.AdditionInfoBulletType
import sequence.world.entities.SnipeBulletType
import sequence.world.entities.WarmupBulletType

object SqBulletTypes {
    lateinit var imagineEnergyPointSmall: BulletType

    lateinit var foreshadowGBA: BulletType

    lateinit var hailCB: BulletType

    lateinit var swarmerCB: BulletType

    lateinit var havocCB: BulletType
    lateinit var havocPC: BulletType

    lateinit var executor: BulletType

    lateinit var finality: BulletType

    lateinit var eternalNight: BulletType

    fun load() {
        foreshadowGBA = PointBulletType().register {
            shootEffect = Fx.instShoot
            hitEffect = SqFx.fgbaBomb
            smokeEffect = Fx.smokeCloud
            trailEffect = SqFx.fgbaTrail
            despawnEffect = SqFx.fgbaBomb
            trailSpacing = 20f
            damage = 1270f
            buildingDamageMultiplier = 0.2f
            speed = 100000f
            hitShake = 60f
            ammoMultiplier = 1f
            splashDamage = 284f
            splashDamagePierce = true
            splashDamageRadius = 40f
            rangeChange = 246f
            status = SqStatusEffects.brokenShield
            statusDuration = 4 * 60f

            fragBullets = 6
            fragBullet = BulletType().register {
                lifetime = 150f
                damage = 130f
                status = SqStatusEffects.brokenShield
                statusDuration = 30f
                pierce = true
                despawnEffect = Fx.none
                hitEffect = Fx.none
            }
        }
        imagineEnergyPointSmall = object : ImagineEnergyPointDrawBulletType(), AdditionInfoBulletType {
            override fun update(b: Bullet?) {
                super.update(b)
                if (b == null) return
                trueDamage(b.team, b.x, b.y, 24f, b.damage * Time.delta)
                Damage.damage(b.team, b.x, b.y, 24f, 0.01f)
            }

            override fun continuousDamage() = damage * 60f

            override val info: StatValue
                get() = StatValue { bt ->
                    SqStatValues.Bullets.trueDamage(bt, damage)
                    bt.row()
                }
        }.register {
            keepVelocity = false
            collides = false
            collidesGround = false
            collidesAir = false
            absorbable = false
            hittable = false
            speed = 0f

            clearEffects()
            despawnEffect = SqFx.iepsDespawn
        }
        executor = SnipeBulletType().register {
            speed = 999999f
            damage = 8000f
            lifetime = 720f
            trailEffect = SqFx.fgbaTrail
            trailSpacing = 20f
            hitEffect = SqFx.egbaHit
            despawnEffect = SqFx.iepsDespawn
            fragBullets = 3
            status = SqStatusEffects.brokenShield
            statusDuration = 5f * 60f

            fragBullet = BasicBulletType(6f, 700f).register {
                lifetime = 97.5f
                hitSize = 7f
                pierceCap = 8
                pierce = true
                homingPower = 1f
                homingRange = 999f
                pierceBuilding = true
                backColor = trailColor
                hitColor = backColor
                frontColor = Color.clear
                despawnEffect = Fx.hitBulletColor
                hitEffect = despawnEffect
                buildingDamageMultiplier = 1.2f
            }
        }
        havocCB = BulletType().register {
            clearEffects()

            lifetime = 0f
            damage = 0f
            speed = 0f

            fragBullets = 7
            fragRandomSpread = 0f
            fragSpread = 5f
            fragVelocityMin = 1f
            reloadMultiplier = 1f / 0.75f

            fragBullet = BasicBulletType(6f, 188f).register {
                lifetime = 62f
                width = 12f
                hitSize = 7f
                height = 20f
                smokeEffect = Fx.shootBigSmoke
                pierceCap = 12
                pierce = true
                pierceBuilding = true
                trailColor = SqColor.crystallizedBeryllium
                backColor = trailColor
                hitColor = backColor
                frontColor = Color.white
                trailWidth = 2.8f
                trailLength = 8
                despawnEffect = Fx.hitBulletColor
                hitEffect = despawnEffect
                buildingDamageMultiplier = 1.2f
            }
        }
        havocPC = BulletType().register {
            clearEffects()

            lifetime = 0f
            damage = 0f
            speed = 0f

            fragBullets = 5
            fragRandomSpread = 0f
            fragSpread = 3f
            fragVelocityMin = 1f
            reloadMultiplier = 1f / 0.57f

            fragBullet = BasicBulletType(9f, 243f).register {
                lifetime = 41f
                width = 11f
                hitSize = 12f
                height = 21f
                pierceCap = 12
                pierce = true
                pierceBuilding = true
                trailColor = SqColor.phaseCore
                backColor = trailColor
                hitColor = backColor
                frontColor = Color.white
                trailWidth = 2.8f
                trailLength = 12
                despawnEffect = Fx.hitBulletColor
                hitEffect = despawnEffect
                buildingDamageMultiplier = 1.33f

                status = SqStatusEffects.corrode
                statusDuration = 4f * 60f
            }
        }
        hailCB = ArtilleryBulletType(3f, 46f).register {
            knockback = 0.8f
            lifetime = 80f
            height = 11f
            width = height
            collidesTiles = false
            splashDamageRadius = 25f
            splashDamage = 33f
            pierce = true
            pierceCap = 2
            trailColor = SqColor.crystallizedBeryllium
            backColor = trailColor
            hitColor = backColor
            frontColor = SqColor.crystallizedBeryllium
            despawnEffect = Fx.hitBulletColor
        }
        swarmerCB = MissileBulletType(3.7f, 38f).register {
            width = 8f
            height = 8f
            shrinkY = 0f
            splashDamageRadius = 29f
            splashDamage = 25f * 1.8f
            hitEffect = Fx.blastExplosion
            despawnEffect = Fx.blastExplosion
            ammoMultiplier = 4f
            trailColor = SqColor.crystallizedBeryllium
            backColor = trailColor
            hitColor = backColor
            frontColor = SqColor.crystallizedBeryllium
        }
        eternalNight = ImagineEnergyPointDrawBulletType().register {
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
            intervalBullet = imagineEnergyPointSmall.copy().register {
                damage = 120f / 60f
                lifetime = 100f
            }

            fragBullets = 8
            fragBullet = ImagineEnergyPointDrawBulletType().register {
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
                intervalBullet = imagineEnergyPointSmall.copy().register {
                    damage = 98f / 60f
                    lifetime = 100f
                }

                fragBullets = 1
                fragBullet = imagineEnergyPointSmall.copy().register {
                    damage = 112f / 60f
                    lifetime = 150f
                }
            }
        }
        finality = (object : SnipeBulletType(), WarmupBulletType, AdditionInfoBulletType, IgnoredFragBullet {
            override var warmupBulletType: BulletType? = null
            override val info: StatValue
                get() = _info
            var _info = StatValue { }
            val trueDamage = 4000f
            override fun onHit(target: Teamc) {
                (target as? Healthc)?.trueDamage(trueDamage)
            }
        }).register {
            pierce = true
            pierceBuilding = true
            speed = 999999f
            damage = 12000f
            lifetime = 1200f
            trailEffect = SqFx.fgbaTrail
            trailSpacing = 20f
            despawnEffect = SqFx.egbaHit
            hitEffect = SqFx.finalityHit
            val warmupType = (object : BulletType(), IgnoredFragBullet {
                override fun draw(b: Bullet?) {
                    super.draw(b)
                    b ?: return
                    val fl = (b.time / 10f).coerceIn(0f, 1f)
                    Draw.color(trailColor)
                    Fill.circle(b.x, b.y, 2.4f * fl)
                    Draw.z(Layer.effect)
                    Draw.color(Color.black)
                    Fill.circle(b.x, b.y, 1.9f * fl)
                    Draw.reset()
                }
            }).register {
                clearEffects()
                trailColor = SqColor.imagineEnergy
                trailWidth = 1.4f
                trailLength = 18
                pierce = true
                pierceBuilding = true
                pierceCap = 3
                lifetime = 60f
                speed = 10f
                damage = 770f
                status = SqStatusEffects.entropyIncreasing
                statusDuration = 60f

                fragVelocityMin = 1f
                fragVelocityMax = 1f
                fragOffsetMin = 0f
                fragOffsetMax = 0f
                fragRandomSpread = 0f
                fragBullets = 1
                fragBullet = (object : BulletType(), IgnoredFragBullet {
                    override fun draw(b: Bullet?) {
                        super.draw(b)
                        b ?: return
                        Draw.color(trailColor)
                        Fill.circle(b.x, b.y, 2.4f)
                        Draw.z(Layer.effect)
                        Draw.color(Color.black)
                        Fill.circle(b.x, b.y, 1.9f)
                        Draw.reset()
                    }
                }).register {
                    trailColor = SqColor.imagineEnergy
                    trailWidth = 1.4f
                    trailLength = 18
                    pierce = true
                    pierceBuilding = true
                    pierceCap = 3
                    lifetime = 80f
                    speed = 12f
                    damage = 770f
                    status = SqStatusEffects.entropyIncreasing
                    statusDuration = 60f
                    homingPower = 0.12f
                    homingRange = 999999f
                }
            }
            _info = StatValue { bt ->
                SqStatValues.Bullets.trueDamage(bt, trueDamage)
                bt.row()
                val fc = Table()
                SqStatValues.ammo(
                    ObjectMap.of(SqItems.encapsulatedImagineEnergy, warmupType),
                    nested = true,
                    showUnit = false
                ).display(fc)
                val coll = Collapser(fc, true)
                coll.setDuration(0.1f)
                bt.table { ft: Table ->
                    ft.left().defaults().left()
                    ft.add(
                        SqBundle.format(
                            "bullet.warmup",
                            1f / ((SqBlocks.finality as WarmupItemTurret).shootNeededWarmup / 60f)
                        )
                    )
                    ft.button(
                        Icon.downOpen,
                        Styles.emptyi
                    ) { coll.toggle(false) }.update { i: ImageButton ->
                        i.style.imageUp =
                            if (!coll.isCollapsed) Icon.upOpen else Icon.downOpen
                    }.size(8f).padLeft(16f).expandX()
                }
                bt.row()
                bt.add(coll)
            }
            val radius = 80f
            warmupBulletType = object : BulletType() {
                override fun init(b: Bullet?) {
                    super.init(b)
                    (b ?: return).time = b.lifetime
                    rand.setSeed(b.id.toLong())
                    val vec = Vec2()
                        .trnsExact((rand.nextFloat() * 720f) modf 360f, (rand.nextFloat() * 160f) modf radius)
                        .add(Vec2().trnsExact(b.vel.angle(), -90f))
                        .add(b.x, b.y)
                    warmupType.create(
                        b.owner, b.team, vec.x, vec.y, b.vel.angle(), 1f,
                        (rand.nextFloat() modf 1f) + 1f, b.mover
                    )
                    b.remove()
                    b.vel.setZero()
                }
            }
        }
    }

    open class ImagineEnergyPointDrawBulletType : BulletType() {
        override fun draw(b: Bullet?) {
            super.draw(b)
            if (b == null) return
            Draw.color(SqColor.imagineEnergy)
            Fill.circle(b.x, b.y, 3f)
            Drawf.light(b.x, b.y, 2f, SqColor.imagineEnergy, 0.2f)
            Draw.reset()
        }
    }

    private val rand = Rand()
}
