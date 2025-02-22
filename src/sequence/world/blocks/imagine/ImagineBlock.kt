package sequence.world.blocks.imagine

import mindustry.world.Block
import sequence.core.AllOpen

@AllOpen
class ImagineBlock(name: String) : Block(name) {
    var hasImagine = true
    var imagineCapacity = 10f

    init {
        update = true
        solid = true
    }

    fun hasImagineEnergy(): Boolean {
        return hasImagine
    }
}
