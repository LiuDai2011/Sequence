package sequence.world.blocks

import arc.Core
import arc.func.Prov
import arc.struct.ObjectMap
import mindustry.Vars
import mindustry.content.Blocks
import mindustry.gen.Building
import mindustry.gen.Sounds
import mindustry.world.Block
import mindustry.world.Tile
import mindustry.world.meta.BuildVisibility
import sequence.core.MapDataManager
import sequence.core.SqLog
import sequence.util.get

class MBSPlaceHolder private constructor(val block: Block = Blocks.air) :
    Block("sequence-inner-block.mbs-place-holder-${block.name}") {
    init {
        replaceable = true

        update = true
        squareSprite = false

        destructible = true
        breakable = false
        solid = true
        rebuildable = false
        canOverdrive = false

        instantDeconstruct = true

        hasItems = true
        hasLiquids = true
        hasPower = false

        buildVisibility = BuildVisibility.hidden
        inEditor = false

        drawCracks = false
        drawArrow = false
        drawTeamOverlay = false
        hasShadow = false

        ambientSound = Sounds.none
        breakSound = Sounds.none
        destroySound = Sounds.none
        loopSound = Sounds.none
        placeSound = Sounds.none

        region = Core.atlas.find("status-blasted")
        fullIcon = Core.atlas.find("status-blasted")
        uiIcon = Core.atlas.find("status-blasted")

        localizedName = block.localizedName
    }

    override fun canBreak(tile: Tile?) = false
    override fun isHidden() = true
    override fun canReplace(other: Block?) = (other ?: Blocks.air) == block

    override fun drawShadow(tile: Tile?) {}

    inner class MBSPlaceHolderBuild : Building() {
        override fun draw() {}
        override fun onDestroyed() {}
        override fun update() {
            if (!Vars.state.isEditor) {
                val holdBlock = MapDataManager.placeHolders[pos()]
                if (holdBlock == null) {
                    tile.setAir()
                    return
                }
                holdBlock.apply {
                    if (this == this@MBSPlaceHolder.block) return
                    SqLog.info("@, @, @ set", x, y, this)
                    Vars.world.tiles[this@MBSPlaceHolderBuild.pos()].setBlock(MBSPlaceHolder[this])
                }
            }
        }
    }

    companion object {
        val map: ObjectMap<Block, MBSPlaceHolder> = ObjectMap()
        operator fun get(block: Block): MBSPlaceHolder = map[block, Prov { MBSPlaceHolder(block) }]
    }
}