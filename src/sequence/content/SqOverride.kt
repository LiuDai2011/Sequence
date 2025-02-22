package sequence.content

import arc.Events
import mindustry.content.Blocks
import mindustry.ctype.UnlockableContent
import mindustry.game.EventType.ClientLoadEvent
import mindustry.world.blocks.defense.turrets.ItemTurret
import sequence.graphic.SqColor

object SqOverride {
    fun setup() {
        Events.on(ClientLoadEvent::class.java) {
            SqContentMap.init()
            Blocks.itemSource.health = Int.MAX_VALUE
            Blocks.liquidSource.health = Int.MAX_VALUE
            Blocks.powerSource.health = Int.MAX_VALUE
            Blocks.payloadSource.health = Int.MAX_VALUE
            Blocks.heatSource.health = Int.MAX_VALUE
            Blocks.itemVoid.health = Int.MAX_VALUE
            Blocks.liquidVoid.health = Int.MAX_VALUE
            Blocks.powerVoid.health = Int.MAX_VALUE
            Blocks.payloadVoid.health = Int.MAX_VALUE

            Blocks.foreshadow.override<ItemTurret> {
                ammoTypes.put(SqItems.grainBoundaryAlloy, SqBulletTypes.foreshadowGBA)
            }
        }
    }

    fun addOverrideTag(content: UnlockableContent) {
        content.description = content.description ?: ""
        content.description += "\n[#${SqColor.LiuDai}]Override by [#${SqColor.NedKelly}]Sequence[]"
    }

    inline fun <reified T : UnlockableContent> UnlockableContent.override(builder: T.() -> Unit) {
        addOverrideTag(this)
        builder(this as T)
    }
}
