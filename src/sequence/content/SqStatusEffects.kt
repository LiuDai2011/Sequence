package sequence.content

import arc.func.Cons
import arc.util.Time
import mindustry.type.StatusEffect
import sequence.core.SeqElem
import sequence.graphic.SqColor
import sequence.util.MUnit
import sequence.util.clearImmunities
import sequence.util.invoke
import sequence.util.register
import kotlin.math.pow

object SqStatusEffects {
    lateinit var brokenShield: SqStatusEffect
    lateinit var corrode: SqStatusEffect
    lateinit var curse: SqStatusEffect
    lateinit var entropyIncreasing: SqStatusEffect

    fun load() {
        brokenShield = SqStatusEffect("broken-shield").register {
            color = SqColor.brokenShield
            damage = 3.7f
            onUpdate = Cons { unit ->
                unit.shield = (unit.shield - damage.pow(4) * Time.delta).coerceAtLeast(0f)
            }
        }
        corrode = SqStatusEffect("corrode").register {
            color = SqColor.corrode
            damage = 4f
            speedMultiplier = 0.5f
            healthMultiplier = 0.9f
            reloadMultiplier = 0.8f
            buildSpeedMultiplier = 0.8f
            damageMultiplier = 0.9f
            effect = SqFx.corrode
        }
        curse = SqStatusEffect("curse").register {
            color = SqColor.curse
            damage = 5f
            speedMultiplier = 0.46f
            healthMultiplier = 0.85f
            reloadMultiplier = 0.86f
            effect = SqFx.curse
        }
        entropyIncreasing = SqStatusEffect("entropy-increasing").register {
            clearImmunities()
            color = SqColor.entropyIncreasing
            damage = 1f
            effectChance = 1f
            effect = SqFx.entropyIncreasing
            onUpdate = Cons { unit ->
                unit.maxHealth *= 0.999f.pow(Time.delta)
                unit.shield *= 0.995f.pow(Time.delta)
                unit.health = unit.health.coerceAtMost(unit.maxHealth)
            }
        }
    }

    class SqStatusEffect(name: String) : StatusEffect(name), SeqElem {
        var ord: Int = -1
        override val order: Int
            get() = ord
        var onUpdate: Cons<MUnit> = Cons {}

        override fun update(unit: MUnit?, time: Float) {
            super.update(unit, time)
            if (unit == null) return
            onUpdate(unit)
        }
    }
}