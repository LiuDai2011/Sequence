package sequence.world.blocks.distribution

import mindustry.world.blocks.distribution.Duct
import sequence.core.SeqElem

class SqDuct(name: String) : Duct(name), SeqElem {
    var ord: Int = -1
    override val order: Int
        get() = ord
}