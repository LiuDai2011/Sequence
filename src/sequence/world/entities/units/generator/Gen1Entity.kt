package sequence.world.entities.units.generator

import arc.scene.ui.layout.Table
import arc.util.Time
import mindustry.gen.UnitEntity
import mindustry.graphics.Pal
import mindustry.ui.Bar
import sequence.util.EntityRegister
import kotlin.math.floor

class Gen1Entity : UnitEntity() {
    override fun classId() = EntityRegister.getID(this::class.java)

    var timesShield = 0
    var timer0: Float = 0f

    override fun update() {
        super.update()
        timer0 += Time.delta
        timesShield = (timesShield + floor(timer0 / timePerShield).toInt()).coerceAtMost(timesShieldMax)
        timer0 %= timePerShield
    }

    override fun display(table: Table?) {
        super.display(table)
        table?.table { bt ->
            bt.defaults().growX().height(20f).pad(4f)
            bt.row()
            bt.add(Bar(
                { "Timer: $timesShield" },
                { Pal.accent },
                { timesShield.toFloat() / timesShieldMax }
            )).growX().row()
        }?.growX()
    }

    override fun rawDamage(amount: Float) {
        if (timesShield > 0) {
            timesShield--
            return
        }
        val hadShields = shield > 1e-4f
        if (hadShields) super.rawDamage(amount.coerceAtMost(maxDamage) * (1 - shieldImmunity))
        else super.rawDamage(amount.coerceAtMost(maxDamage))
    }

    companion object {
        var timesShieldMax = 1800
        var timePerShield = 5f
        var maxDamage = 1000f
        var shieldImmunity = 0.25f
    }
}