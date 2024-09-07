package sequence.world.blocks.defense

import arc.graphics.g2d.TextureRegion
import mindustry.world.blocks.defense.ShieldWall
import mindustry.world.meta.StatValue
import sequence.core.SeqElem

open class SqShieldWall(name: String) : ShieldWall(name), SeqElem {
    init {
        outputsPower = false
        hasPower = true
        consumesPower = true
        conductivePower = true
    }

    override fun statValue() = null
}