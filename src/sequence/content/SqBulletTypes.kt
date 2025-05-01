package sequence.content

import mindustry.content.Fx
import mindustry.entities.bullet.BulletType
import mindustry.entities.bullet.PointBulletType
import sequence.util.register

object SqBulletTypes {
    lateinit var foreshadowGBA: BulletType

    fun load() {
        foreshadowGBA = PointBulletType().register<PointBulletType> {
            shootEffect = Fx.instShoot
            hitEffect = SqFx.fgbaBomb
            smokeEffect = Fx.smokeCloud
            trailEffect = SqFx.fgbaTrail
            despawnEffect = SqFx.fgbaBomb
            trailSpacing = 20f
            damage = 127f
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
    }
}
