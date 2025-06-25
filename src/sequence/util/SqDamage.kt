package sequence.util

import arc.Events
import arc.func.Boolf
import arc.func.Cons
import arc.math.Mathf
import arc.math.geom.Point2
import arc.math.geom.Rect
import arc.math.geom.Vec2
import arc.struct.IntFloatMap
import arc.util.Nullable
import arc.util.Time
import mindustry.Vars
import mindustry.core.World
import mindustry.entities.Units
import mindustry.game.EventType
import mindustry.game.EventType.UnitBulletDestroyEvent
import mindustry.game.EventType.UnitDamageEvent
import mindustry.game.Team
import mindustry.gen.Building
import mindustry.gen.Bullet
import mindustry.gen.Call
import mindustry.gen.Healthc
import mindustry.world.blocks.storage.CoreBlock.CoreBuild

object SqDamage {
    fun Healthc.trueDamage(amount: Float) {
        health(health() - amount)
        damage(0.00001f)
    }

    fun Building.trueDamage(amount: Float, withEffect: Boolean) {
        val pre: Float = this.hitTime
        damage(amount)
        if (!withEffect) {
            this.hitTime = pre
        }
    }

    fun Building.trueDamage(source: Team?, damage: Float) {
        trueDamage(damage)
        healthChanged()
        if (health <= 0f) {
            Call.buildDestroyed(this)
        }
    }

    fun CoreBuild.trueDamage(source: Team?, damage: Float) {
        if (iframes > 0) return
        if (source != null && source !== team) {
            lastDamage = source
        }
        trueDamage(damage)
    }

    fun Building.trueDamage(bullet: Bullet, source: Team?, damage: Float) {
        trueDamage(source, damage)
        Events.fire(Building.bulletDamageEvent.set(this, bullet))
    }

    fun trueDamageUnits(
        team: Team?, x: Float, y: Float, size: Float, damage: Float, predicate: Boolf<MUnit?>,
        acceptor: Cons<MUnit?>
    ) {
        val cons = Cons { entity: MUnit ->
            if (!predicate[entity] || !entity.hittable()) return@Cons
            entity.hitbox(hitrect)
            if (!hitrect.overlaps(rect)) return@Cons
            entity.trueDamage(damage)
            acceptor[entity]
        }
        rect.setSize(size * 2).setCenter(x, y)
        if (team != null) {
            Units.nearbyEnemies(team, rect, cons)
        } else {
            Units.nearby(rect, cons)
        }
    }

    fun trueDamage(x: Float, y: Float, radius: Float, damage: Float) =
        trueDamage(null, x, y, radius, damage, false)

    fun trueDamage(team: Team?, x: Float, y: Float, radius: Float, damage: Float) =
        trueDamage(team, x, y, radius, damage, false)

    fun trueDamage(team: Team?, x: Float, y: Float, radius: Float, damage: Float, air: Boolean, ground: Boolean) =
        trueDamage(team, x, y, radius, damage, false, air, ground)

    fun trueDamage(team: Team?, x: Float, y: Float, radius: Float, damage: Float, complete: Boolean) =
        trueDamage(team, x, y, radius, damage, complete, true, true)

    fun trueDamage(
        team: Team?, x: Float, y: Float, radius: Float, damage: Float, complete: Boolean, air: Boolean,
        ground: Boolean
    ) =
        trueDamage(team, x, y, radius, damage, complete, air, ground, false, null)

    fun trueDamage(
        team: Team?, x: Float, y: Float, radius: Float, damage: Float, complete: Boolean, air: Boolean,
        ground: Boolean, scaled: Boolean, @Nullable source: Bullet?
    ) {
        val cons = Cons { unit: MUnit ->
            if (unit.team === team || !unit.checkTarget(air, ground) || !unit.hittable()
                || !unit.within(x, y, radius + if (scaled) unit.hitSize / 2f else 0f)
            )
                return@Cons
            val dead = unit.dead
            val amount = calculateDamage(
                if (scaled) Math.max(0f, unit.dst(x, y) - unit.type.hitSize / 2)
                else unit.dst(x, y), radius, damage
            )
            unit.trueDamage(amount)
            if (source != null) {
                Events.fire(bulletDamageEvent.set(unit, source))
                unit.controller().hit(source)
                if (!dead && unit.dead) {
                    Events.fire(UnitBulletDestroyEvent(unit, source))
                }
            }
            val dst = vec.set(unit.x - x, unit.y - y).len()
            unit.vel.add(vec.setLength((if (radius > 0f) 1f - dst / radius else 1f) * 2f / unit.mass()))
            if (complete && damage >= 9999999f && unit.isPlayer) {
                Events.fire(EventType.Trigger.exclusionDeath)
            }
        }
        rect.setSize(radius * 2).setCenter(x, y)
        if (team != null) {
            Units.nearbyEnemies(team, rect, cons)
        } else {
            Units.nearby(rect, cons)
        }
        if (ground) {
            if (!complete) {
                trueTileDamage(
                    team,
                    World.toTile(x),
                    World.toTile(y),
                    radius / Vars.tilesize,
                    damage * (source?.type?.buildingDamageMultiplier
                        ?: 1f),
                    source
                )
            } else {
                completeDamage(team, x, y, radius, damage * (source?.type?.buildingDamageMultiplier ?: 1f))
            }
        }
    }

