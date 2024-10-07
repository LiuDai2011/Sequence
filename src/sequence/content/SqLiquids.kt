package sequence.content

import arc.graphics.Color
import arc.scene.ui.Button
import arc.scene.ui.Image
import arc.struct.Seq
import arc.util.Scaling
import arc.util.Strings
import mindustry.Vars
import mindustry.type.Liquid
import mindustry.ui.Styles
import mindustry.world.blocks.defense.turrets.ItemTurret
import mindustry.world.meta.StatValue
import sequence.core.SeqElem
import sequence.core.SqBundle
import sequence.util.classEq
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
            heatCapacity = 1f
            boilPoint = 2f
            temperature = 0.4f
            damageMulti = 1.183f
            knockbackMulti = 1.173f
            consumeAmount = 8f
            color = Color.valueOf("c5d6f2")
            ord = 3
        }
    }

    open class SqLiquid(name: String) : Liquid(name), SeqElem {
        var ord = -1
        override fun order(): Int {
            return ord
        }

        override fun statValue(): StatValue? {
            return null
        }
    }

    open class VectorizedFluid(name: String) : SqLiquid(name) {
        var damageMulti = 0f
        var knockbackMulti = 0f
        var consumeAmount = 0f

        init {
            all.add(this)
        }

        override fun statValue(): StatValue {
            return StatValue { table ->
                for (content in Vars.content.blocks()) {
                    if (content is ItemTurret &&
                        content.consumesLiquid(this) &&
                        content classEq ItemTurret::class
                    ) {
                        table.row().table(Styles.grayPanel) {
                            it.left().top().defaults().padRight(3f).left()
                            val button = Button(Styles.cleari)
                            button.table { st ->
                                st.row()
                                st.add(Image(content.fullIcon).setScaling(Scaling.fit))
                            }.size(Vars.iconMed)
                            button.margin(5f)
                            button.clicked { Vars.ui.content.show(content) }
                            it.add(button)
                            it.add(getDesc(content))
                            it.row()
                        }.padLeft(0f).padTop(5f).padBottom(5f).growX().margin(10f)
                    }
                }
                table.row()
            }
        }

        private fun getDesc(it: ItemTurret): String {
            builder.setLength(0)
            builder.append(
                SqBundle.format(
                    "stat.bullet-damage-multi",
                    Strings.autoFixed(damageMulti * 100f - 100f, 2)
                )
            )
            var flag = false
            for (entry in it.ammoTypes) if (entry.value.knockback > 0) {
                flag = true
                break
            }
            if (flag) {
                builder.append("  ")
                builder.append(
                    SqBundle.format(
                        "stat.bullet-knockback-multi",
                        Strings.autoFixed(knockbackMulti * 100f - 100f, 2)
                    )
                )
            }
            return builder.toString()
        }

        companion object {
            val all = Seq<VectorizedFluid>()
            private val builder = StringBuilder()
        }
    }
}
