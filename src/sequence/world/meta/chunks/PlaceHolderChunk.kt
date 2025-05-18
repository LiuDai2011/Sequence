package sequence.world.meta.chunks

import mindustry.Vars
import mindustry.io.TypeIO
import sequence.core.MapDataManager
import sequence.io.SqCustomChunk
import sequence.util.*
import java.io.DataInput
import java.io.DataOutput

object PlaceHolderChunk : SqCustomChunk {
    override val version: Int
        get() = 1

    override fun writeCustom(stream: DataOutput) {
        val size = MapDataManager.placeHolders.size
        stream.writeInt(size)
        for ((k, v) in MapDataManager.placeHolders) {
            stream.writeInt(k)
            TypeIO.writeStringData(stream, v.name)
        }
    }

    override fun readCustom(stream: DataInput, ver: Int) {
        when (ver) {
            1 -> {
                val size = stream.readInt()
                for (i in 0..<size) {
                    val k = stream.readInt()
                    val v = TypeIO.readStringData(stream)
                    MapDataManager.placeHolders[k] = Vars.content.block(v)
                }
            }
        }
    }
}
