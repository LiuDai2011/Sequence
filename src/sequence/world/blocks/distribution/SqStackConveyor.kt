package sequence.world.blocks.distribution

import mindustry.world.blocks.distribution.StackConveyor
import sequence.core.SeqElem

class SqStackConveyor(name: String) : StackConveyor(name), SeqElem {
    var ord: Int = -1

    override val order: Int
        get() = ord
}