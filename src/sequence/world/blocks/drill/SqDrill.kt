package sequence.world.blocks.drill

import mindustry.world.blocks.production.Drill
import sequence.core.SeqElem

class SqDrill(name: String) : Drill(name), SeqElem {
    var ord: Int = -1
    override val order: Int
        get() = ord
}
