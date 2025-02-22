package sequence.core

import arc.Events
import arc.struct.IntMap
import mindustry.game.EventType
import mindustry.world.Block

object MapDataManager {
    val placeHolders: IntMap<Block> = IntMap()

    init {
        Events.on(EventType.WorldLoadEvent::class.java) {
            placeHolders.clear()
        }
    }
}