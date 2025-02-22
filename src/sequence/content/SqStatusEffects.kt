package sequence.content

import arc.graphics.Color
import arc.util.Time
import mindustry.gen.Unit
import mindustry.type.StatusEffect
import sequence.core.SeqElem
import sequence.util.register
import kotlin.math.pow

object SqStatusEffects {
    lateinit var brokenShield: SqStatusEffect

    fun load() {
        brokenShield = object : SqStatusEffect("broken-shield") {
            override fun update(unit: Unit?, time: Float) {
                super.update(unit, time)
                if (unit == null) return
                unit.shield = (unit.shield - damage.pow(4) * Time.delta).coerceAtLeast(0f)
            }
        }.register {
            color = Color.valueOf("ffd37f")
            damage = 6.7f
        }
    }

    class SqStatusEffect(name: String) : StatusEffect(name), SeqElem {
        var order: Int = -1

        override fun statValue() = null

        override fun order() = order
    }
}