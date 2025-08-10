package sequence.content

import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.graphics.g2d.Lines
import arc.math.Angles
import arc.math.Mathf
import arc.math.Rand
import arc.math.geom.Vec2
import mindustry.Vars
import mindustry.entities.Effect
import mindustry.entities.effect.ParticleEffect
import mindustry.graphics.Drawf
import sequence.graphic.SqColor
import sequence.util.register

object SqFx {
    val fgbaTrail = ParticleEffect().register {
        clip = 40f
        particles = 1
        baseLength = 0.1f
        length = 0.1f
        line = true
        randLength = false
        lenFrom = 22f
        lenTo = 22f
        strokeFrom = 8f
        strokeTo = 0f
        cone = 0f
        colorFrom = SqColor.grainBoundaryAlloy[0]
        colorTo = SqColor.grainBoundaryAlloy[2]
    }
    val fgbaBomb = Effect(15f, 100f) { e ->
        Draw.color(SqColor.grainBoundaryAlloy[1])
        Draw.alpha(e.foutpow())
        Fill.circle(e.x, e.y, 9f)
        Draw.alpha(1f)
        Lines.stroke(e.fout() * 4f)
        Lines.circle(e.x, e.y, 4f + e.finpow() * 20f)
        Drawf.light(e.x, e.y, 150f, SqColor.grainBoundaryAlloy[0], 0.9f * e.fout())
    }

    // region EGBAHit
    data class EGBAHitData(val depth: Int, var created: Boolean = false)

    private var egbaHit_: Effect? = null
    val egbaHit get() = egbaHit_!!

    init {
        egbaHit_ = Effect(40f) { e ->
            if (e.data == null && !Vars.state.isPaused) {
                e.lifetime = 0f
                e.time = Float.MAX_VALUE
                egbaHit_!!.at(e.x, e.y, e.rotation, e.color, EGBAHitData(4))
                return@Effect
            } else e.data ?: return@Effect

            rand.setSeed(e.id.toLong())
            val depth = (e.data as EGBAHitData).depth
            val baseVec = Vec2(e.x, e.y)
            val arr = Array(depth) { i ->
                Vec2().trnsExact(
                    e.rotation + ((i - depth / 2) * 0.05f * (depth + 1) + (rand.nextFloat() - 0.5f) * 0.3f) * Mathf.radiansToDegrees,
                    (depth + 1).toFloat() * 8f + (rand.nextFloat() - 0.5f) * 9f
                ).add(baseVec)
            }

            if (!(e.data as EGBAHitData).created && e.time / e.lifetime > 0.035f && !Vars.state.isPaused) {
                for (i in 0..<depth) {
                    val vec = arr[i]
                    egbaHit_!!.at(vec.x, vec.y, vec.cpy().minus(baseVec).angle(), EGBAHitData(depth - 1))
                }
                (e.data as EGBAHitData).created = true
            }

            Lines.stroke((depth + 1) * 0.9f * e.foutpow(), SqColor.grainBoundaryAlloy[0])
            for (i in 0..<depth) {
                val vec = arr[i]
                Lines.line(e.x, e.y, vec.x, vec.y)
            }
        }
    }

    // endregion
    val iepsDespawn = Effect(40f) { e ->
        Draw.color(SqColor.imagineEnergy)
        Draw.alpha(e.fout())
        Fill.circle(e.x, e.y, 3f * e.foutpow())
        Drawf.light(e.x, e.y, 2f * e.foutpow(), SqColor.imagineEnergy, 0.2f * e.fout())
        Draw.reset()
    }
    val finalityHit = Effect(35f, 100f) { e ->
        Draw.color(SqColor.imagineEnergy)
        Lines.stroke(e.fout() * 4f)
        Lines.circle(e.x, e.y, 4f + e.finpow() * 20f)
        for (i in 0..<4) {
            Drawf.tri(e.x, e.y, 6f, 80f * e.fout(), (i * 90).toFloat())
        }
        Draw.color()
        for (i in 0..<4) {
            Drawf.tri(e.x, e.y, 3f, 30f * e.fout(), (i * 90).toFloat())
        }
        Drawf.light(e.x, e.y, 150f, SqColor.imagineEnergy, 0.9f * e.fout())
    }

    val corrode = Effect(40f) { e ->
        Draw.color(SqColor.phaseCore)
        Angles.randLenVectors(
            e.id.toLong(), 2, 1f + e.fin() * 2f
        ) { x, y -> Fill.circle(e.x + x, e.y + y, e.fout() * 1.2f) }
        Draw.reset()
    }
    val curse = Effect(40f) { e ->
        Draw.color(SqColor.curse)
        Draw.alpha(0.5f)
        val radius = e.foutpow() * 18f
        Lines.circle(e.x, e.y, radius)
        Draw.reset()
    }
    val entropyIncreasing = Effect(40f) { e ->
        Draw.color(SqColor.entropyIncreasing)
        Angles.randLenVectors(
            e.id.toLong(), 4, 1f + e.fin() * 2f
        ) { x, y -> Fill.circle(e.x + x, e.y + y, e.fout() * 1.2f) }
        Draw.reset()
    }

    private val rand = Rand()
}