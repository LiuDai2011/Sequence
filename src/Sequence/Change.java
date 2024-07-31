package Sequence;

import mindustry.entities.bullet.BulletType;

public class Change {
    public static BulletType damageMulti(BulletType type, float multi) {
        if (type == null) return null;
        type.damage *= multi;
        type.splashDamage *= multi;
        type.lightningDamage *= multi;
        if (type.fragBullets != 0 && type.fragBullet != null) {
            type.fragBullet = damageMulti(type.fragBullet.copy(), multi);
        }
        if (type.intervalDelay > 0 && type.intervalBullet != null) {
            type.intervalBullet = damageMulti(type.intervalBullet.copy(), multi);
        }
        if (type.lightning != 0 && type.lightningType != null) {
            type.lightningType = damageMulti(type.lightningType.copy(), multi);
        }
        return type;
    }

    public static BulletType knockbackMulti(BulletType type, float multi) {
        if (type == null) return null;
        type.knockback *= multi;
        if (type.fragBullets != 0 && type.fragBullet != null) {
            type.fragBullet = knockbackMulti(type.fragBullet.copy(), multi);
        }
        if (type.intervalDelay > 0 && type.intervalBullet != null) {
            type.intervalBullet = knockbackMulti(type.intervalBullet.copy(), multi);
        }
        if (type.lightning != 0 && type.lightningType != null) {
            type.lightningType = knockbackMulti(type.lightningType.copy(), multi);
        }
        return type;
    }


}
