package sequence.core

import arc.util.Nullable
import mindustry.world.meta.StatValue

interface SeqElem {
    fun order() = -1

    @Nullable
    fun statValue(): StatValue?
}
