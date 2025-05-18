package sequence.world.blocks.turrets

import mindustry.world.blocks.defense.turrets.ItemTurret
import sequence.core.SeqElem

class SqItemTurret(name: String) : ItemTurret(name), SeqElem {
    var ord: Int = -1
    override val order: Int
        get() = ord
}