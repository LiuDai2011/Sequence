package sequence.util

import arc.func.Cons
import arc.graphics.Color
import arc.graphics.g2d.PixmapRegion
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

    fun BulletType.change(cons: Cons<BulletType?>) = bulletType(this, cons)

    fun color(pixmap: PixmapRegion, cond: ColorBool, to: Int2Color): PixmapRegion {
        pixmap.pixmap.each { x, y ->
            if (x >= pixmap.x && x < pixmap.x + pixmap.width &&
                y >= pixmap.y && y < pixmap.y + pixmap.height &&
                cond(pixmap.pixmap[x, y])
            )
                pixmap.pixmap.set(x, y, to(x, y))
        }
        return pixmap
    }

    fun color(pixmap: PixmapRegion, from: Color, to: Color): PixmapRegion =
        color(pixmap, { it == from.rgba() }, { _, _ -> to })
}

typealias Int2Color = (Int, Int) -> Color
typealias ColorBool = (Int) -> Boolean
