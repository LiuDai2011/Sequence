package Sequence.world.util;

import arc.func.Cons;
import mindustry.entities.bullet.BulletType;

public class Change {
    public static BulletType bulletType(BulletType type, Cons<BulletType> cons) {
        if (type == null) return null;
        cons.get(type);
        if (type.fragBullets != 0 && type.fragBullet != null) {
            type.fragBullet = bulletType(type.fragBullet.copy(), cons);
        }
        if (type.intervalDelay > 0 && type.intervalBullet != null) {
            type.intervalBullet = bulletType(type.intervalBullet.copy(), cons);
        }
        if (type.lightning != 0 && type.lightningType != null) {
            type.lightningType = bulletType(type.lightningType.copy(), cons);
        }
        return type;
    }
}
