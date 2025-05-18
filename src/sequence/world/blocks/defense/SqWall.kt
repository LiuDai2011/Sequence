package sequence.world.blocks.defense

import mindustry.world.blocks.defense.Wall
import mindustry.world.meta.Env
import sequence.core.SeqElem

class SqWall(name: String) : Wall(name), SeqElem {
    var ord: Int = -1
    override val order: Int
        get() = ord

    init {
        envDisabled = envDisabled or Env.scorching
    }

    override fun init() {
        super.init()
        health *= wallHealthMultiplier
    }

    companion object {
        const val wallHealthMultiplier = 4
    }
}
