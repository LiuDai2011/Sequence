package sequence.world.blocks.defense

import arc.func.Cons
import arc.func.Prov
import mindustry.Vars
import mindustry.Vars.tilesize
import mindustry.content.Fx
import mindustry.gen.Building
import mindustry.gen.Groups
import mindustry.graphics.Drawf
import mindustry.graphics.Pal
import mindustry.type.Liquid
import mindustry.ui.Fonts
import mindustry.world.Block
import sequence.content.SqLiquids
import sequence.content.SqLiquids.VectorizedFluid
import sequence.core.SeqElem

class BulletEnhancer(name: String) : Block(name), SeqElem {
    var range: Float = 80f

    //    var maxEnhancer: Float = 1e8f
    var ord: Int = 49
    override val order: Int
        get() = ord

    init {
        hasLiquids = true
        liquidCapacity = 50f
        update = true
        solid = true
        buildType = Prov { BulletEnhancerBuild() }
    }

    override fun drawPlace(x: Int, y: Int, rotation: Int, valid: Boolean) {
        super.drawPlace(x, y, rotation, valid)
        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, range, Pal.placing)
    }

    inner class BulletEnhancerBuild : Building() {
        override fun updateTile() {
            super.updateTile()
            if (!Vars.net.client()) {
                Groups.bullet.intersect(x - range, y - range, range * 2, range * 2, Cons {
                    if (it.dst(x, y) <= range && it.team == team) {
                        val liquid = liquids.current() ?: return@Cons
                        if (liquid !is VectorizedFluid) return@Cons
//                        if (it.damage * (liquid.damageMulti - 1f) > maxEnhancer) return@intersect
                        if (liquids.get(liquid) >= Int.MAX_VALUE) {
                            liquids.set(liquid, 0f)
                            return@Cons
                        }
                        val consume = liquid.consumeAmount * it.damage * liquid.damageMulti
                        if (getLiquidAmount(liquid) < consume) return@Cons
//                        it.type(it.type.copy().change { bt ->
//                            bt?.apply {
//                                damage *= liquid.damageMulti
//                                knockback *= liquid.knockbackMulti
//                            }
//                        })
                        it.damage *= liquid.damageMulti
                        Fx.blastExplosion.at(it)
                        consumeLiquid(liquid, consume)
                    }
                })
            }
        }

        override fun acceptLiquid(source: Building?, liquid: Liquid?): Boolean {
            return liquid is VectorizedFluid && liquids.get(liquid) <= liquidCapacity
        }

        override fun drawSelect() {
            super.drawSelect()
            Drawf.dashCircle(x, y, range, team.color)
            Fonts.def.draw(getLiquidAmount(SqLiquids.vectorizedFluid).toString(), x, y)
        }

        protected fun getLiquidAmount(liquid: Liquid): Float {
            var r = liquids?.get(liquid) ?: 0f
            for (b in proximity) {
                r += b.liquids?.get(liquid) ?: 0f
            }
            return r
        }

        protected fun consumeLiquid(liquid: Liquid, amount: Float) {
            if (liquids.get(liquid) <= amount) {
                var have = liquids.get(liquid)
                var amount = amount
                liquids.remove(liquid, amount.coerceAtMost(have).coerceAtLeast(0f))
                amount -= have
                for (b in proximity) {
                    have = b?.liquids?.get(liquid) ?: 0f
                    b?.liquids?.remove(liquid, amount.coerceAtMost(have).coerceAtLeast(0f))
                    amount -= have
                }
                return
            }
            liquids.remove(liquid, amount)
        }
    }
}
