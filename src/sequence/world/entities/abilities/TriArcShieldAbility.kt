package sequence.world.entities.abilities

import arc.func.Cons
import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Lines
import arc.math.Mathf
import arc.math.geom.Vec2
import mindustry.Vars
import mindustry.entities.abilities.Ability
import mindustry.gen.Groups
import mindustry.graphics.Layer
import sequence.util.MUnit

class TriArcShieldAbility : Ability() {
    override fun draw(unit: MUnit?) {
        unit ?: return
        val alpha = 0.5f
        val x = 1.0f
        val y = 0.0f
        val width = 20f
        val widthScale = 1.5f
        val angle = 60f
        val radius = 120f

        Draw.z(Layer.shields)

        Draw.color(unit.team.color ?: unit.type.shieldColor(unit), Color.white, Mathf.clamp(alpha))
        val pos = Vec2().set(x, y).rotate(unit.rotation - 90f).add(unit)

        if (!Vars.renderer.animateShields) {
            Draw.alpha(0.4f)
        }

        Lines.stroke(width * widthScale)
        Lines.arc(pos.x, pos.y, radius, angle / 360f, unit.rotation - angle / 2f)
        Lines.arc(pos.x, pos.y, radius, angle / 360f, unit.rotation + 120f - angle / 2f)
        Lines.arc(pos.x, pos.y, radius, angle / 360f, unit.rotation - 120f - angle / 2f)
        Draw.reset()
    }

    override fun update(unit: MUnit?) {
        unit ?: return
        val radius = 150f
        Groups.bullet.intersect(unit.x - radius, unit.y - radius, 2f * radius, 2f * radius, Cons {
            if (it.dst(unit) <= radius && it.team != unit.team) {
                val len = it.vel.len()
                it.vel.set(unit).sub(it).setLength(len).scl(-1f)
            }
        })
    }

    companion object {
        private var paramUnit: MUnit? = null
    }
}