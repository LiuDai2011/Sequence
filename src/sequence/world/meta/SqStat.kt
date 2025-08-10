package sequence.world.meta

import mindustry.world.meta.Stat
import mindustry.world.meta.StatCat

object SqStat {
    val sequence = StatCat("sequence")
    val sequenceOrder = Stat("sequenceOrder", sequence)
    val sequenceEffect = Stat("sequenceEffect", sequence)
    val informationAnnihilationFactor = Stat("informationAnnihilationFactor")
    val ammoOverride = Stat("ammo", StatCat.function)
    val reloadOverride = Stat("reload", StatCat.function)
}
