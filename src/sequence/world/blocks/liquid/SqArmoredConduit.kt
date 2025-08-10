package sequence.world.blocks.liquid

import mindustry.world.blocks.liquid.ArmoredConduit
import sequence.core.SeqElem

class SqArmoredConduit(name: String) : ArmoredConduit(name), SeqElem {
    var ord: Int = -1

    override val order: Int
        get() = ord
}