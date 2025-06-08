package sequence.content

import arc.graphics.Color
import mindustry.type.Item
import mindustry.world.meta.Stat
import sequence.core.SeqElem
import sequence.util.register
import sequence.world.meta.SqStat

object SqItems {
    lateinit var berylliumalAlloy: SqItem
    lateinit var crystallizedBeryllium: SqItem
    lateinit var encapsulatedImagineEnergy: SqItem
    lateinit var grainBoundaryAlloy: SqItem
    lateinit var pureSilicon: SqItem
    lateinit var phaseCore: SqItem
    lateinit var standardAlloyPlate: SqItem
    lateinit var vectorizedChip: SqItem
    lateinit var encapsulatedInverseEnergy: SqItem
    lateinit var particleContainer: SqItem

    fun load() {
        berylliumalAlloy = SqItem("berylliumal-alloy", Color.valueOf("989aa4")).register {
            cost = 0.95f
            healthScaling = 1.3f
        }
        crystallizedBeryllium = SqItem("crystallized-beryllium", Color.valueOf("d1eeee")).register {
            healthScaling = 0.5f
            cost = 0.65f
        }
        encapsulatedImagineEnergy =
            InformationAnnihilateItem("encapsulated-imagine-energy", Color.valueOf("bf92f9")).register {
                radioactivity = 15f
                charge = 7f
                cost = 4.5f
                healthScaling = 0.01f
                explosiveness = 2f
            }
        encapsulatedInverseEnergy =
            InformationAnnihilateItem("encapsulated-inverse-energy", Color.valueOf("f93a3a")).register {
                radioactivity = 22f
                cost = 0f
                healthScaling = -1f
                explosiveness = 20f
                informationAnnihilationFactor = -0.01f
            }
        grainBoundaryAlloy = SqItem("grain-boundary-alloy", Color.valueOf("aaaaaa")).register {
            radioactivity = 0.3f
            charge = 0.5f
            cost = 1.8f
        }
        pureSilicon = SqItem("pure-silicon", Color.valueOf("53565c")).register {
            cost = 0.9f
        }
        phaseCore = SqItem("phase-core", Color.valueOf("ffd59e")).register {
            radioactivity = 2.5f
            cost = 5f
            healthScaling = 0.1f
        }
        standardAlloyPlate = SqItem("standard-alloy-plate", Color.valueOf("cbd97f")).register {
            cost = 1.45f
            healthScaling = 1.3f
        }
        vectorizedChip = SqItem("vectorized-chip", Color.valueOf("4a4b53")).register {
            cost = 1.2f
            radioactivity = 0.85f
            explosiveness = 0.05f
        }
        particleContainer = SqItem("particle-container", Color.valueOf("ffd59e")).register {
            cost = 0.9f
        }
    }

    class SqItem @JvmOverloads constructor(name: String, color: Color = Color(Color.black)) : Item(name, color),
        SeqElem {
        var ord = -1
        override val order: Int
            get() = ord
    }

    class InformationAnnihilateItem @JvmOverloads constructor(name: String, color: Color = Color(Color.black)) :
        SqItem(name, color) {
        var informationAnnihilationFactor: Float = 0.3f

        override fun setStats() {
            super.setStats()
            stats.remove(Stat.explosiveness)
            stats.remove(Stat.radioactivity)
            stats.remove(Stat.charge)
            stats.remove(Stat.flammability)
            stats.addPercent(SqStat.informationAnnihilationFactor, informationAnnihilationFactor)
        }
    }
}
