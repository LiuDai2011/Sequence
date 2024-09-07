package sequence.util

import arc.func.Cons
import mindustry.entities.bullet.BulletType

object Change {
    fun bulletType(type: BulletType?, cons: Cons<BulletType?>): BulletType? {
        if (type == null) return null
        cons[type]
        type.apply {
            if (fragBullets != 0 && fragBullet != null) {
                fragBullet = bulletType(fragBullet.copy(), cons)
            }
            if (intervalDelay > 0 && intervalBullet != null) {
                intervalBullet = bulletType(intervalBullet.copy(), cons)
            }
            if (lightning != 0 && lightningType != null) {
                lightningType = bulletType(lightningType.copy(), cons)
            }
        }
        return type
    }
}
