package sequence.io

import java.io.DataInput
import java.io.DataOutput

object ImagineEnergyChunk : SqCustomChunk {
    override val version: Int
        get() = 0

    override fun writeCustom(stream: DataOutput) {

    }

    override fun readCustom(stream: DataInput, ver: Int) {

    }
}