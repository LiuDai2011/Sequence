package sequence.content

import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.util.Time
import mindustry.content.Fx
import mindustry.entities.Damage
import mindustry.entities.bullet.BasicBulletType
import mindustry.entities.bullet.BulletType
import mindustry.entities.bullet.PointBulletType
import mindustry.gen.Bullet
import mindustry.graphics.Drawf
import sequence.graphic.SqColor
import sequence.util.SqDamage
import sequence.util.clearEffects
import sequence.util.register
import sequence.world.entities.SnipeBulletType

object SqBulletTypes {
    lateinit var imagineEnergyPointSmall: BulletType

    lateinit var foreshadowGBA: BulletType

    lateinit var havocCB: BulletType
    lateinit var havocPC: BulletType

    lateinit var executor: BulletType

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
        imagineEnergyPointSmall = object : ImagineEnergyPointDrawBulletType() {
            override fun update(b: Bullet?) {
                super.update(b)
                if (b == null) return
                SqDamage.trueDamage(b.team, b.x, b.y, 24f, b.damage * Time.delta)
                Damage.damage(b.team, b.x, b.y, 24f, 0.01f)
            }

            override fun continuousDamage() = damage * 60f
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
            fragBullets = 3

            fragBullet = BasicBulletType(6f, 700f).register {
                lifetime = 97.5f
                width = 12f
                hitSize = 7f
                height = 20f
                pierceCap = 8
                pierce = true
                homingPower = 1f
                homingRange = 999f
                pierceBuilding = true
                trailColor = SqColor.grainBoundaryAlloy[0]
                backColor = trailColor
                hitColor = backColor
                frontColor = Color.white
                trailWidth = 2.8f
                trailLength = 12
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
}
