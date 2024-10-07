package sequence.content

import arc.graphics.Color
import mindustry.type.Item
import mindustry.world.meta.Stat
import mindustry.world.meta.StatValue
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

    fun load() {
        berylliumalAlloy = SqItem("berylliumal-alloy").register {
            color = Color.valueOf("989aa4")
            cost = 0.95f
            healthScaling = 1.3f
        }
        crystallizedBeryllium = SqItem("crystallized-beryllium").register {
            color = Color.valueOf("d1eeee")
            healthScaling = 0.5f
            cost = 0.65f
        }
        encapsulatedImagineEnergy = EncapsulatedImagineEnergyItem("encapsulated-imagine-energy").register {
            color = Color.valueOf("bf92f9")
            radioactivity = 15f
            charge = 7f
            cost = 4.5f
            healthScaling = 0.01f
            explosiveness = 2f
        }
        grainBoundaryAlloy = object : SqItem("grain-boundary-alloy") {
            init {
                color = Color.valueOf("aaaaaa")
                radioactivity = 0.3f
                charge = 0.5f
                cost = 1.8f
            }
        }
        pureSilicon = object : SqItem("pure-silicon") {
            init {
                color = Color.valueOf("53565c")
                cost = 0.9f
            }
        }
        phaseCore = object : SqItem("phase-core") {
            init {
                color = Color.valueOf("ffd59e")
                radioactivity = 2.5f
                cost = 5f
                healthScaling = 0.1f
            }
        }
        standardAlloyPlate = object : SqItem("standard-alloy-plate") {
            init {
                color = Color.valueOf("cbd97f")
                cost = 1.45f
                healthScaling = 1.3f
            }
        }
        vectorizedChip = object : SqItem("vectorized-chip") {
            init {
                color = Color.valueOf("4a4b53")
                cost = 1.2f
                radioactivity = 0.85f
                explosiveness = 0.05f
            }
        }
    }

    open class SqItem(name: String) : Item(name), SeqElem {
        var ord = -1
        override fun order(): Int {
            return ord
        }

        override fun statValue(): StatValue? {
            return null
        }
    }

    open class EncapsulatedImagineEnergyItem(name: String) : SqItem(name) {
        override fun setStats() {
            super.setStats()
            stats.remove(Stat.explosiveness)
            stats.remove(Stat.radioactivity)
            stats.remove(Stat.charge)
            stats.remove(Stat.flammability)
            stats.addPercent(SqStat.informationAnnihilationFactor, 0.3f)
        }
    }
}
