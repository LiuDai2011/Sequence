package sequence.content

import arc.graphics.g2d.Draw
import arc.graphics.g2d.Lines
import mindustry.entities.Effect
import mindustry.entities.effect.ParticleEffect
import mindustry.graphics.Drawf
import sequence.graphic.SqColor
import sequence.util.register

object SqFx {
    val fgbaTrail = ParticleEffect().register {
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
        colorFrom = SqColor.gba[0]
        colorTo = SqColor.gba[2]
    }
    val fgbaBomb = Effect(15f, 100f) { e ->
        Draw.color(SqColor.gba[2])
        Lines.stroke(e.fout() * 4f)
        Lines.circle(e.x, e.y, 4f + e.finpow() * 20f)
        Drawf.light(e.x, e.y, 150f, SqColor.gba[1], 0.9f * e.fout())
    }
}