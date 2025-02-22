package sequence.world.meta.chunks

import mindustry.io.SaveFileReader.CustomChunk
import java.io.DataInput
import java.io.DataOutput

object PlaceHolderChunk : CustomChunk {
    override fun write(stream: DataOutput) {
    }

    override fun read(stream: DataInput) {
    }
}