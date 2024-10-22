package sequence.content

import arc.Events
import mindustry.Vars
import mindustry.content.Blocks
import mindustry.game.EventType.ClientLoadEvent
import mindustry.world.blocks.defense.turrets.ItemTurret
import mindustry.world.blocks.defense.turrets.Turret
import sequence.content.wrap.JavaWrappers
import sequence.util.classEq

object SqOverride {
    fun setup() {
        Events.on(ClientLoadEvent::class.java) {
            for (content in Vars.content.blocks()) {
                if (content is Turret) {
                    if (content is ItemTurret) {
                        if (content classEq ItemTurret::class) {
                            content.buildType = JavaWrappers.wrapItemTurretBuild(content)
                        }
                    }
                }
            }
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
        }
    }
}
