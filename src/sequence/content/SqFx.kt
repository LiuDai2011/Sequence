package sequence.content

import arc.graphics.Color
import mindustry.entities.effect.ParticleEffect
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
        colorFrom = Color.valueOf("dedede")
        colorTo = Color.valueOf("6b6b6b")
    }
}