package sequence.world.blocks.defense

import arc.func.Prov
import arc.math.Mathf
import arc.util.Strings
import arc.util.Time
import mindustry.world.meta.StatValue
import sequence.core.SqBundle
import sequence.ui.SqUI.pad

class UnionWall(name: String) : SqWall(name) {
    var apportionedSpeed: Float = 0.008f
    var depthLimit: Int = 8

    init {
        buildType = Prov { UnionWallBuild() }
        update = true
    }

    inner class UnionWallBuild : WallBuild() {
        override fun damage(damage: Float) {
            damage(damage, 0)
        }

        fun damage(damage: Float, depth: Int) {
            if (depth > depthLimit || damage >= maxHealth) {
                super.damage(damage)
                return
            }
            if (damage <= 0.001 * health) {
                heal(damage)
                return
            }

            var count = 1
            var handled = 0
            for (p in proximity) {
                if (p is UnionWallBuild) {
                    count++
                }
            }

            super.damage(damage / count)
            for (p in proximity) {
                if (p is UnionWallBuild) {
                    try {
                        p.damage(damage / count, depth + 1)
                        handled++
                    } catch (e: StackOverflowError) {
                        super.damage(damage - damage / count * handled)
                        break
                    }
                }
            }
        }

        override fun updateTile() {
            super.updateTile()
            var sumHealth = 0f
            var sumMaxHealth = 0f
            for (p in proximity) {
                if (p is UnionWallBuild) {
                    sumHealth += p.health
                    sumMaxHealth += p.maxHealth
                }
            }
            for (p in proximity) {
                if (p is UnionWallBuild) {
                    p.health = Mathf.lerp(
                        p.health / p.maxHealth,
                        sumHealth / sumMaxHealth,
                        apportionedSpeed * Time.delta
                    ) * p.maxHealth
                }
            }
        }
    }

    override val order: Int
        get() = 94

    override val statValue: StatValue?
        get() = StatValue {
            it.row()
            it.pad { bt ->
                bt.add(SqBundle["blocks.union-wall.seqstat"]).row()
                bt.add(
                    SqBundle.format(
                        "blocks.union-wall.seqstat.apportionedSpeed",
                        Strings.autoFixed(apportionedSpeed * 60f, 4)
                    )
                )
            }
        }
}
