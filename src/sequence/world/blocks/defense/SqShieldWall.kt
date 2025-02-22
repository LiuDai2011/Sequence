package sequence.world.blocks.defense

import mindustry.world.blocks.defense.ShieldWall
import sequence.core.SeqElem

class SqShieldWall(name: String) : ShieldWall(name), SeqElem {
    init {
        outputsPower = false
        hasPower = true
        consumesPower = true
        conductivePower = true
    }

    override fun statValue() = null
}