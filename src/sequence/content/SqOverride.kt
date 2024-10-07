package sequence.content

import arc.Core
import arc.Events
import arc.struct.Seq
import mindustry.Vars
import mindustry.content.Blocks
import mindustry.ctype.MappableContent
import mindustry.ctype.UnlockableContent
import mindustry.game.EventType.ClientLoadEvent
import mindustry.world.blocks.defense.turrets.ItemTurret
import mindustry.world.blocks.defense.turrets.Turret
import sequence.SeqMod
import sequence.SeqMod.Companion.skipChk
import sequence.content.wrap.JavaWrappers
import sequence.core.SeqElem
import sequence.core.SqBundle
import sequence.core.SqLog
import sequence.util.IgnoredLocalName
import sequence.util.IgnoredSequenceElementImpl
import sequence.util.classEq
import sequence.util.hasSign
import sequence.world.meta.SqStat

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
            val uninitiatedContent: Seq<String> = Seq()
            Vars.content.each { content ->
                if (content.minfo.mod == null) return@each
                if (content is MappableContent && content.minfo.mod.name == SeqMod.MOD.name) {
                    if (!content.hasSign<IgnoredSequenceElementImpl>()) {
                        if (content !is SeqElem)
                            uninitiatedContent.add("Not yet implemented(seqstat): content ${content.name}\nPlease give the information to LiuDai")
                    }
                    if (!content.hasSign<IgnoredLocalName>()) {
                        val prefix = "${content.contentType}.${content.name}"
                        if (!Core.bundle.has("$prefix.name") ||
                            !Core.bundle.has("$prefix.description") ||
                            !Core.bundle.has("$prefix.details")
                        )
                            uninitiatedContent.add(
                                "Not yet implemented(bundle): content ${content.name}\nPlease give the information to LiuDai" +
                                        (if (!Core.bundle.has("$prefix.name")) "\n$prefix.name=" else "") +
                                        (if (!Core.bundle.has("$prefix.description")) "\n$prefix.description=" else "") +
                                        (if (!Core.bundle.has("$prefix.details")) "\n$prefix.details=" else "")
                            )
                    } else SqLog.warn("$content has sign iln")
                    if (content is SeqElem && content is UnlockableContent) {
                        content.stats.useCategories = true
                        content.stats.add(
                            SqStat.sequenceOrder,
                            if (content.order() == -1) SqBundle.mod("seq-null") else content.order().toString()
                        )
                        if (content.order() != -1 && content.statValue() != null) {
                            content.stats.add(SqStat.sequenceEffect, content.statValue())
                        }
                    }
                }
            }
            if (!uninitiatedContent.isEmpty) {
                if (!skipChk) {
                    TODO("\n${uninitiatedContent.joinToString("\n\n")}\n\n")
                } else {
                    SqLog.warn("\n${uninitiatedContent.joinToString("\n\n")}\n\n")
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
