package sequence.content

import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.util.Time
import mindustry.content.Fx
import mindustry.entities.Damage
import mindustry.entities.bullet.BulletType
import mindustry.entities.bullet.PointBulletType
import mindustry.gen.Bullet
import mindustry.graphics.Drawf
import sequence.graphic.SqColor
import sequence.util.SqDamage
import sequence.util.clearEffects
import sequence.util.register

object SqBulletTypes {
    lateinit var imagineEnergyPointSmall: BulletType
    lateinit var foreshadowGBA: BulletType

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
