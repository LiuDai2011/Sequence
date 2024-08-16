package Sequence.world.entities;

import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.entities.Units;
import mindustry.gen.Bullet;
import mindustry.gen.Posc;

public class SpreadPointBulletType extends OnHitRunPointBulletType {
    public float minRadius = 0f;
    public float radius = 80f;

    public SpreadPointBulletType() {
        super();
        onHit = b -> {
            float bx = b.x,
                    by = b.y;
            Seq<Posc> res = new Seq<>();
            Units.nearby(null, bx, by, radius, e -> {
                if(e.team == b.team || e.dead() || !e.hittable()) return;
                res.add(e);
            });
            Units.nearbyBuildings(bx, by, radius, build -> {
                if(build.dead() || build.team == b.team) return;
                res.add(build);
            });
            res.sort(e -> e.dst(bx, by));
            int count = 0;
            for (Posc e : res) {
                if (e.dst(bx, by) < minRadius) continue;
                if (++count > fragBullets) break;
                fragBullet.create(
                        b,
                        b.team,
                        bx,
                        by,
                        Mathf.angle(e.x() - bx, e.y() - by),
                        1,
                        1,
                        1,
                        null).vel.setLength(e.dst(bx, by));
            }
            if (res.isEmpty()) {
                for (; count < fragBullets; ++count) {
                    fragBullet.create(
                            b,
                            b.team,
                            bx,
                            by,
                            Mathf.random(0, 360),
                            1,
                            1,
                            1,
                            null).vel.setLength(Mathf.random(minRadius, radius));
                }
            } else {
                for (; count < fragBullets; ++count) {
                    fragBullet.create(
                            b,
                            b.team,
                            bx,
                            by,
                            Mathf.angle(res.first().x() - bx, res.first().y() - by),
                            1,
                            1,
                            1,
                            null).vel.setLength(res.first().dst(bx, by));
                }
            }
        };
    }

    @Override
    public void createFrags(Bullet b, float x, float y) {
    }
}
