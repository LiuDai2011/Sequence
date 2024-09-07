package sequence.content

import arc.Events
import mindustry.Vars
import mindustry.content.Blocks
import mindustry.game.EventType.ClientLoadEvent
import mindustry.world.blocks.defense.turrets.ItemTurret
import mindustry.world.blocks.defense.turrets.Turret
import sequence.SeqMod
import sequence.content.wrap.JavaWrappers
import sequence.core.SeqElem
import sequence.core.SqBundle
import sequence.core.SqEventType.ContentStatInitEvent
import sequence.ui.SqContentInfoDialog
import sequence.world.meta.SqStat

object SqOverride {
    fun setup() {
        Events.on(ClientLoadEvent::class.java) {
            Vars.ui.content = SqContentInfoDialog()
            for (content in Vars.content.blocks()) {
                if (content is Turret) {
                    if (content is ItemTurret) {
                        content.buildType = JavaWrappers.wrapItemTurretBuild(content)
                    }
                }
            }
            Events.on(ContentStatInitEvent::class.java) { e ->
                e.apply {
                    if (content.minfo.mod.name == SeqMod.MOD?.name && content !is SeqElem) TODO("Not yet implemented")
                    if (content is SeqElem) {
                        content.stats.useCategories = true
                        content.stats.add(
                            SqStat.sequenceOrder,
                            if (content.order() == -1) SqBundle.modCat("seq-null") else content.order().toString()
                        )
                        if (content.order() != -1 && content.statValue() != null) {
                            e.content.stats.add(SqStat.sequenceEffect, content.statValue())
                        }
                    }
                }
            }
            Blocks.itemSource.health = Int.MAX_VALUE
            Blocks.liquidSource.health = Int.MAX_VALUE
            Blocks.powerSource.health = Int.MAX_VALUE
            Blocks.payloadSource.health = Int.MAX_VALUE
            Blocks.heatSource.health = Int.MAX_VALUE
            Blocks.itemVoid.health = Int.MAX_VALUE
            Blocks.liquidVoid.health = Int.MAX_VALUE
            Blocks.powerVoid.health = Int.MAX_VALUE
            Blocks.payloadVoid.health = Int.MAX_VALUE
        }
    }
}
