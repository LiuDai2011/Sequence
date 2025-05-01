package sequence.world.meta

import arc.math.geom.Point2
import arc.struct.IntMap
import arc.struct.OrderedSet
import arc.struct.Seq
import arc.struct.StringMap
import arc.util.io.Reads
import arc.util.io.Streams.OptimizedByteArrayOutputStream
import arc.util.io.Writes
import arc.util.serialization.Base64Coder
import mindustry.Vars
import mindustry.content.Blocks
import mindustry.ctype.ContentType
import mindustry.game.Team
import mindustry.io.TypeIO
import mindustry.world.Block
import mindustry.world.blocks.legacy.LegacyBlock
import java.io.*
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream

class MultiBlockSchematic() {
    var tiles: Seq<CacheBlockTile> = Seq()
    var tags: StringMap = StringMap()

    constructor(vararg blocks: CacheBlockTile) : this() {
        tiles.add(blocks)
    }

    constructor(tiles: Seq<CacheBlockTile>) : this() {
        this.tiles = tiles
    }

    fun setPlaceHolder(x: Int, y: Int, team: Team) {
        for (tile in tiles) {
            tile.x += x
            tile.y += y
            tile.setPlaceHolder(team)
            tile.x -= x
            tile.y -= y
        }
    }

    fun removePlaceHolder(x: Int, y: Int) {
        for (tile in tiles) {
            tile.x += x
            tile.y += y
            tile.removePlaceHolder()
            tile.x -= x
            tile.y -= y
        }
    }

    fun draw(x: Int, y: Int) {
        for (tile in tiles) {
            tile.x += x
            tile.y += y
            tile.draw()
            tile.x -= x
            tile.y -= y
        }
    }

    fun afterChange() {
        tiles.sort { o1, o2 ->
            if (o1.y == o2.y) {
                if (o1.x == o2.x) 0
                else o1.x - o2.x
            } else -o1.y + o2.y
        }
    }

    fun writeBase64() = Companion.writeBase64(this)

    companion object {
        val header = byteArrayOf('m'.code.toByte(), 'b'.code.toByte(), 's'.code.toByte(), 'h'.code.toByte())
        private val out = OptimizedByteArrayOutputStream(1024)

        fun read(input: InputStream): MultiBlockSchematic {
            for (b in header) {
                if (input.read() != b.toInt()) {
                    throw IOException("Not a schematic file (missing header).")
                }
            }
            DataInputStream(InflaterInputStream(input)).use { stream ->
                val map = StringMap()
                val tags = stream.readInt()
                for (i in 0 until tags) {
                    map.put(stream.readUTF(), stream.readUTF())
                }
                val blocks = IntMap<Block>()
                val length = stream.readInt()
                for (i in 0 until length) {
                    val name = stream.readUTF()
                    val block = Vars.content.getByName<Block>(ContentType.block, name)
                    blocks.put(i, if (block == null || block is LegacyBlock) Blocks.air else block)
                }
                val total = stream.readInt()
                val tiles = Seq<CacheBlockTile>(total)
                for (i in 0 until total) {
                    val block = blocks[stream.readInt()]
                    val position = stream.readInt()
                    val config = TypeIO.readObject(Reads.get(stream))
                    val rotation = stream.readByte()
                    if (block !== Blocks.air) {
                        tiles.add(
                            CacheBlockTile(
                                Point2.x(position).toInt(),
                                Point2.y(position).toInt(),
                                BlockTile(block, 0, 0, rotation * 90f),
                                config
                            )
                        )
                    }
                }
                return MultiBlockSchematic(tiles).apply {
                    this.tags = map
                }
            }
        }

        fun write(schematic: MultiBlockSchematic, output: OutputStream) {
            output.write(header)
            DataOutputStream(DeflaterOutputStream(output)).use { stream ->
                stream.writeInt(schematic.tags.size)
                for (e in schematic.tags.entries()) {
                    stream.writeUTF(e.key)
                    stream.writeUTF(e.value)
                }
                val blocks = OrderedSet<Block>()
                schematic.tiles.each { blocks.add(it.tile.block) }
                stream.writeInt(blocks.size)
                for (i in 0 until blocks.size) {
                    stream.writeUTF(blocks.orderedItems()[i].name)
                }
                stream.writeInt(schematic.tiles.size)
                for (tile in schematic.tiles) {
                    stream.writeInt(blocks.orderedItems().indexOf(tile.tile.block))
                    stream.writeInt(Point2.pack(tile.x, tile.y))
                    TypeIO.writeObject(Writes.get(stream), tile.config)
                    stream.writeByte((tile.tile.rotation / 90f).toInt())
                }
            }
        }

        fun writeBase64(schematic: MultiBlockSchematic): String {
            return try {
                out.reset()
                write(schematic, out)
                String(Base64Coder.encode(out.buffer, out.size()))
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }

        fun readBase64(schematic: String): MultiBlockSchematic {
            return try {
                read(ByteArrayInputStream(Base64Coder.decode(schematic.trim { it <= ' ' }))).apply {
                    afterChange()
                }
            } catch (e: IOException) {
                throw java.lang.RuntimeException(e)
            }
        }
    }
}