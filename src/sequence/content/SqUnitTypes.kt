package sequence.content

import mindustry.gen.MechUnit
import mindustry.type.UnitType
import sequence.core.SeqElem
import sequence.util.IgnoredLocalName
import sequence.util.build
import sequence.world.entities.abilities.TriArcShieldAbility

object SqUnitTypes {
    fun load() {
        object : SqUnitType("test-register"), IgnoredLocalName {
            init {
                abilities.add(TriArcShieldAbility())
            }
        }.build { MechUnit.create() }
    }

    class SqUnitType(name: String) : UnitType(name), SeqElem {
        var ord = -1
        override val order: Int
            get() = ord
    }
}
