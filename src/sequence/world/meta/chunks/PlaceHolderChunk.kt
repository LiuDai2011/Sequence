package sequence.world.meta.chunks

import mindustry.Vars
import mindustry.io.SaveFileReader.CustomChunk
import mindustry.io.TypeIO
import sequence.core.MapDataManager
import sequence.util.component1
import sequence.util.component2
import sequence.util.set
import java.io.DataInput
import java.io.DataOutput

object PlaceHolderChunk : CustomChunk {
    override fun write(stream: DataOutput) {
        val size = MapDataManager.placeHolders.size
        stream.writeInt(size)
        for ((k, v) in MapDataManager.placeHolders) {
            stream.writeInt(k)
            TypeIO.writeStringData(stream, v.name)
        }
    }

    override fun read(stream: DataInput) {
        val size = stream.readInt()
        for (i in 0..<size) {
            val k = stream.readInt()
            val v = TypeIO.readStringData(stream)
            MapDataManager.placeHolders[k] = Vars.content.block(v)
        }
    }
}
