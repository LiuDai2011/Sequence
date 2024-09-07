package sequence.world.blocks.defense

import mindustry.world.blocks.defense.Wall
import mindustry.world.meta.Env
import sequence.core.SeqElem

abstract class SqWall(name: String) : Wall(name), SeqElem {
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