    fun trueTileDamage(team: Team?, x: Int, y: Int, baseRadius: Float, damage: Float) =
        trueTileDamage(team, x, y, baseRadius, damage, null)

    fun trueTileDamage(team: Team?, x: Int, y: Int, baseRadius: Float, damage: Float, @Nullable source: Bullet?) {
        Time.run(0f) {
            val `in` = Vars.world.build(x, y)
            if (`in` != null && `in`.team !== team && `in`.block.size > 1 && `in`.health > damage) {
                val d = damage * Math.min(`in`.block.size.toFloat(), baseRadius * 0.4f)
                if (source != null) {
                    `in`.trueDamage(source, team, d)
                } else {
                    `in`.trueDamage(team, d)
                }
                return@run
            }

            val radius = Math.min(baseRadius, 100f)
            val rad2 = radius * radius
            val rays = Mathf.ceil(radius * 2 * Mathf.pi)
            val spacing = Math.PI * 2.0 / rays
            damages.clear()

            for (i in 0..rays) {
                var dealt = 0f
                var startX = x
                var startY = y
                val endX = x + (Math.cos(spacing * i) * radius).toInt()
                val endY = y + (Math.sin(spacing * i) * radius).toInt()
                val xDist = Math.abs(endX - startX)
                val yDist = -Math.abs(endY - startY)
                val xStep = if (startX < endX) +1 else -1
                val yStep = if (startY < endY) +1 else -1
                var error = xDist + yDist
                while (startX != endX || startY != endY) {
                    val build = Vars.world.build(startX, startY)
                    if (build != null && build.team !== team) {
                        val edgeScale = 0.6f
                        val mult = (1f - Mathf.dst2(
                            startX.toFloat(),
                            startY.toFloat(),
                            x.toFloat(),
                            y.toFloat()
                        ) / rad2 + edgeScale) / (1f + edgeScale)
                        val next = damage * mult - dealt
                        val p = Point2.pack(startX, startY)
                        damages.put(p, damages[p].coerceAtLeast(next))
                        dealt += build.health
                        if (next - dealt <= 0) {
                            break
                        }
                    }
                    if (2 * error - yDist > xDist - 2 * error) {
                        error += yDist
                        startX += xStep
                    } else {
                        error += xDist
                        startY += yStep
                    }
                }
            }

            for (e in damages) {
                val cx = Point2.x(e.key).toInt()
                val cy = Point2.y(e.key).toInt()
                val build = Vars.world.build(cx, cy)
                if (build != null) {
                    if (source != null) {
                        build.trueDamage(source, team, e.value)
                    } else {
                        build.trueDamage(team, e.value)
                    }
                }
            }
        }
    }

    private val rect = Rect()
    private val hitrect = Rect()
    private val vec = Vec2()
    private val damages = IntFloatMap()

    private fun completeDamage(team: Team?, x: Float, y: Float, radius: Float, damage: Float) {
        val trad = (radius / Vars.tilesize).toInt()
        for (dx in -trad..trad) {
            for (dy in -trad..trad) {
                val tile = Vars.world.tile(Math.round(x / Vars.tilesize) + dx, Math.round(y / Vars.tilesize) + dy)
                if (tile?.build != null && (team == null || team !== tile.team()) && dx * dx + dy * dy <= trad * trad) {
                    tile.build.damage(team, damage)
                }
            }
        }
    }

    private fun calculateDamage(dist: Float, radius: Float, damage: Float): Float {
        val falloff = 0.4f
        val scaled = if (radius <= 0.00001f) 1f else Mathf.lerp(1f - dist / radius, 1f, falloff)
        return damage * scaled
    }

    private val bulletDamageEvent = UnitDamageEvent()
}