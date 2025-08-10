package sequence.world.blocks.drill

import arc.func.Cons
import arc.func.Floatf
import arc.func.Prov
import arc.math.geom.Vec2
import arc.util.Time
import mindustry.gen.Groups
import sequence.util.invoke

class ImpulseDrill(name: String) : SqDrill(name) {
    var impulseRange: Floatf<Float> = Floatf { 90f * it }
    var impulsePower: Floatf<Float> = Floatf { 80f * it }

    init {
        buildType = Prov { ImpulseDrillBuild() }
    }

    inner class ImpulseDrillBuild : DrillBuild() {
        override fun updateTile() {
            super.updateTile()
            val speed = lastDrillSpeed * timeScale()
            val range = impulseRange(speed) * Time.delta
            val power = impulsePower(speed) * Time.delta
            Groups.unit.intersect(x - range, y - range, range * 2f, range * 2f, Cons {
                it ?: return@Cons
                if (it.team == team || !it.hittable() || it.dst(this) > range) return@Cons
                val vec = Vec2()
                it.impulse(vec.set(it).minus(Vec2(x, y)).nor().scl(-power))
            })
        }
    }
}
