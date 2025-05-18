package sequence.core

import arc.util.Nullable
import mindustry.world.meta.StatValue

@AllOpen
interface SeqElem {
    val order: Int
        get() = -1

    @get:Nullable
    val statValue: StatValue?
        get() = null
}
