package sequence.world.meta

import arc.Events
import mindustry.game.EventType
import sequence.io.SqCustomChunk
import java.io.DataInput
import java.io.DataOutput

object ImagineEnergyChunk : SqCustomChunk {
    override val version: Int
        get() = 0

    init {
        Events.on(EventType.ResetEvent::class.java) { e ->
        }
        Events.on(EventType.WorldLoadEvent::class.java) { e ->
            //
        }
    }

    override fun writeCustom(stream: DataOutput) {

    }

    override fun readCustom(stream: DataInput, ver: Int) {

    }
}