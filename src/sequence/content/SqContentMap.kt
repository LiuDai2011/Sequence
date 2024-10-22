package sequence.content

import arc.Core
import arc.struct.IntMap
import arc.struct.Seq
import mindustry.Vars
import mindustry.ctype.MappableContent
import mindustry.ctype.UnlockableContent
import sequence.SeqMod
import sequence.core.SeqElem
import sequence.core.SqBundle
import sequence.core.SqLog
import sequence.util.IgnoredLocalName
import sequence.util.IgnoredSequenceElementImpl
import sequence.util.hasSign
import sequence.util.set
import sequence.world.meta.SqStat

object SqContentMap {
    val seqMap: IntMap<Seq<UnlockableContent>> = IntMap()
    fun init() {
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
                        seqMap.get(content.order()) { Seq() }.add(content)
                    }
                }
            }
        }
        if (!uninitiatedContent.isEmpty) {
            if (!SeqMod.skipChk) {
                TODO("\n${uninitiatedContent.joinToString("\n\n")}\n\n")
            } else {
                SqLog.warn("\n${uninitiatedContent.joinToString("\n\n")}\n\n")
            }
        }
    }
}