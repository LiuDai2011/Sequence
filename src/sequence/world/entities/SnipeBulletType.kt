package sequence.world.entities

import arc.math.geom.Geometry
import arc.math.geom.Rect
import arc.math.geom.Vec2
import arc.struct.Seq
import arc.util.pooling.Pool.Poolable
import arc.util.pooling.Pools
import mindustry.Vars
import mindustry.Vars.tilesize
import mindustry.core.World
import mindustry.entities.Units
import mindustry.entities.bullet.BulletType
import mindustry.gen.Building
import mindustry.gen.Bullet
import mindustry.gen.Teamc
import sequence.util.MUnit

open class SnipeBulletType : BulletType() {
    var trailSpacing = 10f

    init {
        collides = false
        reflectable = false
        keepVelocity = false
        backMove = false
    }

    override fun init(b: Bullet) {
        super.init(b)
        val rot = b.rotation()
        val x = b.x
        val y = b.y

        vec.trnsExact(rot, b.lifetime)
        seg1.set(b.x, b.y)
        seg2.set(seg1).add(vec)
        World.raycastEachWorld(b.x, b.y, seg2.x, seg2.y) { cx, cy ->
            if (!b.type.collidesGround) return@raycastEachWorld false
            val bd = Vars.world.build(cx, cy)
            if (bd == null || bd.team == b.team || !bd.collide(b)) return@raycastEachWorld false
            collided.add(collidePool.obtain().set(cx * tilesize * 1f, cy * tilesize * 1f, bd))
            false
        }

        val expand = 3f
        rect.setPosition(x, y).setSize(vec.x, vec.y).normalize().grow(expand * 2f)
        val x2: Float = vec.x + x
        val y2: Float = vec.y + y
        Units.nearbyEnemies(b.team, rect) { u: MUnit ->
            if (u.checkTarget(b.type.collidesAir, b.type.collidesGround) && u.hittable()) {
                u.hitbox(hitrect)
                val vec = Geometry.raycastRect(x, y, x2, y2, hitrect.grow(expand * 2))
                if (vec != null) {
                    collided.add(collidePool.obtain().set(vec.x, vec.y, u))
                }
            }
        }

        collided.sort { it: Collided -> b.dst2(it.x, it.y) }
        if (!collided.isEmpty) {
            var pr = 0
            for (t in collided) {
                if (t.target is Building) {
                    (t.target as Building).collision(b)
                    hit(b, t.x, t.y)
                } else if (t.target is MUnit) {
                    b.collision(t.target as MUnit, t.x, t.y)
                }
                b.hit = false
                pr++
                if (!b.type.pierce || (b.type.pierceCap != -1 && pr >= b.type.pierceCap) || !b.type.pierceBuilding) break
            }
            if (pr != 0) { // 防止 pierce == 0
                val t = collided[pr - 1]
                b.type.despawnEffect.at(t.x, t.y, b.rotation())
                Geometry.iterateLine(0f, b.x, b.y, t.x, t.y, trailSpacing) { ix, iy ->
                    trailEffect.at(ix, iy, rot)
                }
            }
        } else {
            Geometry.iterateLine(0f, b.x, b.y, seg2.x, seg2.y, trailSpacing) { ix, iy ->
                trailEffect.at(ix, iy, rot)
            }
        }

        collidePool.freeAll(collided)
        collided.clear()
        b.hit = true
        b.remove()
        b.vel.setZero()
    }


    class Collided : Poolable {
        var x = 0f
        var y = 0f
        var target: Teamc? = null

        fun set(x: Float, y: Float, target: Teamc?): Collided {
            this.x = x
            this.y = y
            this.target = target
            return this
        }

        override fun reset() {
            target = null
        }
    }


    companion object {
        private val hitrect = Rect()
        private val rect = Rect()
        private val vec = Vec2()
        private val seg1 = Vec2()
        private val seg2 = Vec2()
        private val collided = Seq<Collided>()
        private val collidePool = Pools.get(Collided::class.java) { Collided() }
    }
}
