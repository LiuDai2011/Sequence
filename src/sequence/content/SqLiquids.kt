package sequence.content

import arc.graphics.Color
import arc.struct.Seq
import arc.util.Strings
import mindustry.type.Liquid
import mindustry.ui.Styles
import mindustry.world.meta.StatValue
import sequence.core.SeqElem
import sequence.core.SqBundle
import sequence.util.register

object SqLiquids {
    lateinit var crystallizedFluid: SqLiquid
    lateinit var vectorizedFluid: SqLiquid
    fun load() {
        crystallizedFluid = SqLiquid("crystallized-fluid").register {
            color = Color.valueOf("d1eeee")
            heatCapacity = 1.2f
            boilPoint = 2.3f
            temperature = 0.3f
        }
        vectorizedFluid = VectorizedFluid("vectorized-fluid").register {
            heatCapacity = 3f
            boilPoint = 1f
            temperature = 0.4f
            damageMulti = 1.183f
            knockbackMulti = 1.173f
            consumeAmount = 0.002f
            color = Color.valueOf("c5d6f2")
            ord = 3
        }
    }

    class SqLiquid(name: String) : Liquid(name), SeqElem {
        var ord = -1
        override val order: Int
            get() = ord
    }

    class VectorizedFluid(name: String) : SqLiquid(name) {
        var damageMulti = 0f
        var knockbackMulti = 0f
        var consumeAmount = 0f

        val desc: String by lazy {
            SqBundle.format(
                "stat.bullet-damage-multi",
                Strings.autoFixed(damageMulti * 100f - 100f, 2)
            ) + " " + SqBundle.format(
                "stat.bullet-knockback-multi",
                Strings.autoFixed(knockbackMulti * 100f - 100f, 2)
            )
        }

        init {
            all.add(this)
        }

        override val statValue: StatValue?
            get() = StatValue { table ->
                table.row().table(Styles.grayPanel) {
                    it.left().top().defaults().padRight(3f).left()
                    it.add(desc)
                }
                table.row()
            }

        companion object {
            val all = Seq<VectorizedFluid>()
        }
    }
}
