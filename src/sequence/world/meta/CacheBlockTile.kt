package sequence.world.meta

import mindustry.content.Blocks

class CacheBlockTile(var x: Int, var y: Int, var tile: BlockTile = empty, var config: Any? = null) {
    fun draw() {
        tile.x += x
        tile.y += y
        tile.draw()
        tile.x -= x
        tile.y -= y
    }

    companion object {
        val empty = BlockTile(Blocks.air, Int.MAX_VALUE, Int.MAX_VALUE, 0f, 0f)
    }
}