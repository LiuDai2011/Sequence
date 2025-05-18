package sequence.io

import mindustry.io.SaveFileReader.CustomChunk
import java.io.DataInput
import java.io.DataOutput

interface SqCustomChunk : CustomChunk {
    val version: Int

    override fun write(stream: DataOutput) {
        stream.writeInt(version)
        writeCustom(stream)
    }

    fun writeCustom(stream: DataOutput)

    override fun read(stream: DataInput) {
        val version = stream.readInt()
        readCustom(stream, version)
    }

    fun readCustom(stream: DataInput, ver: Int)
}