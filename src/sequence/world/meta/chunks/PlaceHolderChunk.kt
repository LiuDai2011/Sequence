package sequence.world.meta.chunks

import mindustry.io.SaveFileReader.CustomChunk
import java.io.DataInput
import java.io.DataOutput

object PlaceHolderChunk : CustomChunk {
    override fun write(stream: DataOutput) {
//        val size = MapDataManager.placeHolders.size
//        stream.writeInt(size)
//        for ((k, v) in MapDataManager.placeHolders) {
//            stream.writeInt(k)
//            TypeIO.writeStringData(stream, v.name)
//        }
    }

    override fun read(stream: DataInput) {
//        val size = stream.readInt()
//        for (i in 0..<size) {
//            val k = stream.readInt()
//            val v = TypeIO.readStringData(stream)
//            MapDataManager.placeHolders[k] = Vars.content.block(v)
//        }
    }
}
